/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package examples.basic.icons;

import examples.ExampleTestHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class IconsExampleTest extends ExampleTestHelper {
    @Override
    protected Class getTestedClass() {
        return MainIcons.class;
    }

    private static Stream<Arguments> provideSeedsAndRounds() {
        return Stream.of(
                Arguments.of(123456789L, 11, "icons-expected-state-00.xml"),
                Arguments.of(3141596L, 15, "icons-expected-state-01.xml"),
                Arguments.of(99991111L, 1485, "icons-expected-state-02.xml"),
                Arguments.of(224183818L, 1157, "icons-expected-state-03.xml"),
                Arguments.of(2703814402L, 3620, "icons-expected-state-04.xml"),
                Arguments.of(788128408L, 1548, "icons-expected-state-05.xml"),
                Arguments.of(258725349L, 1897, "icons-expected-state-06.xml"),
                Arguments.of(2246624749L, 4695, "icons-expected-state-07.xml"),
                Arguments.of(1368222688L, 2893, "icons-expected-state-08.xml"),
                Arguments.of(1220010590L, 1188, "icons-expected-state-09.xml"),
                Arguments.of(172056694L, 1229, "icons-expected-state-10.xml"),
                Arguments.of(1631323957L, 1389, "icons-expected-state-11.xml"),
                Arguments.of(1708810312L, 1376, "icons-expected-state-12.xml"),
                Arguments.of(780532665L, 4707, "icons-expected-state-13.xml")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSeedsAndRounds")
    public void checkMovingIcons(long seed, int nbRounds, String expectedResult)
            throws Exception {
        boolean generateExpectedResult = false;
        iterateAndCheckState(seed, nbRounds, expectedResult, generateExpectedResult);
    }
}
