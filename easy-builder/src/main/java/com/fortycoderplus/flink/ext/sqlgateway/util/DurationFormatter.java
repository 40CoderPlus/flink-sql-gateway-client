/*
 * (c) Copyright 2023 40CoderPlus. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortycoderplus.flink.ext.sqlgateway.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DurationFormatter {

    public static String formatWithHighestUnit(Duration duration) {
        long nanos = duration.toNanos();

        List<TimeUnit> orderedUnits = Arrays.asList(
                TimeUnit.NANOSECONDS,
                TimeUnit.MICROSECONDS,
                TimeUnit.MILLISECONDS,
                TimeUnit.SECONDS,
                TimeUnit.MINUTES,
                TimeUnit.HOURS,
                TimeUnit.DAYS);

        TimeUnit highestIntegerUnit = IntStream.range(0, orderedUnits.size())
                .sequential()
                .filter(idx -> nanos % orderedUnits.get(idx).unit.getDuration().toNanos() != 0)
                .boxed()
                .findFirst()
                .map(idx -> {
                    if (idx == 0) {
                        return orderedUnits.get(0);
                    } else {
                        return orderedUnits.get(idx - 1);
                    }
                })
                .orElse(TimeUnit.MILLISECONDS);

        return String.format(
                "%d %s",
                nanos / highestIntegerUnit.unit.getDuration().toNanos(),
                highestIntegerUnit.getLabels().get(0));
    }

    private enum TimeUnit {
        DAYS(ChronoUnit.DAYS, singular("d"), plural("day")),
        HOURS(ChronoUnit.HOURS, singular("h"), plural("hour")),
        MINUTES(ChronoUnit.MINUTES, singular("min"), singular("m"), plural("minute")),
        SECONDS(ChronoUnit.SECONDS, singular("s"), plural("sec"), plural("second")),
        MILLISECONDS(ChronoUnit.MILLIS, singular("ms"), plural("milli"), plural("millisecond")),
        MICROSECONDS(ChronoUnit.MICROS, singular("µs"), plural("micro"), plural("microsecond")),
        NANOSECONDS(ChronoUnit.NANOS, singular("ns"), plural("nano"), plural("nanosecond"));

        private static final String PLURAL_SUFFIX = "s";

        private final List<String> labels;

        private final ChronoUnit unit;

        TimeUnit(ChronoUnit unit, String[]... labels) {
            this.unit = unit;
            this.labels = Arrays.stream(labels).flatMap(ls -> Arrays.stream(ls)).collect(Collectors.toList());
        }

        /**
         * @param label the original label
         * @return the singular format of the original label
         */
        private static String[] singular(String label) {
            return new String[] {label};
        }

        /**
         * @param label the original label
         * @return both the singular format and plural format of the original label
         */
        private static String[] plural(String label) {
            return new String[] {label, label + PLURAL_SUFFIX};
        }

        public List<String> getLabels() {
            return labels;
        }

        public ChronoUnit getUnit() {
            return unit;
        }

        public static String getAllUnits() {
            return Arrays.stream(TimeUnit.values())
                    .map(TimeUnit::createTimeUnitString)
                    .collect(Collectors.joining(", "));
        }

        private static String createTimeUnitString(TimeUnit timeUnit) {
            return timeUnit.name() + ": (" + String.join(" | ", timeUnit.getLabels()) + ")";
        }
    }
}
