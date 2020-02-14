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
    static int MIN_NB_NODES = 1;
    static int MAX_NB_NODES = 40;

    @Override
    protected Class getTestedClass() {
        return Dijkstra.class;
    }

    @ParameterizedTest
    @MethodSource("provideSeeds")
    public void checkStates(long seed)
            throws Exception {
        prepareTopology(seed);
        int nbNodes = MIN_NB_NODES + Random.nextInt(MAX_NB_NODES - MIN_NB_NODES);
        deployTopology(testedTopology, nbNodes);
        testedTopology.selectNode(pickRandomNode());
          checkProperties(testedTopology);
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
                Arguments.of(927120188L),
                Arguments.of(2586211009L),
                Arguments.of(3006414085L),
                Arguments.of(149314093L),
                Arguments.of(3038723760L),
                Arguments.of(67139927L),
                Arguments.of(194124366L),
                Arguments.of(290511497L),
                Arguments.of(646229232L),
                Arguments.of(262129828L),
                Arguments.of(580828517L),
                Arguments.of(383211965L),
                Arguments.of(14964087L),
                Arguments.of(782516455L),
                Arguments.of(1160711396L),
                Arguments.of(94919108L),
                Arguments.of(192137869L),
                Arguments.of(261433355L),
                Arguments.of(100908385L),
                Arguments.of(2784322904L),
                Arguments.of(446717975L),
                Arguments.of(222066278L),
                Arguments.of(399027438L),
                Arguments.of(140776733L),
                Arguments.of(2078313492L),
                Arguments.of(1604711788L),
                Arguments.of(1147832568L),
                Arguments.of(1700315861L),
                Arguments.of(1419122176L),
                Arguments.of(2614527307L)
        );
    }
    // endregion Seeds
}