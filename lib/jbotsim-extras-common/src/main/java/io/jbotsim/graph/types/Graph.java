package io.jbotsim.graph.types;

import java.util.List;

/**
 * <p>The {@link Graph} interface provides basic method for JBotSim graphs manipulation.</p>
 */
public interface Graph {

    /**
     * <p>Provides the list of {@link Edge}s in the graph.</p>
     * @return a {@link List} of the nodes in the graph
     */
    List<Edge> getEdges();

    /**
     * <p>Provides the list of nodes in the graph.</p>
     * @return a {@link List} of the nodes in the graph
     */
    List<Integer> getNodes();

    /**
     * <p>Provides the degree of the specified vertex.</p>
     * <p>Note: loops are counted twice.</p>
     * @param vertex the vertex to be tested
     * @return the degree of the vertex
     */
    Integer degreeOf(int vertex);
}
