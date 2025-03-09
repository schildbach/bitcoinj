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

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class DifficultyTest {

    @Test
    @Parameters(method = "testVectors")
    public void compactToInteger(long compact, String expectedInteger) {
        Difficulty difficulty = Difficulty.ofCompact(compact);
        BigInteger integer = difficulty.asInteger();
        assertEquals(expectedInteger, integer.toString(16));
    }

    @Test
    @Parameters(method = "testVectors")
    public void integerToCompact(long expectedCompact, String integerHex) {
        Difficulty difficulty = Difficulty.ofInteger(new BigInteger(integerHex, 16));
        long compact = difficulty.compact();
        assertEquals(expectedCompact, compact);
    }

    // vectors from https://en.bitcoin.it/wiki/Difficulty
    private Object[] testVectors() {
        return new Object[] {
                new Object[] { 0x1d00ffff, "ffff0000000000000000000000000000000000000000000000000000" }, // difficulty 1
                new Object[] { 0x1b0404cb, "404cb000000000000000000000000000000000000000000000000" },
        };
    }
}
