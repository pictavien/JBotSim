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

package examples.centralized;

import examples.ExampleTestHelper;
import examples.Random;
import examples.basic.mobilebroadcast.MobileBroadcastNode;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ColoringTest extends ExampleTestHelper {
    static int MIN_NB_NODES = 10;
    static int MAX_NB_NODES = 40;

    @Override
    protected Class getTestedClass() {
        return Coloring.class;
    }

    private static Stream<Arguments> provideSeeds() {
        return Stream.of(
                Arguments.of(2849427948l, "coloring-state-00.xml"),
                Arguments.of(2898812548l, "coloring-state-01.xml"),
                Arguments.of(2944125432l, "coloring-state-02.xml"),
                Arguments.of(3071718520l, "coloring-state-03.xml"),
                Arguments.of(1130910306l, "coloring-state-04.xml"),
                Arguments.of(1781528851l, "coloring-state-05.xml"),
                Arguments.of(1562627007l, "coloring-state-06.xml"),
                Arguments.of(135134899l, "coloring-state-07.xml"),
                Arguments.of(464510212l, "coloring-state-08.xml"),
                Arguments.of(3126921024l, "coloring-state-09.xml"),
                Arguments.of(2019115918l, "coloring-state-10.xml"),
                Arguments.of(3048720313l, "coloring-state-11.xml"),
                Arguments.of(2379611612l, "coloring-state-12.xml"),
                Arguments.of(2655014896l, "coloring-state-13.xml")        );
    }

    @ParameterizedTest
    @MethodSource("provideSeeds")
    public void checkStates(long seed, String expectedResult)
            throws Exception {
        boolean generateExpectedResult = false;
        prepareTopology(seed);
        int nbNodes = MIN_NB_NODES + Random.nextInt(MAX_NB_NODES - MIN_NB_NODES);
        deployRandomNodes(testedTopology, nbNodes);
        if(generateExpectedResult)
            generateExpectedFile(expectedResult);
        checkColoring(testedTopology);
        checkTopologyAgainstXMLFile(expectedResult);
    }

    public void checkColoring(Topology tp) {
        for(Node src : tp.getNodes()) {
            for(Node tgt : src.getNeighbors()) {
                assertNotEquals(src.getColor(), tgt.getColor());
            }
        }
    }

    private static void deployRandomNodes(Topology tp, int nbNodes) {
        for (int i = 0; i < nbNodes; i++) {
            Node node = new Node();
            node.setID(i);
            node.setLocation(Random.nextInt(tp.getWidth()), Random.nextInt(tp.getHeight()));
            tp.addNode(node);
        }
    }
}