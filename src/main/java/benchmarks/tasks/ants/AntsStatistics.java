/*
 *     Computer and algorithm interaction simulation software (CAISS).
 *     Copyright (C) 2016 Sergei Pomelov
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

package benchmarks.tasks.ants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author Sergey Pomelov on 29/04/2016. Package local statistics for AntsColony work.
 * @see AntsColony
 */
@ThreadSafe
@ParametersAreNonnullByDefault
final class AntsStatistics implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(AntsStatistics.class);
    private static final long serialVersionUID = 3702542113880074571L;

    @Nonnegative
    private final AtomicInteger antsGoodRuns = new AtomicInteger(0);
    @Nonnegative
    private final AtomicInteger antsRuns = new AtomicInteger(0);
    @Nonnegative
    private final AtomicLong bestRunLength = new AtomicLong(Long.MAX_VALUE);
    @Nonnegative
    private final AtomicLong avgRunLength = new AtomicLong(0L);
    @Nonnull
    private final StringBuilder journal = new StringBuilder(512);

    void addFinishedRun(@Nonnegative long runLength) {
        antsRuns.incrementAndGet();
        avgRunLength.set(((avgRunLength.longValue() * antsRuns.get())
                + runLength) / (antsRuns.get() + 1));
    }

    void addGoodRun(String runJournal) {
        log.info("ant find better: {}/{} avg:|{}|, solution:{}",
                antsGoodRuns.incrementAndGet(),
                antsRuns.get(),
                avgRunLength.get(),
                runJournal);
    }

    @Nonnegative
    int getAntsGoodRuns() {
        return antsGoodRuns.get();
    }

    @Nonnegative
    int getAntsRuns() {
        return antsRuns.get();
    }

    @Nonnegative
    long getBestRunLength() {
        return bestRunLength.get();
    }

    void setBestRunLength(@Nonnegative long bestRunLength) {
        this.bestRunLength.set(bestRunLength);
    }

    @Nonnegative
    long getAvgRunLength() {
        return avgRunLength.get();
    }

    @Nonnull
    String getJournal() {
        return journal.toString();
    }

}
