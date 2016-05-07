/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergey Pomelov.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package simulation.control;

import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import simulation.structures.architecture.ArchitectureComponent;
import simulation.structures.architecture.CalculationNode;
import simulation.structures.architecture.Computer;
import simulation.structures.architecture.DataLink;
import simulation.structures.architecture.MemoryNode;
import simulation.structures.interaction.OperationWithData;

import static util.Constants.LS;
import static util.Restrictions.containsNull;

/**
 * Utility class which plans computational process by finding operation executors. Second purpose
 * is writing a computations process journal.
 * @author Sergey Pomelov on 9/3/14.
 */
final class TasksPlanner {
    private static final Logger log = LoggerFactory.getLogger(TasksPlanner.class);
    /** temporary list of current computer arithmetic nodes */
    @Nonnull
    private final Collection<CalculationNode> cores = new ArrayList<>(8);
    /** temporary list of current computer memory nodes */
    @Nonnull
    private final Collection<MemoryNode> memory = new ArrayList<>(4);
    /**
     * temporary table with information about computational tasks linked with components finish
     * time
     */
    @Nonnull
    private HashMap<ArchitectureComponent, Long> timeTable = new HashMap<>(8);
    /** computational process journal */
    @Nonnull
    private StringBuilder journal = new StringBuilder("");

    TasksPlanner(final Computer computer) {
        load(computer);
    }

    /**
     * @param computer for init class: could be automated. Currently if you want correct results you
     *                 should do it manually before any other tasks for this class.
     */
    void load(Computer computer) {
        if (computer == null) {
            throw new IllegalArgumentException("The computer param shouldn't be null!");
        }
        timeTable = new HashMap<>(16);
        cores.clear();
        memory.clear();
        cores.addAll(computer.getCalculationNodes());
        memory.addAll(computer.getMemoryNodes());
        journal = new StringBuilder("");
    }

    /**
     * @return computational process journal in text format.
     */
    @Nonnull
    String getLog() {
        return journal.toString();
    }

    @Nonnull
    Collection<CalculationNode> getArchNodes() {
        return ImmutableList.copyOf(cores);
    }

    @Nonnull
    Collection<MemoryNode> getMemoryNodes() {
        return ImmutableList.copyOf(memory);
    }

    private void addTask(ArchitectureComponent actor, Long startTime, Long time) {
        if (containsNull(actor, startTime, time)) {
            return;
        }
        final Long newTime = ((startTime < getTime(actor)) ? getTime(actor) : startTime) + time;
        timeTable.put(actor, newTime);
    }

    /**
     * @param operationWithData operation should be done
     * @return assign node for operationWithData and return it.
     */
    @Nonnull
    private ArrayList<CalculationNode> getSuccessArNode(OperationWithData operationWithData) {
        final ArrayList<CalculationNode> readyCores = new ArrayList<>(8);
        cores.stream().forEach(core -> core.getAllowedOperations().stream()
                .filter(oper -> oper.getOperation().equals(operationWithData))
                .forEach(oper -> readyCores.add(core)));
        return readyCores;
    }

    /**
     * @return print current time table state.
     */
    @Nonnull
    String printTimings() {
        final StringBuilder timings = new StringBuilder(64);
        timings.append(LS);
        final Iterator<ArchitectureComponent> componentIterator = timeTable.keySet().iterator();
        componentIterator.forEachRemaining(actor -> timings.append(actor.getName()).append(" - ")
                .append(timeTable.get(actor)).append("ms").append(LS));
        return "Timings: " + timings + LS + "Total time: " + getMaxTime() + " ms" + LS;
    }

    /** @return current predicted finishing time. */
    @Nonnull
    @Nonnegative
    private Long getMaxTime() {
        Long maxTime = 0L;
        final Iterator<Entry<ArchitectureComponent, Long>> componentIterator =
                timeTable.entrySet().iterator();
        //noinspection MethodCallInLoopCondition, by design
        for (Entry<ArchitectureComponent, Long> actor = componentIterator.next();
             componentIterator.hasNext() && (actor != null); actor = componentIterator.next()) {
            final Long endTime = timeTable.get(actor.getKey());
            if (maxTime < endTime) {
                maxTime = endTime;
            }
        }
        return maxTime;
    }

    /**
     * @param nodes nodes through which we should search
     * @return find first finishing node for planning.
     */
    @Nonnull
    Optional<CalculationNode> getFreeCalculationNode(Iterable<CalculationNode> nodes) {
        CalculationNode readyNode = null;
        for (final CalculationNode node : nodes) {
            if (node != null) {
                final Long timeNode;
                timeNode = timeTable.containsKey(node) ? getTime(node) : 0L;
                final Long timeReadyNode;
                //noinspection IfMayBeConditional, nope, this is better
                if ((readyNode != null) && timeTable.containsKey(readyNode)) {
                    timeReadyNode = getTime(readyNode);
                } else {
                    timeReadyNode = 0L;
                }
                if ((readyNode == null) || (timeNode < timeReadyNode)) {
                    readyNode = node;
                }
            }
        }
        return Optional.ofNullable(readyNode);
    }

    /**
     * @param inMemory          where is the data
     * @param dataLink          who should transfer data
     * @param core              destination of the data
     * @param operationWithData what shall be done
     */
    void transfer(MemoryNode inMemory, DataLink dataLink, CalculationNode core,
                  OperationWithData operationWithData) {
        if (containsNull(inMemory, dataLink, core, operationWithData)) {
            log.error("wrong input transfer({},{},{},{}), transfer failed!",
                    inMemory, dataLink.getName(), core, operationWithData);
            return;
        }

        final Optional<Float> optionalTime = dataLink.getTransferTime(operationWithData);
        if (optionalTime.isPresent()) {
            final long time = Math.round(optionalTime.get());
            final Long start = findPossibleStart(inMemory, core, dataLink);
            addTask(inMemory, start, time);
            addTask(dataLink, start, time);
            addTask(core, start, time);
            journal.append("trans: ").append(inMemory.getName()).append("->")
                    .append(dataLink.getName()).append("->").append(core.getName())
                    .append(" : ").append(operationWithData.getData().info()).append(' ')
                    .append(start).append("ms->").append(start + time).append("ms").append(LS);
        } else {
            log.error("wrong operation or wrong node, transfer failed!");
        }
    }

    private long findPossibleStart(MemoryNode inMemory, CalculationNode core, DataLink dataLink) {
        return Math.max(Math.max((timeTable.get(inMemory) != null) ? timeTable.get(inMemory) : 0L,
                (timeTable.get(core) != null) ? timeTable.get(core) : 0L),
                (timeTable.get(dataLink) != null) ? timeTable.get(dataLink) : 0L);
    }

    /**
     * @param component for whom we want timing
     * @return when it should be free.
     */
    @Nonnull
    @Nonnegative
    private Long getTime(ArchitectureComponent component) {
        return (timeTable.get(component) != null) ? timeTable.get(component) : 0L;
    }

    /**
     * @param core              who should work
     * @param operationWithData what shall be done
     */
    void calculate(CalculationNode core, OperationWithData operationWithData) {
        if (containsNull(operationWithData, core)) {
            log.error("wrong input calculate({},{}), calculation failed!", core, operationWithData);
            return;
        }

        final Optional<Long> time = core.getOperationTime(operationWithData);
        if (time.isPresent()) {
            final Long start = getTime(core);
            addTask(core, start, time.get());
            journal.append("comp: ").append(core.getName()).append(" : ")
                    .append(operationWithData.info()).append(start).append("ms->")
                    .append(start + time.get()).append("ms").append(LS);
        } else {
            log.error("wrong operation or wrong node, calculate failed!");
        }
    }
}
