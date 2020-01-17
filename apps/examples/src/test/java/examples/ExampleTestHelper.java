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
package examples;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class ExampleTestHelper {
    protected Topology testedTopology;
    protected Random prng = new Random();

    protected abstract Class getTestedClass();

    public Topology buildTopology()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getTestedClass().getMethod("buildTopology");
        Topology tp = (Topology) method.invoke(null);
        tp.setClockModel(IterableClock.class);

        return tp;
    }

    protected void iterate() {
        IterableClock clock = (IterableClock) testedTopology.getClock();
        clock.tick();
    }

    protected void iterate(int nbIterations) {
        for(int i = 0; i < nbIterations; i++) {
            iterate();
        }
    }

    protected void iterateUntil(BooleanSupplier condition) {
        do {
            iterate();
        } while(condition.getAsBoolean());
    }

    protected Node pickRandomNode() {
        List<Node> nodes = testedTopology.getNodes();
        return nodes.get(prng.nextInt(nodes.size()));
    }


    @BeforeEach
    public void setUp() {
        try {
            testedTopology = buildTopology();
            testedTopology.start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @AfterEach
    public void cleanUp() {
        testedTopology = null;
    }
}
