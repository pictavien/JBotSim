package io.jbotsim.graph.types;

import io.jbotsim.graph.utils.Helper;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static io.jbotsim.graph.GraphGenerator.getEdgePerm;
import static io.jbotsim.graph.utils.Helper.indexOf;

//import static spectral_tc.generation.GraphGenerator.getEdgePerm;

/**
 * Created by acasteig on 31/05/17.
 */
public class SLGraph implements java.io.Serializable, Comparable<SLGraph> {
    public Integer n, m;
    protected List<Edge> edges = new ArrayList<>();
    public SLGraph canonical = null;
    public static boolean TC3 = false;

    public SLGraph(int n) {
        this.n = n;
        this.m = (n*(n-1))/2;
        int k=0;
        for (int i=0; i<n; i++)
            for (int j=i+1; j<n; j++)
                edges.add(new Edge(i, j, k++));
    }

    public SLGraph(SLGraph g) {
        this.n = g.n;
        this.m = (n*(n-1))/2;
        for (Edge e : g.edges)
            edges.add(new Edge(e));
    }

    public SLGraph(Integer... ints){
        this(Arrays.asList(ints));
    }

    public SLGraph(List<Integer> dates) {
        this((int)((1+Math.sqrt(1+8* dates.size()))/2));
        setDates(dates);
    }

    public static int getN(String number){
        BigInteger ref = new BigInteger(number).multiply(BigInteger.TEN);
        BigInteger k = BigInteger.valueOf(3);
        while (k.pow(k.intValue()).compareTo(ref) < 0)
            k = k.add(BigInteger.ONE);
        int kint = k.intValue();
        return (int)(1+Math.sqrt(1+8* kint))/2;
    }

    public SLGraph(BigInteger number) {
        this(number.toString());
    }

    public SLGraph(String val){
        this (getN(val));
        BigInteger base = BigInteger.valueOf(m);
        BigInteger bi = new BigInteger(val);
        List<Integer> dates = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            dates.add(bi.mod(base.pow(i+1)).divide(base.pow(i)).intValue());
        }
        setDates(dates);
    }

    public String serialize(){
        BigInteger base = BigInteger.valueOf(edges.size());
        BigInteger result = BigInteger.ZERO;
        for (int i=0; i < m; i++)
            result = result.add(BigInteger.valueOf(edges.get(i).date).multiply(base.pow(i)));
        return result.toString();
    }

    protected void setDates(List<Integer> dates){
        for (int i=0; i<dates.size(); i++) {
            if (dates.get(i) != -1)
                edges.get(i).setDate(dates.get(i));
            else
                edges.get(i).disable();
        }
    }

    public List<Integer> getDates() {
        List<Integer> dates = new ArrayList<>();
        for (Edge e : edges)
            dates.add(e.isEnabled()?e.date:-1);
        return dates;
    }

    public int getDate(int u, int v) {
        return getEdge(u,v).date;
    }

    public int getDate(int index) {
        return getEdge(index).date;
    }

    public int getMaxDate(){
        return getDates().stream().max(Integer::compareTo).get();
    }

    public int getMinDate(){
        return getDates().stream().min(Integer::compareTo).get();
    }

    @Override
    public int hashCode() {
        return edges.hashCode();
    }

    @Override
    public boolean equals(Object o){
        return getDates().equals(((SLGraph) o).getDates());
    }

    @Override
    public int compareTo(SLGraph g) {
        for (int i = 0; i < m; i++){
            int val1 = edges.get(i).date;
            int val2 = g.edges.get(i).date;
            if (val1 != val2)
                return val1 - val2;
        }
        return 0;
    }

    public SLGraph compress(){
        SLGraph result = new SLGraph(this);
        List<Edge> tmp = result.getActiveEdges();
        int curDate = 0;
        while (! tmp.isEmpty()){
            List<Edge> minimal = new ArrayList<>();
            for (Edge e : tmp)
                if (Edge.isLocalMinimumWithin(e, tmp))
                    minimal.add(e);
            for (Edge e : minimal)
                e.date = curDate;
            curDate++;
            tmp.removeAll(minimal);
        }
        return result;
    }

    public SLGraph permuteNodes(List<Integer> nodePerm){
        List<Integer> edgePerm = getEdgePerm(nodePerm);
        List<Integer> ndates = new ArrayList<>();
        for (int i = 0; i < m; i++)
            ndates.add(getEdge(edgePerm.get(i)).getDate());
        return new SLGraph(ndates);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<Edge> getActiveEdges() {
        return edges.stream().filter(e -> e.isEnabled()).collect(Collectors.toList());
    }

    public List<Edge> getActiveEdges(int node) {
        return edges.stream().filter(e -> e.isEnabled() && e.isAdjacentTo(node)).collect(Collectors.toList());
    }

    public boolean isMinimal(){
        return getRemovableEdges().size() == 0;
    }

    public List<Edge> getRemovableEdges(){
        List<Edge> removable = new ArrayList<>();
        for (Edge e : getActiveEdges()) {
            e.disable();
            if (isTC())
                removable.add(e);
            e.enable();
        }
        return removable;
    }

    public boolean canRemove(Edge edge){
        edge.disable();
        boolean connected = isTC();
        edge.enable();
        return connected;
    }

    public boolean canRemove(Edge edge, int maxdepth){
        edge.disable();
        boolean connected = isTC(maxdepth);
        edge.enable();
        return connected;
    }

    public boolean canRemove(int index){
        disable(index);
        boolean connected = isTC();
        enable(index);
        return connected;
    }

    public List<Edge> getSortedEdges() {
        return getActiveEdges().stream().sorted().collect(Collectors.toList());
    }

    public String toTex(){
        String s ="{";
        for (Edge e : edges)
            s += e.getDate() + " ";
        return s.substring(0, s.length()-1)+"}";
    }

    public Edge getMinEdge(int u){
        Edge minEdge = null;
        for (Edge e : getActiveEdges(u)){
            if (minEdge == null || e.getDate() < minEdge.getDate())
                minEdge = e;
        }
        return minEdge;
    }

    public Edge getMaxEdge(int u){
        Edge maxEdge = null;
        for (Edge e : getActiveEdges(u)){
            if (maxEdge == null || e.getDate() > maxEdge.getDate())
                maxEdge = e;
        }
        return maxEdge;
    }

    public boolean isLocallyMinimal(int u, int v){
        Edge e = getEdge(u, v);
        for (Edge e2 : getActiveEdges()) {
            if (e2.isAdjacentTo(e) && e2.getDate() < e.getDate())
                return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return edges.toString();
    }

    public void enable(int edgeIndex){
        edges.get(edgeIndex).enable();
    }

    public void enableAll(Collection<Integer> list){
        list.forEach(i -> enable(i));
    }

    public void disable(int edgeIndex){
        edges.get(edgeIndex).disable();
    }

    public void disableAll(Collection<Integer> list) {
        list.forEach(i -> disable(i));
    }

    public boolean isEnabled(int k) {
        return edges.get(k).isEnabled();
    }

    public Edge getEdge(int k) {
        return edges.get(k);
    }

    public Edge getEdge(int u, int v) {
        return edges.get(indexOf(n,u,v));
    }

    public static SLGraph newRandomGraph(int n){
        List<Integer> list = new ArrayList<>(Helper.getEdgeList(n));
        Collections.shuffle(list);
        return new SLGraph(list);
    }

    public boolean isValid(){
        List<Edge> active = getActiveEdges();
        for (Edge e1 : active)
            for (Edge e2 : active)
                if (e1.isAdjacentTo(e2) && e1.getDate().equals(e2.getDate()))
                    return false;
        return true;
    }

    public SLGraph inducedSubGraph(List<Integer> nodes){
        SLGraph ngraph = new SLGraph(nodes.size());
        for (Edge e : ngraph.getEdges())
            e.disable();
        for (Edge e : getActiveEdges()){
            if (nodes.contains(e.from) && nodes.contains(e.to)) {
                Edge ne = ngraph.getEdge(nodes.indexOf(e.from), nodes.indexOf(e.to));
                ne.enable();
                ne.setDate(e.getDate());
            }
        }
        return ngraph;
    }

    public SLGraph inducedSubClique(List<Integer> nodes){
        SLGraph ngraph = new SLGraph(nodes.size());
        for (Edge e : getEdges()){
            if (nodes.contains(e.from) && nodes.contains(e.to)) {
                Edge ne = ngraph.getEdge(nodes.indexOf(e.from), nodes.indexOf(e.to));
                ne.setDate(e.getDate());
            }
        }
        return ngraph;
    }

    public SLGraph temporalSubGraph(int minDate, int maxDate){
        SLGraph ngraph = new SLGraph(this);
        for (Edge e : ngraph.getActiveEdges())
            if (e.getDate() < minDate || e.getDate() > maxDate)
                e.disable();
        return ngraph;
    }

    ///////////// ALGORITHMS

    private static void extendsPath(Map<Integer, Set<List<Integer>>> map, int u, int v) {
        Set<List<Integer>> uPaths = new HashSet<>(map.get(u));
        Set<List<Integer>> vPaths = new HashSet<>(map.get(v));
        for (List<Integer> path : uPaths){
            List<Integer> npath = new ArrayList<>(path);
            if (! npath.contains(v)) {
                npath.add(u);
                map.get(v).add(npath);
            }
        }
        for (List<Integer> path : vPaths){
            List<Integer> npath = new ArrayList<>(path);
            if (! npath.contains(u)) {
                npath.add(v);
                map.get(u).add(npath);
            }
        }
    }

    public Map<Integer, Set<List<Integer>>> getPaths(){
        Map<Integer, Set<List<Integer>>> pathsMap = new TreeMap<>();
        // Init empty path for each node
        for (int i=0; i<n; i++) {
            Set<List<Integer>> paths = new HashSet<>();
            paths.add(new ArrayList<>());
            pathsMap.put(i, paths);
        }

        // Foreach edge in chronological order
        for (Edge e : getSortedEdges())
            extendsPath(pathsMap, e.from, e.to); // extend existing journeys

        // Remove empty paths
        List<Integer> emptyList = new ArrayList<>();
        for (Set<List<Integer>> paths : pathsMap.values())
            paths.remove(emptyList);
        return pathsMap;
    }
//
//    public MLGraph spectralTC() {
//        Map<Integer, Set<List<Integer>>> pathsMap = getPaths();
//        for (Set<List<Integer>> paths : pathsMap.values())
//            for (List<Integer> path : new HashSet<>(paths))
//                if (path.size()<2) // remove single edges
//                    paths.remove(path);
//        MLGraph tc = new MLGraph(n);
//        for (int i=0; i<n; i++){
//            for (int k=2;k<n;k++) {
//                Set<Integer> preds = new HashSet<>();
//                for (List<Integer> path : pathsMap.get(i))
//                    if (path.size() == k)
//                        preds.add(path.get(0));
//                for (Integer pred : preds)
//                    tc.addLabel(pred, i, k);
//            }
//        }
//        return tc;
//    }

    public boolean isTC(){
        if (TC3)
            return isTC(3);
        List<Set<Integer>> predecessors = new ArrayList<>();
        for (int i=0; i<n; i++){
            Set<Integer> preds_i = new HashSet<>();
            preds_i.add(i);
            predecessors.add(preds_i);
        }
        for (Edge e : getSortedEdges()){
            Set<Integer> from = predecessors.get(e.from);
            Set<Integer> to = predecessors.get(e.to);
            int fromSize = from.size();
            if (fromSize != n) {
                from.addAll(to);
                if (from.size() != fromSize)
                    e.tag();
            }
            int toSize = to.size();
            if (toSize != n) {
                to.addAll(from);
                if (to.size() != toSize)
                    e.tag();
            }
        }
        for (Set<Integer> pred : predecessors)
            if (pred.size() != n)
                return false;
        return true;
    }
    public boolean isTC(int maxdepth){
        if (maxdepth == 0)
            return isTC();
        List<Map<Integer,Set<Integer>>> predecessors = new ArrayList<>();
        for (int i=0; i<n; i++){
            Set<Integer> preds_i = new HashSet<>();
            preds_i.add(i);
            Map<Integer, Set<Integer>> map = new HashMap<>();
            map.put(0, preds_i);
            for (int j=1; j<=maxdepth; j++)
                map.put(j, new HashSet<>());
            predecessors.add(map);
        }
        for (Edge e : getSortedEdges()){
            Map<Integer, Set<Integer>> from = predecessors.get(e.from);
            Map<Integer, Set<Integer>> to = predecessors.get(e.to);
            for (int hops = 0; hops < maxdepth; hops++)
                to.get(hops+1).addAll(from.get(hops));
            for (int hops = 0; hops < maxdepth; hops++)
                from.get(hops+1).addAll(to.get(hops));
        }
        for (int i=0; i<n; i++){
            Set<Integer> total_i = new HashSet<>();
            for (int hops = 0; hops < maxdepth + 1; hops++)
                total_i.addAll(predecessors.get(i).get(hops));
            if (total_i.size() != n)
                return false;
        }
        return true;
    }
}
