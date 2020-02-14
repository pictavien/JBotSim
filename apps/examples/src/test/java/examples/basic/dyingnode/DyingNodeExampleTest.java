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

package examples.basic.dyingnode;

import examples.ExampleTestHelper;
import io.jbotsim.core.Node;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DyingNodeExampleTest extends ExampleTestHelper {
    @Override
    protected Class getTestedClass() {
        return MainDyingNode.class;
    }

    private static Stream<Arguments> provideSeedsAndRounds() {
        return Stream.of(
                Arguments.of(123456789L, 4149),
                Arguments.of(3141596L, 1646),
                Arguments.of(99991111L, 1485),
                Arguments.of(224183818L, 1157),
                Arguments.of(2703814402L, 3620),
                Arguments.of(788128408L, 1548),
                Arguments.of(258725349L, 1897),
                Arguments.of(2246624749L, 4695),
                Arguments.of(1368222688L, 2893),
                Arguments.of(1220010590L, 1188),
                Arguments.of(172056694L, 1229),
                Arguments.of(1631323957L, 1389),
                Arguments.of(1708810312L, 1376),
                Arguments.of(780532665L, 4707)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSeedsAndRounds")
    public void checkDyingNodes(long seed, int expectedRounds)
            throws Exception {
        prepareTopology(seed);
        List<Node> nodes = testedTopology.getNodes();
        int nbRounds = 0;
        while (!testedTopology.getNodes().isEmpty()) {
            nbRounds++;
            iterate();
        }
        assertEquals(expectedRounds, nbRounds);
    }
}
