package io.jbotsim.ui.painting;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class JSLGraphLinkPainterTest {
    public static void main(String[] args) {

        Topology topology = new Topology();
        Node from = new Node();
        from.setLocation(50, 50);
        topology.addNode(from);

        Node to = new Node();
        from.setLocation(200, 200);
        topology.addNode(to);

        Link link = new Link(from, to);
        link.setProperty("date", topology.getTime());
        topology.addLink(link);

        JViewer viewer = new JViewer(topology);
        viewer.getJTopology().setLinkPainter(new JSLGraphLinkPainter());

        topology.start();
    }
}
