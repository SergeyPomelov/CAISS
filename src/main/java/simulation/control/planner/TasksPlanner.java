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

package simulation.control.planner;

import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import simulation.structures.architecture.ArchitectureComponent;
import simulation.structures.architecture.CalculationNode;
import simulation.structures.architecture.Computer;
import simulation.structures.architecture.DataLink;
import simulation.structures.architecture.MemoryNode;
import simulation.structures.interaction.OperationWithData;
import util.FormatUtil;

import static util.Constants.LS;
import static util.FormatUtil.formatTime;
import static util.Restrictions.containsNull;

/**
 * Utility class which plans computational process by finding operation executors. Second purpose
 * is writing a computations process journal.
 * @author Sergey Pomelov on 9/3/14.
 */
@SuppressWarnings("ObjectAllocationInLoop")
public final class TasksPlanner {

    private static final Logger log = LoggerFactory.getLogger(TasksPlanner.class);
    private static final double PERCENT = 100.0D;

    /** temporary list of current computer arithmetic nodes */
    @Nonnull
    private final Collection<CalculationNode> calculationNodes = new ArrayList<>(0);
    /** temporary list of current computer memory nodes */
    @Nonnull
    private final Collection<MemoryNode> memoryNodes = new ArrayList<>(0);
    /**
     * temporary table with information about computational tasks linked with components finish
     * time
     */
    @Nonnull
    private final HashMap<ArchitectureComponent, Long> timeTable = new HashMap<>(8);
    @Nonnull
    private final Collection<OperationRecord> operationsHistory = new ArrayList<>(0);
    @Nullable
    private final TimeUnit inputTimeUnit;

    public TasksPlanner(Computer computer, @Nullable TimeUnit inputTimeUnit) {
        load(computer);
        this.inputTimeUnit = inputTimeUnit;
    }

    public TasksPlanner(Computer computer) {
        this(computer, null);
    }

    public void barrierNow() {
        final long maxTime = getMaxTime();
        for (CalculationNode node : calculationNodes) {
            final long busyTime = getTime(node);
            if (getTime(node) < maxTime) {
                timeTable.put(node, maxTime);
                operationsHistory.add(new WaitOperationRecord(busyTime, maxTime, inputTimeUnit, node));
            }
        }
    }

    /**
     * @param computer for init class: could be automated. Currently if you want correct results you
     *                 should do it manually before any other tasks for this class.
     */
    @SuppressWarnings("WeakerAccess")
    void load(Computer computer) {
        if (computer == null) {
            throw new IllegalArgumentException("The computer param shouldn't be null!");
        }
        timeTable.clear();
        calculationNodes.clear();
        memoryNodes.clear();
        calculationNodes.addAll(computer.getCalculationNodes());
        memoryNodes.addAll(computer.getMemoryNodes());
    }

    @Nonnull
    Collection<CalculationNode> getArchNodes() {
        return ImmutableList.copyOf(calculationNodes);
    }

    @Nonnull
    Collection<MemoryNode> getMemoryNodes() {
        return ImmutableList.copyOf(memoryNodes);
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
        calculationNodes.stream().forEach(core -> core.getAllowedOperations().stream()
                .filter(oper -> oper.getOperation().equals(operationWithData))
                .forEach(oper -> readyCores.add(core)));
        return readyCores;
    }

    @Nonnull
    public String printHistory() {
        final StringBuilder timings = new StringBuilder(64);
        final long totalTime = getMaxTime();
        final Iterator<OperationRecord> componentIterator = operationsHistory.iterator();
        componentIterator.forEachRemaining(actor -> timings.append(actor).append(
                percentOf(actor.getDuration(), totalTime)).append(LS));
        return LS + "Timings: " + timings + LS + "Total time: " +
                formatTime(totalTime, inputTimeUnit) + LS;
    }

    private static String percentOf(long duration, long totalTime) {
        return '(' + FormatUtil.percentFormat((PERCENT * duration) / totalTime) + ")%";
    }

    /**
     * @return print current time table state.
     */
    @Nonnull
    public String printTimings() {
        final StringBuilder timings = new StringBuilder(64);
        timings.append(LS);
        final Iterator<ArchitectureComponent> componentIterator = timeTable.keySet().iterator();
        componentIterator.forEachRemaining(actor -> timings.append(actor.getName()).append(" - ")
                .append(formatTime(timeTable.get(actor), inputTimeUnit)).append(LS));
        return "Timings: " + timings + LS + "Total time: " + formatTime(getMaxTime(), inputTimeUnit) + LS;
    }

    /** @return current predicted finishing time. */
    @Nonnull
    @Nonnegative
    public Long getMaxTime() {
        Long maxTime = 0L;
        for (Entry<ArchitectureComponent, Long> actor : timeTable.entrySet()) {
            final Long endTime = actor.getValue();
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
    public Optional<CalculationNode> getFreeCalculationNode(Iterable<CalculationNode> nodes) {
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
     * @param memory          where is the data
     * @param dataLink          who should transfer data
     * @param core              destination of the data
     * @param operationWithData what shall be done
     */
    public void transfer(MemoryNode memory, DataLink dataLink, CalculationNode core,
                         OperationWithData operationWithData) {
        if (containsNull(memory, dataLink, core, operationWithData)) {
            log.error("wrong input transfer({},{},{},{}), transfer failed!",
                    memory, dataLink.getName(), core, operationWithData);
            return;
        }

        final Optional<Float> optionalTime = dataLink.getTransferTime(operationWithData);
        if (optionalTime.isPresent()) {
            final long time = Math.round(optionalTime.get());
            final Long start = findPossibleStart(memory, core, dataLink);
            addTask(memory, start, time);
            addTask(dataLink, start, time);
            addTask(core, start, time);
            operationsHistory.add(new TransferOperationRecord(start, start + time, inputTimeUnit,
                    memory, dataLink, core, operationWithData));
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
    public void calculate(CalculationNode core, OperationWithData operationWithData) {
        if (containsNull(operationWithData, core)) {
            log.error("wrong input calculate({},{}), calculation failed!", core, operationWithData);
            return;
        }

        final Optional<Long> time = core.getOperationNeededResources(operationWithData);
        if (time.isPresent()) {
            final Long start = getTime(core);
            addTask(core, start, time.get());
            operationsHistory.add(new CalculationOperationRecord(start, start + time.get(), inputTimeUnit,
                    core, operationWithData));
        } else {
            log.error("wrong operation or wrong node, calculate failed!");
        }
    }

}
