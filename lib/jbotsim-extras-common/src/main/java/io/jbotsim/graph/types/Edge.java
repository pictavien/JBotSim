package io.jbotsim.graph.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasteig on 31/05/17.
 */
public class Edge implements java.io.Serializable, Comparable<Edge> {
    public int from;
    public int to;

    protected int date;

    private boolean enabled;

    private boolean tagged = false;

    public Edge(int from, int to) {
        this(from, to, 0, true);
    }

    public Edge(int from, int to, int date) {
        this(from, to, date, true);
    }

    public Edge(int from, int to, int date, boolean enabled) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.enabled = enabled;
    }

    public Edge(Edge e) {
        this(e.from, e.to, e.date, e.enabled);
    }

    public Integer getOtherEndpoint(int node) {
        return from==node ? to:from;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public boolean isTagged() {
        return tagged;
    }

    public void tag() {
        this.tagged = true;
    }

    public void untag() {
        this.tagged = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAdjacentTo(Integer node){
        return from==node || to==node;
    }

    public boolean isAdjacentTo(Edge e){
        return from==e.from || to==e.to || from==e.to || to==e.from;
    }

    public boolean isLeftAdjacentTo(Edge e){
        return from==e.from || to==e.from;
    }

    public boolean isRightAdjacentTo(Edge e){
        return from==e.to || to==e.to;
    }

    public boolean contains(int v){
        return from==v || to==v;
    }

    @Override
    public boolean equals(Object obj) {
        Edge e = (Edge) obj;
        return (from == e.from && to == e.to);
    }

    public static List<List<Edge>> getAdjacentEdgesSplit(SLGraph graph, int index){
        Edge edge = graph.getEdge(index);
        List<Edge> left = new ArrayList<>();
        List<Edge> right = new ArrayList<>();
        for (int i=0; i<graph.m; i++) {
            if (i != index) {
                Edge other = graph.getEdge(i);
                other.date = graph.getDate(i);
                if (other.isLeftAdjacentTo(edge))
                    left.add(other);
                if (other.isRightAdjacentTo(edge))
                    right.add(other);
            }
        }
        List<List<Edge>> adjEdges = new ArrayList<>();
        adjEdges.add(left);
        adjEdges.add(right);
        return adjEdges;
    }
    @Override
    public int hashCode() {
        return (from+1) * (to+1) + (to+1);
    }

    //@Override
    //public String toString() {
    //    return "(" + from + "," + to + ")";
    //}

    @Override
    public String toString() {
        return "" + (enabled?date:-1);
    }

    public String toString2() {
        return "(" + from + "," + to + ")";
    }

    @Override
    public int compareTo(Edge o) {
        return date - o.date;
    }

    public int compareLex(Edge o) {
        int dFrom = from - o.from;
        if (dFrom != 0)
            return dFrom;
        return to - o.to;
    }

    public static boolean isLocalMinimumWithin(Edge e, List<Edge> edges){
        for (Edge e2 : edges)
            if (! e2.equals(e))
                if (e2.isAdjacentTo(e))
                    if (e2.date < e.date)
                        return false;
        return true;
    }

}
