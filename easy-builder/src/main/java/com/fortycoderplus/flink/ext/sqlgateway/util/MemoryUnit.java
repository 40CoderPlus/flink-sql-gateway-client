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

import java.util.Locale;

public enum MemoryUnit {
    BYTES(new String[] {"b", "bytes"}, 1L),
    KILO_BYTES(new String[] {"k", "kb", "kibibytes"}, 1024L),
    MEGA_BYTES(new String[] {"m", "mb", "mebibytes"}, 1024L * 1024L),
    GIGA_BYTES(new String[] {"g", "gb", "gibibytes"}, 1024L * 1024L * 1024L),
    TERA_BYTES(new String[] {"t", "tb", "tebibytes"}, 1024L * 1024L * 1024L * 1024L);

    private final String[] units;

    private final long multiplier;

    MemoryUnit(String[] units, long multiplier) {
        this.units = units;
        this.multiplier = multiplier;
    }

    public String[] getUnits() {
        return units;
    }

    public long getMultiplier() {
        return multiplier;
    }

    public static String getAllUnits() {
        return concatenateUnits(
                BYTES.getUnits(),
                KILO_BYTES.getUnits(),
                MEGA_BYTES.getUnits(),
                GIGA_BYTES.getUnits(),
                TERA_BYTES.getUnits());
    }

    public static boolean hasUnit(String text) {
        final String trimmed = text.trim();
        final int len = trimmed.length();
        int pos = 0;

        char current;
        while (pos < len && (current = trimmed.charAt(pos)) >= '0' && current <= '9') {
            pos++;
        }

        final String unit = trimmed.substring(pos).trim().toLowerCase(Locale.US);

        return unit.length() > 0;
    }

    private static String concatenateUnits(final String[]... allUnits) {
        final StringBuilder builder = new StringBuilder(128);

        for (String[] units : allUnits) {
            builder.append('(');

            for (String unit : units) {
                builder.append(unit);
                builder.append(" | ");
            }

            builder.setLength(builder.length() - 3);
            builder.append(") / ");
        }

        builder.setLength(builder.length() - 3);
        return builder.toString();
    }
}
