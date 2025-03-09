/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.base;

import org.bitcoinj.base.internal.ByteUtils;

import java.math.BigInteger;
import java.util.Objects;

import static org.bitcoinj.base.internal.Preconditions.checkArgument;

public class Difficulty implements Comparable<Difficulty> {

    /**
     * Standard maximum value for difficulty target (nBits). Also called "difficulty 1".
     */
    public static final Difficulty STANDARD_MAX_DIFFICULTY_TARGET = Difficulty.ofCompact(0x1d00ffff);
    /**
     * A value for difficultyTarget (nBits) that allows (slightly less than) half of all possible hash solutions.
     * Used in unit testing.
     */
    public static final Difficulty EASIEST_DIFFICULTY_TARGET = Difficulty.ofCompact(0x207fffff);

    private final long compact;

    private Difficulty(long compact) {
        this.compact = compact;
    }

    /**
     * Construct a difficulty from a compact form ("nBits").
     *
     * @param compact compact form
     * @return constructed difficulty
     */
    public static Difficulty ofCompact(long compact) {
        long low24bits = compact & 0xffffff;
        checkArgument(low24bits <= 0x7fffff, () ->
                "sign bit 24 cannot be set: " + Long.toHexString(compact));
        return new Difficulty(compact);
    }

    /**
     * Construct a difficulty from an 256-bit integer value. Be aware that if you will use
     * {@link Difficulty#asInteger()} you will likely lose precision.
     *
     * @param value 256-bit integer value
     * @return constructed difficulty
     */
    public static Difficulty ofInteger(BigInteger value) {
        return new Difficulty(ByteUtils.encodeCompactBits(value));
    }

    /**
     * Inside a block the difficulty target is represented using a compact form.
     *
     * @return difficulty target as a long
     */
    public long compact() {
        return compact;
    }

    /**
     * Returns the difficulty target as a 256 bit value that can be compared to a SHA-256 hash. Be aware that if you
     * constructed this difficulty using {@link Difficulty#ofInteger(BigInteger)} you will likely lose precision.
     *
     * @return difficulty target as 256-bit integer value
     */
    public BigInteger asInteger() {
        return ByteUtils.decodeCompactBits(compact);
    }

    /**
     * Determines if the work represented by a given block hash meets or exceeds this difficulty target. The more
     * leading zero bits the hash has, the more work has been put into creating it.
     *
     * @param blockHash block hash that represents work
     * @return true if this target is met or exceeded by given work
     */
    public boolean isMetByWork(Sha256Hash blockHash) {
        BigInteger work = blockHash.toBigInteger();
        return work.compareTo(this.asInteger()) <= 0;
    }

    @Override
    public String toString() {
        return Long.toHexString(compact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.compact == ((Difficulty) o).compact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compact);
    }

    @Override
    public int compareTo(Difficulty other) {
        return Long.compare(this.compact, other.compact);
    }
}
