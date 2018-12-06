package io.jbotsim.graph.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by acasteig on 04/06/17.
 */
public class Helper {
    static Map<String, Object> global = new HashMap<>();
//
//    public static String getDir() {
//        return "/home/acasteig/lworkspace/data/";
//    }
//
//    public static int getNbGraphs(int n){
//        if (!global.containsKey("nbGraphs"))
//            global.put("nbGraphs", Arrays.asList(0,1,1,1,30,30240,1816214400));
//        return ((List<Integer>) global.get("nbGraphs")).get(n);
//    }
//
//    public static int getNbCompressedGraphs(int n){
//        if (!global.containsKey("nbCompressedGraphs"))
//            global.put("nbCompressedGraphs", Arrays.asList(0,1,1,1,21,12509,350920904));
//        return ((List<Integer>) global.get("nbCompressedGraphs")).get(n);
//    }
//
//    public static List<Integer> getNodeList(int n){
//        //if (!global.containsKey("nodeList-" + n)) {
//            List<Integer> nodeList = new ArrayList<>();
//            for (int i=0; i<n; i++)
//                nodeList.add(i);
//        //    global.put("nodeList-" + n, nodeList);
//        //}
//        //return ((List<Integer>) global.get("nodeList-" + n));
//        return nodeList;
//    }
//
    public static List<Integer> getEdgeList(int n){
        if (!global.containsKey("edgeList")) {
            List<Integer> edgeList = new ArrayList<>();
            for (int i=0; i<n*(n-1)/2; i++)
                edgeList.add(i);
            global.put("edgeList", edgeList);
        }
        return ((List<Integer>) global.get("edgeList"));
    }

//    // TODO Check if 0/1 trick correct in all usage
//    public static List<List<Integer>> getNodePerms(int n) {
//        if (!global.containsKey("nodePerms")) {
//            List<Integer> nodes = new ArrayList<>(getNodeList(n));
//            nodes.remove(0);
//            nodes.remove(0);
//            List<List<Integer>> nodesPerms = new LinkedList<>();
//            Iterator<List<Integer>> iter = new PermutationsIterator<>(nodes);
//            while (iter.hasNext()) {
//                List<Integer> nodePerm = new ArrayList<>(iter.next());
//                List<Integer> nodePerm2 = new ArrayList<>(nodePerm);
//                nodePerm.add(0, 0);
//                nodePerm.add(1, 1);
//                nodePerm2.add(0, 1);
//                nodePerm2.add(1, 0);
//                nodesPerms.add(nodePerm);
//                nodesPerms.add(nodePerm2);
//            }
//            global.put("nodePerms", nodesPerms);
//        }
//        return (List<List<Integer>>) global.get("nodePerms");
//    }

    public static int indexOf(int n, int i, int j){ // TODO OPTIMIZE
        int min = Math.min(i, j);
        int max = Math.max(i, j);
        int k = 0;
        for (int a = 0; a <= min; a++){
            for (int b = a+1; b < n; b++){
                if (a == min && b == max)
                    break;
                k++;
            }
        }
        return k;
    }
//
//    public static int compare(List<Integer> list1, List<Integer> list2){
//        if (list1.size() != list2.size())
//            return list1.size() - list2.size();
//        for (int i=0; i<list1.size(); i++){
//            int val1 = list1.get(i);
//            int val2 = list2.get(i);
//            if (val1 != val2)
//                return (val1 - val2);
//        }
//        return 0;
//    }
//
//    public static void serialize(Serializable o, String filename) {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(filename);
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(o);
//            out.close();
//            fileOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static Object unserialize(String filename){
//        Object object = null;
//        try {
//            FileInputStream fileIn = new FileInputStream(filename);
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            object = in.readObject();
//            in.close();
//            fileIn.close();
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return object;
//    }
}
