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
import io.jbotsim.core.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

public class BroadcastingNodeExampleTest extends ExampleTestHelper<Main> {
    @Override
    protected Class<Main> getTestedClass() {
        return Main.class;
    }

    int nbInformed(Collection<Node> nodes) {
        int result = 0;
        for(Node n : nodes) {
            if (((BroadcastingNode)n).informed)
                result++;
        }
        return result;
    }

    @Test
    public void checkPropagation() {
        List<Node> nodes = testedTopology.getNodes();
        testedTopology.selectNode(nodes.get(0));
        int minDim = Math.min(Main.GRID_WIDTH, Main.GRID_HEIGHT);
        int maxDim = Math.max(Main.GRID_WIDTH, Main.GRID_HEIGHT);
        int nbRed = 0;

        for(int i = 0; i < minDim; i++) {
            nbRed += i + 1;
            assertEquals(nbRed, nbInformed(nodes), "at iteration"+i);
            iterate();
        }
        for(int i = minDim; i < maxDim; i++) {
            nbRed += minDim;
            assertEquals(nbRed, nbInformed(nodes), "at "+i);
            iterate();
        }
        for(int i = 0; i < minDim; i++) {
            nbRed += minDim - i - 1;
            assertEquals(nbRed, nbInformed(nodes), "at "+i);
            iterate();
        }
        assertEquals(nodes.size(), nbInformed(nodes));
    }
}
