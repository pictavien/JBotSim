package io.jbotsim.ui;

import io.jbotsim.graph.types.SLGraph;

public class JGraphSliderTest {

    public static void main(String[] args) {

        JGraphSlider slider = new JGraphSlider();

        for (int i = 1; i<20; i++)
            slider.add(new SLGraph(i));


    }
}
