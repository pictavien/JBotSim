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

package io.jbotsim.io.format.dot;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DotImportExportCoherencyTest {
    private static final int TEST_SEED = 314156;
    private static final int MAX_DEGREE = 5;
    private static final int MAX_NUMBER_OF_NODES = 100;
    private static final int NB_IDEMPOTENCE_TESTS = 200;

    @Parameterized.Parameter
    public int prngSeeds;

    @Parameterized.Parameters(name = "{index}: seed={0}")
    public static Collection<Integer> makers() {
        Random rnd = new Random(TEST_SEED);
        ArrayList<Integer> res = new ArrayList<>(NB_IDEMPOTENCE_TESTS);
        res.add(rnd.nextInt());
        for (int i = 1; i < NB_IDEMPOTENCE_TESTS; i++) {
            int prev = res.get(i - 1);
            if (prev % 2 == 0) {
                res.add((rnd.nextInt() + prev) / 2);
            } else {
                res.add(3 * (rnd.nextInt() + prev) + 1);
            }
        }
        return res;
    }

    public static class TestNode1 extends Node {
    }

    public static class TestNode2 extends Node {
    }

    private Node buildRandomNode(Random rnd, Topology tp) throws IllegalAccessException, InstantiationException {
        Class<? extends Node>[] templates = new Class[]{TestNode1.class, TestNode2.class, Node.class};
        int c = rnd.nextInt(templates.length);
        Node result = templates[c].newInstance();
        result.setLocation(rnd.nextInt(tp.getWidth()), rnd.nextInt(tp.getHeight()));

        return result;
    }

    private Topology buildRandomTopology(int nbNodes, Random rnd,
                                         Link.Orientation orientation)
            throws InstantiationException, IllegalAccessException {
        Topology tp = new Topology();
        tp.setOrientation(orientation);

        for (int i = 0; i < nbNodes; i++)
            tp.addNode(buildRandomNode(rnd, tp));

        Node[] nodes = tp.getNodes().toArray(new Node[0]);

        if (nbNodes == 1) {
            tp.getLink(nodes[0], nodes[0], orientation);
        } else {
            for (int src = 0; src < nbNodes; src++) {
                Node srcNode = nodes[src];
                int nbEdges = rnd.nextInt(MAX_DEGREE);
                while (nbEdges-- > 0) {
                    Node dstNode;
                    do {
                        dstNode = nodes[rnd.nextInt(nbNodes)];
                    } while (srcNode == dstNode);


                    if (srcNode.getCommonLinkWith(dstNode) == null &&
                            srcNode.getOutLinkTo(dstNode) == null &&
                            dstNode.getOutLinkTo(srcNode) == null) {
                        tp.addLink(new Link(srcNode, dstNode, orientation));
                    } else if (dstNode.getOutLinkTo(dstNode) != null) {
                        tp.addLink(new Link(srcNode, dstNode,
                                Link.Orientation.DIRECTED));
                    }
                }
            }
        }

        return tp;
    }

    private Topology buildRandomTopology(int nbNodes, Random rnd)
            throws InstantiationException, IllegalAccessException {
        Link.Orientation orientation = rnd.nextBoolean()
                ? Link.Orientation.DIRECTED : Link.Orientation.UNDIRECTED;
        return buildRandomTopology(nbNodes, rnd, orientation);
    }

    public void checkTopologyGraph(Topology tp1, Topology tp2) {
        assertEquals(tp1.getNodes().size(), tp2.getNodes().size());
        assertEquals(tp1.getLinks(Link.Orientation.DIRECTED).size(),
                tp2.getLinks(Link.Orientation.DIRECTED).size());
        assertEquals(tp1.getLinks(Link.Orientation.UNDIRECTED).size(),
                tp2.getLinks(Link.Orientation.UNDIRECTED).size());

        HashMap<Integer, Node> nodes1 = new HashMap<>();
        for (Node n : tp1.getNodes()) {
            nodes1.put(n.getID(), n);
        }
        HashMap<Integer, Node> nodes2 = new HashMap<>();
        for (Node n : tp2.getNodes()) {
            nodes2.put(n.getID(), n);
        }
        assertEquals(nodes1.keySet(), nodes2.keySet());
        for (Node src1 : tp1.getNodes()) {
            Node src2 = nodes2.get(src1.getID());
            Set<Integer> neighbours = new HashSet<>();
            for (Node n : src1.getNeighbors()) {
                neighbours.add(n.getID());
            }
            for (Node n : src2.getNeighbors()) {
                assertTrue(neighbours.contains(n.getID()));
            }
        }
    }

    public static String reorderLines(String s) {
        String[] lines = s.split("\n");
        Arrays.sort(lines);
        StringBuilder sb = new StringBuilder();
        for (String l : lines)
            sb.append(l).append('\n');
        return sb.toString();
    }

    private void runTest(int size, Random rnd) throws IllegalAccessException, InstantiationException {
        DotTopologySerializer dotIO = new DotTopologySerializer(false);
        Topology tp1 = buildRandomTopology(size, rnd);
        Topology tp2 = new Topology();

        String s1 = dotIO.exportToString(tp1);
        dotIO.importFromString(tp2, s1);
        checkTopologyGraph(tp1, tp2);
        String s2 = dotIO.exportToString(tp2);

        assertEquals(reorderLines(s1), reorderLines(s2));
    }

    @Test
    public void importExportCoherencyTest() throws IllegalAccessException, InstantiationException {
        Random rnd = new Random(prngSeeds);
        int maxSmallSize = 3;
        for (int i = 0; i <= maxSmallSize; i++)
            runTest(i, rnd);
        runTest(1 + maxSmallSize + rnd.nextInt(MAX_NUMBER_OF_NODES - maxSmallSize), rnd);
    }

    /**
     * Test that serialization fails if one add directed links in an
     * undirected Topology with out ensuring both ways arcs.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public void importExportFailureOnMixedOrientationTest() throws IllegalAccessException, InstantiationException {
        Random rnd = new Random(prngSeeds);
        DotTopologySerializer dotIO = new DotTopologySerializer(false);
        Topology tp1 = buildRandomTopology(MAX_NUMBER_OF_NODES, rnd, Link.Orientation.UNDIRECTED);
        List<Node> nodes = tp1.getNodes();
        Node src = nodes.get(rnd.nextInt(nodes.size()));
        Node tgt = new Node();
        tp1.addNode(tgt);

        /* First we add a directed link in one way.
         * Serialization should fail
         */
        tp1.addLink(new Link(src, tgt, Link.Orientation.DIRECTED));
        String s1 = dotIO.exportToString(tp1);

        Topology tp2 = new Topology();
        dotIO.importFromString(tp2, s1);
        String s2 = dotIO.exportToString(tp2);
        assertEquals(reorderLines(s1), reorderLines(s2));
        assertEquals(tp1.getNodes().size(), tp2.getNodes().size());
        assertEquals(tp1.getLinks(Link.Orientation.UNDIRECTED).size(),
                tp2.getLinks(Link.Orientation.UNDIRECTED).size());
        /* Here is the error ! */
        assertNotEquals(tp1.getLinks(Link.Orientation.DIRECTED).size(),
                tp2.getLinks(Link.Orientation.DIRECTED).size());

        /* Second, we add an arc in the other way. The Topology should be,
         * now, a coherent undirected topology.
         */
        tp1.addLink(new Link(tgt, src, Link.Orientation.DIRECTED));
        s1 = dotIO.exportToString(tp1);
        tp2 = new Topology();
        dotIO.importFromString(tp2, s1);
        s2 = dotIO.exportToString(tp2);
        assertEquals(reorderLines(s1), reorderLines(s2));
        assertEquals(tp1.getNodes().size(), tp2.getNodes().size());
        assertEquals(tp1.getLinks(Link.Orientation.UNDIRECTED).size(),
                tp2.getLinks(Link.Orientation.UNDIRECTED).size());
        assertEquals(tp1.getLinks(Link.Orientation.DIRECTED).size(),
                tp2.getLinks(Link.Orientation.DIRECTED).size());
    }

    /*
    @Test
    public void importExportWirelessTest() throws IllegalAccessException, InstantiationException {
        Random rnd = new Random(prngSeeds);
        DotTopologySerializer dotIO = new DotTopologySerializer(false);
        Topology tp1 = new Topology();
        tp1.enableWireless();
        Node src = new Node();
        Node tgt = new Node();
        tp1.addNode(src);

        Point loc = src.getLocation();
        loc.setLocation(loc.x, loc.getY() + tp1.getCommunicationRange() / 2);
        tp1.addNode(tgt);
        tp1.addLink(new Link(src, tgt, Link.Orientation.UNDIRECTED, Link.Mode.WIRED));

        assertEquals(1, tp1.getLinks().size());

        tgt.setLocation(loc);
        assertEquals(2, tp1.getLinks().size());

        String s1 = dotIO.exportToString(tp1);

        Topology tp2 = new Topology();
        dotIO.importFromString(tp2, s1);
        String s2 = dotIO.exportToString(tp2);
        assertEquals(reorderLines(s1), reorderLines(s2));
    }*/
}