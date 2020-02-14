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
import io.jbotsim.contrib.algos.Connectivity;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DominatingSetTest extends ExampleTestHelper {
    static boolean GENERATE_EXPECTED_RESULT = false;
    static int MIN_NB_NODES = 1;
    static int MAX_NB_NODES = 100;

    @Override
    protected Class getTestedClass() {
        return DominatingSet.class;
    }

    @ParameterizedTest
    @MethodSource("provideSeeds")
    public void checkStates(long seed, String expectedResult)
            throws Exception {
        prepareTopology(seed);
        int nbNodes = MIN_NB_NODES + Random.nextInt(MAX_NB_NODES - MIN_NB_NODES);
        deployTopology(testedTopology, nbNodes);
        testedTopology.selectNode(pickRandomNode());
        if (GENERATE_EXPECTED_RESULT)
            generateExpectedFile(expectedResult);
        checkTopologyAgainstXMLFile(expectedResult);
    }

    void deployTopology(Topology tp, int nbNodes) throws Exception {
        int width = tp.getWidth();
        int height = tp.getHeight();
        Class<? extends Node> cls = tp.getDefaultNodeModel();
        for (int i = 1; i < nbNodes; i++) {
            Node node = cls.newInstance();
            node.setLocation(Random.nextInt(width), Random.nextInt(height));
            tp.addNode(node);
        }
    }

    // region Seeds
    private static Stream<Arguments> provideSeeds() {
        return Stream.of(
                Arguments.of(  115115550L, "dominatingset-state-00.xml"),
                Arguments.of( 3075220485L, "dominatingset-state-01.xml"),
                Arguments.of(  288109594L, "dominatingset-state-02.xml"),
                Arguments.of(  543825989L, "dominatingset-state-03.xml"),
                Arguments.of( 2980420143L, "dominatingset-state-04.xml"),
                Arguments.of(  119278482L, "dominatingset-state-05.xml"),
                Arguments.of(  226314636L, "dominatingset-state-06.xml"),
                Arguments.of( 2430328094L, "dominatingset-state-07.xml"),
                Arguments.of(  355631632L, "dominatingset-state-08.xml"),
                Arguments.of(   23892109L, "dominatingset-state-09.xml"),
                Arguments.of(  291288912L, "dominatingset-state-10.xml"),
                Arguments.of( 2347510466L, "dominatingset-state-11.xml"),
                Arguments.of(  222269850L, "dominatingset-state-12.xml"),
                Arguments.of( 1311115517L, "dominatingset-state-13.xml"),
                Arguments.of(  745621958L, "dominatingset-state-14.xml"),
                Arguments.of(  313332258L, "dominatingset-state-15.xml"),
                Arguments.of(  194922401L, "dominatingset-state-16.xml"),
                Arguments.of( 2482530488L, "dominatingset-state-17.xml"),
                Arguments.of(   61069721L, "dominatingset-state-18.xml"),
                Arguments.of( 2803020694L, "dominatingset-state-19.xml"),
                Arguments.of( 3219915241L, "dominatingset-state-20.xml"),
                Arguments.of( 3089519822L, "dominatingset-state-21.xml"),
                Arguments.of( 1374419810L, "dominatingset-state-22.xml"),
                Arguments.of(   65622310L, "dominatingset-state-23.xml"),
                Arguments.of(  292163837L, "dominatingset-state-24.xml"),
                Arguments.of( 1569213019L, "dominatingset-state-25.xml"),
                Arguments.of(  114158578L, "dominatingset-state-26.xml"),
                Arguments.of( 2708530308L, "dominatingset-state-27.xml"),
                Arguments.of(  311227703L, "dominatingset-state-28.xml"),
                Arguments.of( 2511731175L, "dominatingset-state-29.xml")
        );
    }
    // endregion Seeds
}