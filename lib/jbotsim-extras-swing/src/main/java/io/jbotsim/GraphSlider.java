package io.jbotsim;

import io.jbotsim.core.Topology;
import io.jbotsim.serialization.tikz.TikzTopologySerializer;
import io.jbotsim.topology.DegreeSequence;
import io.jbotsim.ui.JTopology;
import org.jgrapht.UndirectedGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * Created by acasteig on 27/12/16.
 */

/**
 * The {@link GraphSlider} displays a set of jgrapht {@link UndirectedGraph}s by providing a slider.
 * Arrows allow the user to navigate right or left amongst the graphs.
 */
public class GraphSlider extends JFrame {
    Topology tp;
    JTopology jtp;
    List<UndirectedGraph> graphs = new ArrayList<>();
    HashMap<UndirectedGraph, Topology> topologies = new HashMap<>();
    Integer current = null;
    private final JLabel label;

    public static void main(String[] args) {
        new GraphSlider();
    }
    public GraphSlider() {
        jtp = new JTopology(new Topology());
        add(jtp, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        JButton previous = new JButton("<");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPrevious();
            }
        });
        panel.add(previous);
        JButton reload = new JButton("R");
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });
        panel.add(reload);
        JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getNext();
            }
        });
        panel.add(next);
        label = new JLabel();
        panel.add(label);
        add(panel, BorderLayout.NORTH);
        setVisible(true);
        setSize(getPreferredSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void add(UndirectedGraph graph) {
        addAll(Arrays.asList(graph));
    }

    public void addAll(Collection<UndirectedGraph> graphs){
        this.graphs.addAll(graphs);
        if (current == null && this.graphs.size() > 0)
            setCurrentGraph(0);
    }

    private void setCurrentGraph(int index){
        current = index;
        UndirectedGraph graph = graphs.get(current);
        if (topologies.keySet().contains(graph))
            jtp.setTopology(topologies.get(graph));
        else {
            Topology tp = Renderer.renderGraph(graph);
            topologies.put(graph, tp);
            jtp.setTopology(tp);
        }
        label.setText(current.toString() + "/" + graphs.size());
    }

    private void getPrevious() {
        setCurrentGraph((current - 1) % graphs.size());
    }

    private void reload() {
        topologies.remove(graphs.get(current));
        setCurrentGraph(current);
    }

    private void getNext(){
        setCurrentGraph((current + 1) % graphs.size());
    }

    public void exportLatex(List<UndirectedGraph> selected){
        if (selected.size() % 2 == 1)
            selected.add(selected.get(selected.size()-1));

        String s = "\\documentclass{article}\n"+
                "\\usepackage[utf8]{inputenc}\n"+
                "\\usepackage{tikz}\n"+
                "\\usepackage[margin=1in]{geometry}\n"+
                "\\begin{document}\n";
        for (int i=0; i<selected.size(); i+=2) {
            s += "\\noindent";
            UndirectedGraph g1 = selected.get(i);
            UndirectedGraph g2 = selected.get(i+1);
            Topology tp1 = Renderer.renderGraph(g1);
            Topology tp2 = Renderer.renderGraph(g2);
            s += "\\hspace{1cm}" + i + " : " + new DegreeSequence(g1)+"\\hspace{4cm}";
            s += ((i+1) + " : " + new DegreeSequence(g2) +"\n\n");
            s += new TikzTopologySerializer().exportTopology(tp1).replaceAll("e=1", "e=.7");
            s += "\\hspace{20pt}"+"\n";
            s += new TikzTopologySerializer().exportTopology(tp2).replaceAll("e=1", "e=.7");
            s += "\\bigskip\n\n";
        }
        s += "\\end{document}";

        // FILE EXPORT
        try {
            PrintWriter out = new PrintWriter("/tmp/graphexport.tex");
            out.println(s);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ProcessBuilder builder = new ProcessBuilder("pdflatex", "/tmp/graphexport.tex");
            builder.directory(new File("/tmp/"));
            builder.start();
            builder = new ProcessBuilder("evince", "/tmp/graphexport.pdf");
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
