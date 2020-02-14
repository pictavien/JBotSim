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
    static boolean GENERATE_EXPECTED_RESULT = false;

    @Override
    protected Class getTestedClass() {
        return Coloring.class;
    }

    @ParameterizedTest
    @MethodSource("provideSeeds")
    public void checkStates(long seed, String expectedResult)
            throws Exception {
        prepareTopology(seed);
        int nbNodes = MIN_NB_NODES + Random.nextInt(MAX_NB_NODES - MIN_NB_NODES);
        deployRandomNodes(testedTopology, nbNodes);
        if(GENERATE_EXPECTED_RESULT)
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

    // region Seeds
    private static Stream<Arguments> provideSeeds() {
        return Stream.of(
                Arguments.of( 2272216199L, "coloring-state-00.xml"),
                Arguments.of(  724428213L, "coloring-state-01.xml"),
                Arguments.of(  327233330L, "coloring-state-02.xml"),
                Arguments.of(  145613112L, "coloring-state-03.xml"),
                Arguments.of(  304647057L, "coloring-state-04.xml"),
                Arguments.of( 2904026419L, "coloring-state-05.xml"),
                Arguments.of( 1164224934L, "coloring-state-06.xml"),
                Arguments.of(  920622943L, "coloring-state-07.xml"),
                Arguments.of(  266418708L, "coloring-state-08.xml"),
                Arguments.of( 1426718294L, "coloring-state-09.xml"),
                Arguments.of( 2214612478L, "coloring-state-10.xml"),
                Arguments.of( 1799822989L, "coloring-state-11.xml"),
                Arguments.of(  907129761L, "coloring-state-12.xml"),
                Arguments.of(  225968916L, "coloring-state-13.xml"),
                Arguments.of( 1967928193L, "coloring-state-14.xml"),
                Arguments.of( 1271930367L, "coloring-state-15.xml"),
                Arguments.of(  285206964L, "coloring-state-16.xml"),
                Arguments.of(  272321201L, "coloring-state-17.xml"),
                Arguments.of( 2896219729L, "coloring-state-18.xml"),
                Arguments.of( 1993332333L, "coloring-state-19.xml"),
                Arguments.of(  307215942L, "coloring-state-20.xml"),
                Arguments.of( 1616121885L, "coloring-state-21.xml"),
                Arguments.of( 2712525339L, "coloring-state-22.xml"),
                Arguments.of(  719024799L, "coloring-state-23.xml"),
                Arguments.of( 2118722074L, "coloring-state-24.xml"),
                Arguments.of( 2376328722L, "coloring-state-25.xml"),
                Arguments.of( 1936130908L, "coloring-state-26.xml"),
                Arguments.of(  272857588L, "coloring-state-27.xml"),
                Arguments.of( 1653217506L, "coloring-state-28.xml"),
                Arguments.of( 2726326585L, "coloring-state-29.xml")
        );
    }
    // endregion Seeds
}