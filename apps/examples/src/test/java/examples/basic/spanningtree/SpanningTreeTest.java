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

package examples.basic.spanningtree;

import examples.ExampleTestHelper;
import examples.Random;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SpanningTreeTest extends ExampleTestHelper {
    @Override
    protected Class getTestedClass() {
        return Main.class;
    }

    private static Stream<Arguments> provideNodesAndRounds() {
        Stream<Arguments> params = Stream.empty();
        int[] nbRounds = new int[]{
                0, 1, 2, Main.GRID_WIDTH, Main.GRID_HEIGHT,
                Main.GRID_WIDTH + Main.GRID_HEIGHT
        };

        for (int nodeIdx = 0; nodeIdx < Main.GRID_HEIGHT * Main.GRID_WIDTH;
             nodeIdx++) {
            for (int nbR : nbRounds) {
                params = Stream.concat(params, Stream.of(Arguments.of(nodeIdx, nbR)));
            }
        }

        return params;
    }

    int countVisitedLinks(Topology tp) {
        int result = 0;
        for (Link l : tp.getLinks()) {
            if (l.getWidth() > 1)
                result++;
        }

        return result;
    }

    int countVisitedNodes(Topology tp) {
        int result = 0;
        for (Node n : tp.getNodes()) {
            SpanningTreeNode treeNode = (SpanningTreeNode) n;
            if (treeNode.parent != null)
                result++;
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("provideNodesAndRounds")
    public void checkStates(int nodeIndex, int nbRounds)
            throws Exception {
        prepareTopology();
        testedTopology.selectNode(testedTopology.getNodes().get(nodeIndex));
        iterate(nbRounds);
        assertEquals(countVisitedLinks(testedTopology) + 1,
                countVisitedNodes(testedTopology));
    }

}