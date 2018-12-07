package io.jbotsim.topology.converters;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.graph.types.Edge;
import io.jbotsim.graph.types.SLGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasteig on 01/11/17.
 */
public class SLGraphToTopologyConverter {
    public static Topology getTopology(SLGraph g) {
        return getTopology(g, g.n);
    }

    public static Topology getTopology(SLGraph g, int n){
        Topology tp = new Topology(400,400);
        tp.disableWireless();
        double unit=Math.PI*2.0/n;
        int scale=150;
        for (int i = 0; i < n; i++) {
            double angle = unit * i - Math.PI/2.0;
            tp.addNode(50 + scale + Math.cos(angle) * scale,
                    50 + scale + Math.sin(angle) * scale, tp.newInstanceOfModel("default"));
        }
        for (Link l : new ArrayList<>(tp.getLinks()))
            tp.removeLink(l);
        List<Node> nodes = tp.getNodes();
        for (Edge e : g.getActiveEdges()){
            Link l = new Link(nodes.get(e.from), nodes.get(e.to));
            l.setProperty("date", e.getDate());
            if (e.isTagged())
                l.setWidth(2);
            tp.addLink(l);
        }
        //addK4EALDLabels(tp, g);
        return tp;
    }

//    private static void addK4EALDLabels(Topology tp, SLGraph g) {
//        SLGraph k4 = Pincher.getSubClique(g, 4);
//        for (int i=0; i<4; i++) {
//            tp.getNodes().get(i).setState(IntervalTC.getEALD(k4, i));
//        }
//    }
}
