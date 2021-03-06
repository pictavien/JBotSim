/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.jbotsim.gen.dynamic.trace;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.xml.XMLTraceParser;
import io.jbotsim.ui.JViewer;

public class TracePlayerTest {
    public static void main(String[] args) {
        try {
            Topology topology = new Topology();
            new JViewer(topology);
            TracePlayer tp = new TracePlayer(topology, new XMLTraceParser(topology.getFileManager(), true));
            tp.loadAndStart(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
