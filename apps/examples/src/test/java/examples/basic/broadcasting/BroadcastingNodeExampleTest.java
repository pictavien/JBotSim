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

package examples.basic.broadcasting;

import examples.ExampleTestHelper;
import examples.Random;
import io.jbotsim.core.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class BroadcastingNodeExampleTest extends ExampleTestHelper {

    @Override
    protected Class getTestedClass() {
        return Main.class;
    }

    int nbInformed(Collection<Node> nodes) {
        int result = 0;
        for (Node n : nodes) {
            if (((BroadcastingNode) n).informed)
                result++;
        }
        return result;
    }

    @Test
    public void checkPropagation(TestInfo testInfo) throws Exception {
        prepareTopology();
        List<Node> nodes = testedTopology.getNodes();
        testedTopology.selectNode(nodes.get(0));
        int minDim = Math.min(Main.GRID_WIDTH, Main.GRID_HEIGHT);
        int maxDim = Math.max(Main.GRID_WIDTH, Main.GRID_HEIGHT);
        int nbRed = 0;

        for (int i = 0; i < minDim; i++) {
            nbRed += i + 1;
            assertEquals(nbRed, nbInformed(nodes), "at iteration " + i);
            iterate();
        }
        for (int i = minDim; i < maxDim; i++) {
            nbRed += minDim;
            assertEquals(nbRed, nbInformed(nodes), "at iteration " + i);
            iterate();
        }
        for (int i = 0; i < minDim; i++) {
            nbRed += minDim - i - 1;
            assertEquals(nbRed, nbInformed(nodes), "at iteration " + i);
            iterate();
        }
        assertEquals(nodes.size(), nbInformed(nodes));
    }

    private static int nodeIndex(int x, int y) {
        return Main.GRID_HEIGHT * y + x;
    }

    private static int UPPER_LEFT = nodeIndex(0, 0);
    private static int UPPER_RIGHT = nodeIndex(Main.GRID_WIDTH - 1, 0);
    private static int LOWER_LEFT = nodeIndex(0, Main.GRID_HEIGHT - 1);
    private static int LOWER_RIGHT = nodeIndex(Main.GRID_WIDTH - 1, Main.GRID_HEIGHT - 1);
    private static int CENTER = nodeIndex(Main.GRID_WIDTH / 2, Main.GRID_HEIGHT / 2);

    private static Stream<Arguments> provideSelectedNodeAndRounds() {
        int fileIndex = 0;
        int[] nodeIndices = new int[]{
                UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT, CENTER
        };
        int[] nbRounds = {
                0, 2, Main.GRID_WIDTH, Main.GRID_HEIGHT, Main.GRID_WIDTH + Main.GRID_HEIGHT
        };

        Stream<Arguments> result = Stream.empty();
        for(int nodeIdx : nodeIndices) {
            for(int nbR : nbRounds) {
                String expectedRes = String.format("broadcasting-nodes-state-%02d.xml", fileIndex++);
                result = Stream.concat(result, Stream.of(Arguments.of(nodeIdx, nbR, expectedRes)));
            }
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("provideSelectedNodeAndRounds")
    public void checkMovingIcons(int node,  int nbRounds, String expectedResult)
            throws Exception {
        boolean generateExpectedResult = true;
        iterateAndCheckState(Random.INITIAL_SEED, nbRounds, expectedResult, (t) -> {
            List<Node> nodes = t.getNodes();
            t.selectNode(nodes.get(node));
        }, generateExpectedResult);
    }
}
