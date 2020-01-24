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

package io.jbotsim.adapters.jgrapht;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.ui.JViewer;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.WeightedPseudograph;

public class JGraphTMain {

    protected static final String JGRAPHT = "JGraphT test";

    public static void main(String[] args) {
        Topology topology = new Topology();

        new JViewer(topology);
        topology.addCommand(JGRAPHT);
        topology.addCommandListener(new MyCommandListener(topology));

        topology.start();
    }

    private static class MyCommandListener implements CommandListener {
        private Topology topology;

        public MyCommandListener(Topology topology) {

            this.topology = topology;
        }

        @Override
        public void onCommand(String command) {
            if (command.equalsIgnoreCase(JGRAPHT)) {
//                JGraphTAdapter jGraphTAdapter = new JGraphTAdapter();
                WeightedPseudograph<Node, WeightedLink> pseudograph = JGraphTAdapter.toWeightedPseudograph(topology);
                ConnectivityInspector<Node, WeightedLink> connectivityInspector = new ConnectivityInspector<>(pseudograph);
                System.out.println("Connected: " + connectivityInspector.isConnected());


                DirectedWeightedPseudograph<Node, WeightedLink> directedPseudograph = JGraphTAdapter.toDirectedWeightedPseudograph(topology);
                KosarajuStrongConnectivityInspector<Node, WeightedLink> kosarajuInspector = new KosarajuStrongConnectivityInspector<>(directedPseudograph);
                System.out.println("Strongly-Connected: " + kosarajuInspector.isStronglyConnected());
            }
        }
    }
}
