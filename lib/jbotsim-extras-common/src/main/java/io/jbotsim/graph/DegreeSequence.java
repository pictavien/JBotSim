package io.jbotsim.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by acasteig on 23/12/16.
 */
public class DegreeSequence {
    List<Integer> sequence = new ArrayList<>();

    public DegreeSequence(List<Integer> sequence) {
        this.sequence.addAll(sequence);
    }

//    public DegreeSequence(UndirectedGraph graph) {
//        for (Object i : graph.vertexSet())
//            sequence.add(graph.degreeOf(i));
//        Collections.sort(sequence, Collections.reverseOrder());
//    }

    private static int sum(List<Integer> seq, int from, int to){
        int sum = 0;
        for (int i=from; i <= to; i++)
            sum += seq.get(i);
        return sum;
    }
    public boolean isGraphic(){
        int n = sequence.size();
        List<Integer> seq = new ArrayList<>(sequence);
        seq.add(0, 0); // to work with indices from 1 to n (as in the formula)
        if (sum(seq, 1, n) % 2 != 0)
            return false;
        for (int r = 1; r <= n-1; r++){
            int left = sum(seq, 1, r);
            int right = r*(r-1);
            for (int i = r+1; i <= n; i++)
                right += Math.min(r, seq.get(i));
            if (left > right)
                return false;
        }
        return true;
    }

    /**
     * Returns the multiplicities of the degrees.
     * For example, a sequence of degrees [1, 1, 2, 2, 2, 3, 3, 3, 3]
     * has multiplicities [2,3,4].
     * A sequence [2, 2, 2, 2, 2, 2] has multiplicities [6].
     */
    public HashMap<Integer, Integer> getMultiplicities(){
        HashMap<Integer, Integer> multiplicities = new HashMap<>();
        int degree = sequence.get(0);
        int multiplicity = 1;
        for (int i = 1; i < sequence.size(); i++){
            if (sequence.get(i)!=degree) {
                multiplicities.put(degree, multiplicity);
                degree = sequence.get(i);
                multiplicity=0;
            }
            multiplicity++;
        }
        multiplicities.put(degree, multiplicity);
        return multiplicities;
    }

    /**
     * [2, 2, 3, 3, 4, 4] divided by 2 gives [2, 3, 4]
     */
    public DegreeSequence divideBy(Integer f){
        List<Integer> nseq = new ArrayList<>();
        for (int i=0; i<sequence.size(); i+=f)
            nseq.add(sequence.get(i));
        return new DegreeSequence(nseq);
    }

//    public Set<Integer> getPossibleFactors() {
//        Set<Integer> factors = Primality.getCommonPrimeFactors(getMultiplicities().values());
//        Set<Integer> result = new HashSet<>();
//
//        for (Integer f : factors){
//            if (divideBy(f).isGraphic())
//                result.add(f);
//        }
//        return result;
//    }

    @Override
    public String toString() {
        return sequence.toString();
    }

//    public String debug(){
//        String s = toString() + "\n";
//        Set<Integer> possibleFactors = getPossibleFactors();
//        s += "possible factors : " + possibleFactors + "\n";
//        for (Integer f : possibleFactors) {
//            DegreeSequence seq = divideBy(f);
//            s += "divided by " + f + " -> " + seq.toString() + "\n";
//            s += "isGraphic ? : " + seq.isGraphic() + "\n";
//        }
//        return s;
//    }

//    public static void main(String[] args) {
//        List<Integer> test = new ArrayList<>();
//        test.add(0);
//        test.add(2);
//        test.add(2);
//        DegreeSequence sequence = new DegreeSequence(test);
//        System.out.println(sequence);
//        System.out.println(sequence.isGraphic());
//    }
}
