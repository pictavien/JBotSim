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

package io.jbotsim.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * These tests check the behavior of a `Topology` against mixed use of wired
 * and wireless links.
 */
public class WirelessLinkTest {
    static final boolean SHOW_NODE_LOCATIONS = false;
    static final boolean CHECK_NUMBER_OF_LINKS = true;

    /**
     * This test checks if a wireless link is created when a node enters the
     * communication range of another one and if the link disappears when the
     * node goes away out of the range.
     */
    @Test
    public void checkMovementLinkCreationTest() {
        Topology tp = createTwoUnlinkedNodesTopology();
        Node src = tp.findNodeById(0);
        Node tgt = tp.findNodeById(1);

        moveTgtInCR(src, tgt, 1);
        moveTgtOutCR(src, tgt, 0);
        moveTgtInCR(src, tgt, 1);
        moveTgtOutCR(src, tgt, 0);
    }

    /**
     * This test checks the number of links created when wireless and wired mode
     * are mixed. Here the scenario is as follow:
     * - a wireless link is created by moving a node closer to the other one
     * - a wired link is created between the nodes
     * - the wireless link is deleted by moving a node out of the communication
     * range.
     */
    @Test
    public void checkWirelessWiredCreationTest() {
        Topology tp = createTwoUnlinkedNodesTopology();
        Node src = tp.findNodeById(0);
        Node tgt = tp.findNodeById(1);

        /* tgt enters the communication range of src */
        moveTgtInCR(src, tgt, 1);

        /* we add a wired link between src and tgt */
        wiredLinkSrcTgt(src, tgt, 2);

        /* tgt moves to far */
        moveTgtOutCR(src, tgt, 1);
    }

    /**
     * This test checks the number of links created when wireless and wired mode
     * are mixed. Here the scenario is as follow:
     * - a wired link is created between the nodes
     * - a wireless link is created by moving a node closer to the other one
     * - the wireless link is deleted by moving a node out of the communication
     * range.
     */
    @Test
    public void checkWiredWirelessCreationTest() {
        Topology tp = createTwoUnlinkedNodesTopology();
        Node src = tp.findNodeById(0);
        Node tgt = tp.findNodeById(1);

        /* we add a wired link between src and tgt */
        wiredLinkSrcTgt(src, tgt, 1);

        /* tgt enters the communication range of src */
        moveTgtInCR(src, tgt, 2);

        /* tgt moves to far */
        moveTgtOutCR(src, tgt, 1);
    }

    /*
     * Create a new `Topology` with two nodes referred in above tests as `src`
     * (the first created) and `tgt` (the second one).
     * The `Topology` supports wireless and is set up as undirected.
     * `tgt` is located far enough (actually twice the communication range) from
     * `src`. Before returning, the method checks that no link exists.
     */
    Topology createTwoUnlinkedNodesTopology() {
        Topology tp = new Topology();
        tp.setOrientation(Link.Orientation.UNDIRECTED);
        tp.enableWireless();
        double cr = tp.getCommunicationRange();

        Node src = new Node();
        tp.addNode(src);
        assertEquals(0, src.getID());

        Node tgt = new Node();
        tp.addNode(src.getX(), src.getY() + 2 * cr, tgt);
        assertEquals(1, tgt.getID());

        /* tgt is too far from src */
        displayNodesLoc(src, tgt);
        assertEquals(0, tp.getLinks().size());

        return tp;
    }

    /*
     * Move `tgt` relatively to the location of `src`. The new location of `tgt`
     * is computed by a translation of `src` on the Y-axis. The translation
     * distance is `crScale * Topology.getCommunicationRange()`.
     * Once `tgt` has been translated, the number of links of the topology is
     * compared to `expectedNumberOfLinks`.
     */
    void moveTgt(Node src, Node tgt, double crScale, int expectedNumberOfLinks) {
        Topology tp = src.getTopology();
        double cr = tp.getCommunicationRange();
        assertEquals((int) cr, (int) src.getCommunicationRange());
        tgt.setLocation(src.getX(), src.getY() + cr * crScale);
        displayNodesLoc(src, tgt);
        if (CHECK_NUMBER_OF_LINKS)
            assertEquals(expectedNumberOfLinks, tp.getLinks().size());
        else
            System.err.println("expected = " + expectedNumberOfLinks +
                    " actual = " + tp.getLinks().size());
    }

    /*
     * Move `tgt` out of the wireless communication range.
     */
    void moveTgtOutCR(Node src, Node tgt, int expectedNumberOfLinks) {
        moveTgt(src, tgt, 2.0, expectedNumberOfLinks);
    }

    /*
     * Move `tgt` into the wireless communication range.
     */
    void moveTgtInCR(Node src, Node tgt, int expectedNumberOfLinks) {
        moveTgt(src, tgt, 0.5, expectedNumberOfLinks);
    }

    /*
     * Create an undirected wired link between `src` and `tgt`.
     * After the addition of the new link, the number of links of the topology
     * is compared to `expectedNumberOfLinks`.
     */
    void wiredLinkSrcTgt(Node src, Node tgt, int expectedNumberOfLinks) {
        Topology tp = src.getTopology();
        Link l = new Link(src, tgt, Link.Orientation.UNDIRECTED, Link.Mode.WIRED);
        tp.addLink(l);
        displayNodesLoc(src, tgt);
        if (CHECK_NUMBER_OF_LINKS)
            assertEquals(expectedNumberOfLinks, tp.getLinks().size());
        else
            System.err.println("expected = " + expectedNumberOfLinks +
                    " actual = " + tp.getLinks().size());
    }

    void displayNodesLoc(Node src, Node tgt) {
        if (!SHOW_NODE_LOCATIONS)
            return;

        System.out.println("src at " + src.getLocation());
        System.out.println("tgt at " + tgt.getLocation());
        double d = src.distance(tgt);
        double cr = src.getCommunicationRange();
        assertEquals((int) src.getCommunicationRange(), (int) tgt.getCommunicationRange());

        System.out.println("distance src-tgt = " + d + " " + ((d <= cr) ? "<=" : ">") +
                " communication range (" + cr + ")");
    }
}
