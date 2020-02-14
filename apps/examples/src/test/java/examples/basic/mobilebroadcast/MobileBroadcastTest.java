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

package examples.basic.mobilebroadcast;

import examples.ExampleTestHelper;
import examples.Random;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MobileBroadcastTest extends ExampleTestHelper {
    static int MAX_NB_NODES = 100;

    @Override
    protected Class getTestedClass() {
        return Main.class;
    }

    private static Stream<Arguments> provideSeedsAndRounds() {
        return Stream.of(
                Arguments.of(1187526848L, 3752, "mobile-broadcast-state-00.xml"),
                Arguments.of(52020382L, 1835, "mobile-broadcast-state-01.xml"),
                Arguments.of(1994327645L, 4192, "mobile-broadcast-state-02.xml"),
                Arguments.of(79666526L, 3169, "mobile-broadcast-state-03.xml"),
                Arguments.of(3131814482L, 2842, "mobile-broadcast-state-04.xml"),
                Arguments.of(2449223272L, 332, "mobile-broadcast-state-05.xml"),
                Arguments.of(729524137L, 2373, "mobile-broadcast-state-06.xml"),
                Arguments.of(643917313L, 751, "mobile-broadcast-state-07.xml"),
                Arguments.of(44848269L, 480, "mobile-broadcast-state-08.xml"),
                Arguments.of(224328392L, 6029, "mobile-broadcast-state-09.xml"),
                Arguments.of(2096729302L, 168, "mobile-broadcast-state-10.xml"),
                Arguments.of(1407213749L, 5817, "mobile-broadcast-state-11.xml"),
                Arguments.of(489717398L, 75, "mobile-broadcast-state-12.xml"),
                Arguments.of(38812495L, 5214, "mobile-broadcast-state-13.xml")
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
