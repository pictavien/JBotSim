package io.jbotsim.topology;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.graph.types.Graph;
import io.jbotsim.graph.types.SLGraph;
import io.jbotsim.topology.converters.SLGraphToTopologyConverter;

/**
 * Created by acasteig on 22/12/16.
 */

/**
 * <p>The {@link GraphRenderer} is able to a {@link Graph} object into a displayable {@link Topology}.</p>
 *
 * <p>For now the {@link #renderGraph(Graph)} method only takes {@link SLGraph} into account.</p>
 */
public class GraphRenderer {

    /**
     * <p>Transform the provided {@link Graph} into a displayable {@link Topology}.</p>
     *
     * <p>The graph is automatically scaled to fit into the {@link Topology}.</p>
     * @param graph
     * @return
     */
    public static Topology renderGraph(Graph graph){
        Topology tp = new Topology();

        if(graph instanceof SLGraph)
            tp = SLGraphToTopologyConverter.getTopology((SLGraph)graph);

        autoscale(tp);
        return tp;
    }

    /**
     * <p>Automatically scales the position of the {@link Node}s of the {@link Topology} to match its boundaries.</p>
     * <p>Note that this method changes the {@link Node}s' position without respect for their
     * {@link io.jbotsim.core.Link}s.</p>
     * @param topology the {@link Topology} to scale/
     */
    private static void autoscale(Topology topology){
        double Xmax = 0, Ymax = 0, Xmin = Double.MAX_VALUE, Ymin = Double.MAX_VALUE;
        for (Node node : topology.getNodes()){
            if (node.getX() > Xmax)
                Xmax = node.getX();
            if (node.getY() > Ymax)
                Ymax = node.getY();
            if (node.getX() < Xmin)
                Xmin = node.getX();
            if (node.getY() < Ymin)
                Ymin = node.getY();
        }
        //TODO: strange behaviour expected with single Node Topology (zero division)
        double width = Xmax - Xmin;
        double height = Ymax - Ymin;
        double availableWidth = topology.getWidth()*0.8;
        double availableHeight = topology.getHeight()*0.8;
        double scale = Math.min(availableWidth/width, availableHeight/height);
        for (Node node : topology.getNodes()){
            node.setLocation(node.getX() - Xmin, node.getY() - Ymin);
            node.setLocation(node.getX()*scale + topology.getWidth()*0.1, node.getY()*scale + topology.getHeight()*0.1);
        }
    }

}
