package io.jbotsim.graph;

import io.jbotsim.graph.utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acasteig on 11/07/17.
 */
public class GraphGenerator {
//    // Current performance : selects 4311014401 out of 14! (= 87178291200)
//    public static boolean select(List<Integer> p) { // OPT
//        // 0 to be inserted after (indices are shifted -1)
//        if (p.size() == 14) {
//            // all couples corresponds to triplets with future 0
//            return (p.get(8) == 1 && p.get(13) == 2) ||
//                    (p.get(4) == 1 && p.get(11) == 2) ||
//                    (p.get(4) == 2 && p.get(11) == 1) ||
//                    (p.get(8) == 1 && p.get(11) == 2) ||
//                    (p.get(4) == 1 && p.get(8) == 2) ||
//                    (p.get(4) == 2 && p.get(8) == 1) ||
//                    (p.get(3) == 1 && p.get(4) == 2) ||
//                    (p.get(0) == 1 && p.get(1) == 2) ||
//                    (p.get(0) == 1 && p.get(4) == 2);
//        } else if (p.size() == 10) { // n = 5
//            return p.get(0) == 1 || p.get(8) == 1;
//        } else { // n = 4
//            return true;
//        }
//    }
//
//    public static Iterator<List<Integer>> getIterator(int n){
//        List<Integer> initPerm = new ArrayList<>();
//        for (int i = 1; i < (n*(n-1))/2; i++) // 0 to be inserted later
//            initPerm.add(i);
//
//        return new PermutationsIterator<>(initPerm);
//    }
//
//    public static SLGraph getCanonical(SLGraph graph){
//        if (graph.canonical == null) { // TODO test if useful to remember it
//            graph.canonical = graph;
//            for (List<Integer> edgePerm : getIsomorphicEdgePerms(graph.n)) {
//                List<Integer> ndates = new ArrayList<>();
//                for (int i = 0; i < graph.m; i++)
//                    ndates.add(graph.getEdge(edgePerm.get(i)).getDate());
//                SLGraph ngraph = new SLGraph(ndates);
//                if (ngraph.compareTo(graph.canonical) < 0)
//                    graph.canonical = ngraph;
//            }
//        }else{
//            System.out.println("USEFUL REMEMBERING CANONICAL");
//        }
//        return graph.canonical;
//    }
//
//    public static List<List<Integer>> isomorphicEdgePerms = null;
//    public static List<List<Integer>> getIsomorphicEdgePerms(int n){
//        if (isomorphicEdgePerms == null) {
//            isomorphicEdgePerms = new ArrayList<>();
//            List<Integer> nodes = getNodeList(n);
//            Iterator<List<Integer>> iter = new PermutationsIterator<>(nodes);
//            while (iter.hasNext()) {
//                List<Integer> edgePerm = getEdgePerm(iter.next());
//                if (edgePerm.get(0) == 0) // OPT
//                    isomorphicEdgePerms.add(edgePerm);
//            }
//            isomorphicEdgePerms.remove(0); // identity
//        }
//        return isomorphicEdgePerms;
//    }

    public static List<Integer> getEdgePerm(List<Integer> nodePerm){
        List<Integer> edgePerm = new ArrayList<>();
        for (int i=0; i<nodePerm.size(); i++){
            for (int j=i+1; j < nodePerm.size(); j++){
                edgePerm.add(Helper.indexOf(nodePerm.size(), nodePerm.get(i), nodePerm.get(j)));
            }
        }
        return edgePerm;
    }
////    @Test
////    public void testGetIsomorphicEdgePerms(){
////        System.out.println(Helper.getEdgeList(5));
////        for (List<Integer> perm : getIsomorphicEdgePerms(5)){
////            System.out.println(perm);
////        }
////    }
//
//    public static void filterNoRemovableClique(String inFile, String outFile, int n, int cliqueOrder){
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File(inFile)));
//            BufferedWriter pw = new BufferedWriter(new FileWriter(new File(outFile)));
//            String line;
//            int i=0;
//            while ((line = br.readLine()) != null) {
//                SLGraph graph = new SLGraph(line);
//                if (! Clique.canRemoveClique(graph, cliqueOrder))
//                    pw.write(graph.serialize()+"\n");
//                if (i % 1000000 == 0)
//                    System.out.println(i);
//                i++;
//            }
//            br.close();
//            pw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void transitivizeAll(String inFile, String outFile, int n){
//        Iterator<SLGraph> iter = new FileIterator(inFile);
//        try {
//            BufferedWriter pw = new BufferedWriter(new FileWriter(new File(outFile)));
//            int k=0;
//            while (iter.hasNext()) {
//                SLGraph g = iter.next();
//                k = k + 1;
//                MLGraph tc = g.spectralTC();
//                pw.write(tc.toIntsString()+" "+ Cycle.getSignature(g)+" "+ g.serialize() + "\n");
//                if (k%100000==0) {
//                    System.out.println(k + "/" + Helper.getNbCompressedGraphs(n));
//                }
//            }
//            pw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void generateAll(String outFile, int n){
//        Set<SLGraph> buffer = new HashSet<>();
//        try {
//            BufferedWriter pw = new BufferedWriter(new FileWriter(new File(outFile)));
//            Iterator<List<Integer>> iter = getIterator(n);
//            long i=0;
//            while (iter.hasNext()) {
//                i++;
//                List<Integer> perm = iter.next();
//                if (select(perm)) {
//                    perm.add(0, 0);
//                    buffer.add(getCanonical(new SLGraph(perm).compress()));
//                    if (buffer.size()==10000) {
//                        for (SLGraph g2 : buffer)
//                            pw.write(g2.serialize() + "\n");
//                        buffer.clear();
//                        System.out.println("flushing ("+i+")");
//                    }
//                }
//            }
//            if (! buffer.isEmpty())
//                for (SLGraph g2 : buffer)
//                    pw.write(g2.serialize() + "\n");
//            pw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void main(String[] args) {
//        generateAll(getDir()+"COMP4", 4);
//    }
}
