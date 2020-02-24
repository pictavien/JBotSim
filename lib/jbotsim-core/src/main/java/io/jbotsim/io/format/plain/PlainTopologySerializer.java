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
package io.jbotsim.io.format.plain;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;

import java.util.HashMap;

public class PlainTopologySerializer implements TopologySerializer {

    protected static final String EOL = "\n";
    protected static final String SPACE = " ";
    protected static final String ORIENTATION_MARKER_DIRECTED = "-->";
    protected static final String ORIENTATION_MARKER_UNDIRECTED = "<-->";

    public void importFromString(Topology topology, String data){
        new Importer(topology, data).importTopology();
    }

    public String exportToString(Topology topology){
        return new Exporter(topology).exportTopology();
    }

    private class Importer {
        private Topology topology;
        private String data;

        public Importer(Topology topology, String data) {
            this.topology = topology;
            this.data = data;
        }

        public void importTopology() {
            importTopologyParameters();
            HashMap<String, Node> nodeTable = importNodes();
            importLinks(nodeTable);
        }

        private void importTopologyParameters() {
            topology.setCommunicationRange(Double.parseDouble(data.substring(data.indexOf(SPACE) + 1, data.indexOf(EOL))));
            jumpToNextLine();
            topology.setSensingRange(Double.parseDouble(data.substring(data.indexOf(SPACE) + 1, data.indexOf(EOL))));
            jumpToNextLine();
        }


        // region nodes
        private HashMap<String, Node> importNodes() {
            HashMap<String, Node> nodeTable = new HashMap<>();

            while (hasNodes())
                importNode(nodeTable);

            return nodeTable;
        }

        private void importNode(HashMap<String, Node> nodeTable) {
            Point location = parseNodeLocation();
            try {
                Node node = topology.newInstanceOfModel("default");
                node.setLocation(location);
                topology.addNode(node);
                String id = data.substring(0, data.indexOf(SPACE));
                node.setProperty("id", id);
                nodeTable.put(id, node);
                jumpToNextLine();
            } catch (Exception e) {}
        }

        private Point parseNodeLocation() {
            double x = new Double(data.substring(data.indexOf("x") + 3, data.indexOf(", y")));
            double y = 0;
            double z = 0;
            if (data.contains("z")) {
                y = new Double(data.substring(data.indexOf("y") + 3, data.indexOf(", z")));
                z = new Double(data.substring(data.indexOf("z") + 3, data.indexOf("]")));
            }else{
                y = new Double(data.substring(data.indexOf("y") + 3, data.indexOf("]")));
            }

            return new Point (x, y, z);
        }

        private boolean hasNodes() {
            return data.indexOf("[") > 0;
        }
        // endregion nodes

        // region links
        private void importLinks(HashMap<String, Node> nodeTable) {
            while (hasLinks())
                importLink(nodeTable);
        }

        private boolean hasLinks() {
            return data.indexOf("--") > 0;
        }

        private void importLink(HashMap<String, Node> nodeTable) {
            String firstNodeId = data.substring(0, data.indexOf(SPACE));
            String secondNodeId = data.substring(data.indexOf(">") + 2, data.indexOf(EOL));
            Node n1 = nodeTable.get(firstNodeId);
            Node n2 = nodeTable.get(secondNodeId);
            Link.Orientation orientation = isCurrentLinkUndirected() ? Link.Orientation.UNDIRECTED : Link.Orientation.DIRECTED;
            topology.addLink(new Link(n1, n2, orientation, Link.Mode.WIRED));
            jumpToNextLine();
        }

        private boolean isCurrentLinkUndirected() {
            return data.indexOf("<") > 0 && data.indexOf("<") < data.indexOf(EOL);
        }

        // endregion links

        private void jumpToNextLine() {
            data = nextLine();
        }
        private String nextLine() {
            return data.substring(data.indexOf(EOL) + 1);
        }
    }

    private class Exporter {
        private Topology topology;

        public Exporter(Topology topology) {
            this.topology = topology;
        }

        public String exportTopology() {
            StringBuffer res = new StringBuffer();
            exportTopologyParams(res);
            exportNodes(res);
            exportLinks(res);
            return res.toString();
        }

        private void exportLinks(StringBuffer res) {
            for (Link l : topology.getLinks())
                exportLink(res, l);
        }

        private void exportLink(StringBuffer res, Link l) {
            if (l.isWireless())
                return;

            String sourceSerializationTag = getSerializationTag(l.source);
            String destinationSerializationTag = getSerializationTag(l.destination);
            String orientationMarker = l.isDirected() ? ORIENTATION_MARKER_DIRECTED : ORIENTATION_MARKER_UNDIRECTED;

            String linkAsString  = sourceSerializationTag + SPACE + orientationMarker + SPACE + destinationSerializationTag;

            res.append(linkAsString + EOL);
        }

        private String getSerializationTag(Node node) {
            return node.getID() + "";
        }

        private void exportNodes(StringBuffer res) {
            for (Node n : topology.getNodes())
                exportNode(res, n);
        }

        private void exportNode(StringBuffer res, Node n) {
            String locationAsString = exportNodeLocation(n);
            String nodeSerializationTag = getSerializationTag(n);
            res.append(nodeSerializationTag + SPACE + locationAsString + EOL);
        }

        private String exportNodeLocation(Node n) {
            Point p2d = new Point(n.getLocation().getX(), n.getLocation().getY());

            String locationAsString = p2d.toString();
            int startIndex = locationAsString.indexOf("[") - 1; // this keeps the leading space

            return locationAsString.substring(startIndex);
        }

        private void exportTopologyParams(StringBuffer res) {
            res.append("cR" + SPACE + topology.getCommunicationRange() + EOL);
            res.append("sR" + SPACE + topology.getSensingRange() + EOL);
        }
    }
}
