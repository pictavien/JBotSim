package io.jbotsim.ui;

import io.jbotsim.core.Topology;
import io.jbotsim.graph.DegreeSequence;
import io.jbotsim.graph.types.Graph;
import io.jbotsim.graph.types.SLGraph;
import io.jbotsim.serialization.tikz.TikzTopologySerializer;
import io.jbotsim.topology.GraphRenderer;

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
 * The {@link JGraphSlider} displays a set of {@link Graph}s by providing a slider.
 * Arrows allow the user to navigate right or left amongst the graphs.
 */
public class JGraphSlider extends JFrame {
    Topology tp;
    JTopology jtp;
    List<Graph> graphs = new ArrayList<>();
    HashMap<Graph, Topology> topologies = new HashMap<>();
    Integer current = null;
    private final JLabel label;

    public JGraphSlider() {
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

    public void add(Graph graph) {
        addAll(Arrays.asList(graph));
    }

    public void addAll(Collection<Graph> graphs){
        this.graphs.addAll(graphs);
        if (current == null && this.graphs.size() > 0)
            setCurrentGraph(0);
    }

    private void setCurrentGraph(int index){
        current = index;
        Graph graph = graphs.get(current);
        if (topologies.keySet().contains(graph))
            jtp.setTopology(topologies.get(graph));
        else {
            Topology tp = GraphRenderer.renderGraph(graph);
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

    public void exportLatex(List<Graph> selected){
        // only handle SLGraph for now
        if(! (selected.get(0) instanceof SLGraph))
            return;

        if (selected.size() % 2 == 1)
            selected.add(selected.get(selected.size()-1));

        String s = "\\documentclass{article}\n"+
                "\\usepackage[utf8]{inputenc}\n"+
                "\\usepackage{tikz}\n"+
                "\\usepackage[margin=1in]{geometry}\n"+
                "\\begin{document}\n";
        for (int i=0; i<selected.size(); i+=2) {
            s += "\\noindent";
            SLGraph g1 = (SLGraph)selected.get(i);
            SLGraph g2 = (SLGraph)selected.get(i+1);
            Topology tp1 = GraphRenderer.renderGraph(g1);
            Topology tp2 = GraphRenderer.renderGraph(g2);
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
