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

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest extends ExampleTestHelper {
    static boolean GENERATE_EXPECTED_RESULT = false;
    static int MIN_NB_NODES = 1;
    static int MAX_NB_NODES = 40;

    @Override
    protected Class getTestedClass() {
        return Dijkstra.class;
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
        checkProperties(testedTopology);
        checkTopologyAgainstXMLFile(expectedResult);
    }

    private void checkProperties(Topology tp) {
        assertTrue(Connectivity.isConnected(tp));
        // We check if the number of thick links (width > 1) is the number of
        // nodes - 1
        int nbThick = 0;
        for (Link l : tp.getLinks())
            if (l.getWidth() > 1)
                nbThick++;
        assertEquals(tp.getNodes().size() - 1, nbThick);
    }

    /*
     * Deployment take a random location for a first Node. Then, newly generated
     * node is located randomly in the communication range of the last generated
     * node.
     */
    void deployTopology(Topology tp, int nbNodes) {
        double cR = tp.getCommunicationRange();

        Node prevNode = new Node();
        prevNode.setID(0);
        setLocationAround(tp, tp.getWidth() / 2, tp.getHeight() / 2, cR, prevNode);
        tp.addNode(prevNode);

        for (int i = 1; i < nbNodes; i++) {
            Node node = new Node();
            node.setID(i);
            setLocationAround(tp, prevNode.getX(), prevNode.getY(), cR, node);
            tp.addNode(node);
            prevNode = node;
        }
    }

    private void setLocationAround(Topology tp, double x, double y, double cR, Node n) {
        double angle = 2 * Math.PI * Random.random();
        double dx = .9 * cR * Math.cos(angle);
        double dy = .9 * cR * Math.sin(angle);
        n.setLocation(x + dx, y + dy);
    }

    // region Seeds
    private static Stream<Arguments> provideSeeds() {
        return Stream.of(
                Arguments.of(   54649622l, "dijkstra-state-00.xml"),
                Arguments.of(   29963404l, "dijkstra-state-01.xml"),
                Arguments.of(   23926647l, "dijkstra-state-02.xml"),
                Arguments.of( 1823426971l, "dijkstra-state-03.xml"),
                Arguments.of( 1498520659l, "dijkstra-state-04.xml"),
                Arguments.of( 2606923371l, "dijkstra-state-05.xml"),
                Arguments.of( 3060314622l, "dijkstra-state-06.xml"),
                Arguments.of( 3050016382l, "dijkstra-state-07.xml"),
                Arguments.of( 1589221107l, "dijkstra-state-08.xml"),
                Arguments.of( 1520132333l, "dijkstra-state-09.xml"),
                Arguments.of(  308248611l, "dijkstra-state-10.xml"),
                Arguments.of(  294984020l, "dijkstra-state-11.xml"),
                Arguments.of(  578314305l, "dijkstra-state-12.xml"),
                Arguments.of( 2084826558l, "dijkstra-state-13.xml"),
                Arguments.of(  326345610l, "dijkstra-state-14.xml"),
                Arguments.of( 2718626159l, "dijkstra-state-15.xml"),
                Arguments.of(  548330741l, "dijkstra-state-16.xml"),
                Arguments.of( 1541516371l, "dijkstra-state-17.xml"),
                Arguments.of( 1914322302l, "dijkstra-state-18.xml"),
                Arguments.of(  121902761l, "dijkstra-state-19.xml"),
                Arguments.of(   54910674l, "dijkstra-state-20.xml"),
                Arguments.of(  986122447l, "dijkstra-state-21.xml"),
                Arguments.of( 1390712986l, "dijkstra-state-22.xml"),
                Arguments.of(  315952060l, "dijkstra-state-23.xml"),
                Arguments.of(  974926728l, "dijkstra-state-24.xml"),
                Arguments.of(  237418848l, "dijkstra-state-25.xml"),
                Arguments.of( 3266816386l, "dijkstra-state-26.xml"),
                Arguments.of(  649925900l, "dijkstra-state-27.xml"),
                Arguments.of( 1837627249l, "dijkstra-state-28.xml"),
                Arguments.of( 2125019424l, "dijkstra-state-29.xml")
        );
    }
    // endregion Seeds
}