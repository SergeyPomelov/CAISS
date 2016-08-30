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

package benchmarks.ants.colonies.colony;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import benchmarks.ants.colonies.colony.ant.AntRunResult;

/**
 * Package local statistics for AntsColony work.
 * @author Sergey Pomelov on 29/04/2016.
 * @see AntsColony
 */
@ThreadSafe
@ParametersAreNonnullByDefault
final class AntsStatistics implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AntsStatistics.class);
    private static final long serialVersionUID = 3702542113880074571L;

    @Nonnull
    private final AtomicReference<AntRunResult> bestRun = new AtomicReference<>(null);
    @Nonnegative
    private final AtomicInteger antsGoodRuns = new AtomicInteger(0);
    @Nonnegative
    private final AtomicInteger antsRuns = new AtomicInteger(0);
    @Nonnegative
    private final AtomicLong bestRunLength = new AtomicLong(Long.MAX_VALUE);
    @Nonnegative
    private final AtomicLong avgRunLength = new AtomicLong(0L);
    @Nonnull
    private final StringBuilder journal = new StringBuilder(0);


    @Nonnull
    Optional<AntRunResult> getBestRun() {
        return Optional.ofNullable(bestRun.get());
    }

    void addFinishedRun(@Nonnegative long runLength) {
        antsRuns.incrementAndGet();
        avgRunLength.set(((avgRunLength.longValue() * antsRuns.get())
                + runLength) / (antsRuns.get() + 1));
    }

    void setNewBestRun(AntRunResult runResult, String runJournal) {
        bestRun.set(runResult);
        bestRunLength.set(runResult.getLength());
        if (log.isDebugEnabled()) {
            journal.append(runJournal);
            log.debug("ant find better: {}/{} avg:|{}|, solution:{}",
                    antsGoodRuns.incrementAndGet(),
                    antsRuns.get(),
                    avgRunLength.get(),
                    runJournal);
        }
    }

    @Nonnegative
    long getBestRunLength() {
        return bestRunLength.get();
    }

    @Nonnull
    String getJournal() {
        return journal.toString();
    }

}
