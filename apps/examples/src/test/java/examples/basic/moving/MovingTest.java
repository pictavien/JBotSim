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

package examples.basic.moving;

import examples.ExampleTestHelper;
import examples.Random;
import examples.basic.mobilebroadcast.MobileBroadcastNode;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class MovingTest extends ExampleTestHelper {
    static int MAX_NB_NODES = 100;

    @Override
    protected Class getTestedClass() {
        return Main.class;
    }

    private static Stream<Arguments> provideSeedsAndRounds() {
        return Stream.of(
                Arguments.of(2960822718l, 2148, "moving-state-00.xml"),
                Arguments.of(175756200l, 529, "moving-state-01.xml"),
                Arguments.of(1047519949l, 5053, "moving-state-02.xml"),
                Arguments.of(2747726654l, 5912, "moving-state-03.xml"),
                Arguments.of(13482930l, 5068, "moving-state-04.xml"),
                Arguments.of(2710719295l, 5475, "moving-state-05.xml"),
                Arguments.of(166495702l, 1631, "moving-state-06.xml"),
                Arguments.of(151877483l, 4679, "moving-state-07.xml"),
                Arguments.of(890813843l, 191, "moving-state-08.xml"),
                Arguments.of(371423136l, 3825, "moving-state-09.xml"),
                Arguments.of(1803228689l, 596, "moving-state-10.xml"),
                Arguments.of(2661122963l, 3920, "moving-state-11.xml"),
                Arguments.of(910329277l, 4770, "moving-state-12.xml"),
                Arguments.of(123557590l, 3889, "moving-state-13.xml")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSeedsAndRounds")
    public void checkStates(long seed, int nbRounds, String expectedResult)
            throws Exception {
        boolean generateExpectedResult = false;
        iterateAndCheckState(seed, nbRounds, expectedResult, (tp) -> {
            deployRandomNodes(tp, Random.nextInt(MAX_NB_NODES));
        }, generateExpectedResult);
    }

    private static void deployRandomNodes(Topology tp, int nbNodes) {
        for (int i = 0; i < nbNodes; i++){
            Node node = new MobileBroadcastNode();
            node.setID(i);
            node.setLocation(Random.nextInt(tp.getWidth()), Random.nextInt(tp.getHeight()));
            tp.addNode(node);
        }
    }
}