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
import io.jbotsim.io.TopologySerializer;
import io.jbotsim.io.format.xml.XMLTopologySerializer;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        for (int i = 0; i < nbIterations; i++) {
            iterate();
        }
    }

    protected void iterateUntil(BooleanSupplier condition) {
        do {
            iterate();
        } while (condition.getAsBoolean());
    }

    protected Node pickRandomNode() {
        List<Node> nodes = testedTopology.getNodes();
        return nodes.get(prng.nextInt(nodes.size()));
    }


    public void prepareTopology()
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        prepareTopology(Random.INITIAL_SEED);
    }

    public void prepareTopology(long seed)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Random.setSeed(seed);
        testedTopology = buildTopology();
        testedTopology.start();
    }

    public void checkTopologyAgainstXMLFile(String xmlFileName) throws Exception {
        TopologySerializer serializer = new XMLTopologySerializer(true);
        String actual = serializer.exportToString(testedTopology);
        URL url = getClass().getResource(xmlFileName);
        String expected = testedTopology.getFileManager().read(url.getPath());
        assertEquals(expected, actual);
    }

    public void generateExpectedFile(String xmlFilename) {
        String filename = System.getProperty("user.home") + File.separatorChar +
                xmlFilename;
        System.out.println("generating expected file in " + filename);

        TopologySerializer serializer = new XMLTopologySerializer(true);
        String xmlContent = serializer.exportToString(testedTopology);
        testedTopology.getFileManager().write(filename, xmlContent);
    }

    public void iterateAndCheckState(long seed, int nbRounds, String expectedResultFile,
                                     boolean generateExpectedResult) throws Exception {
        iterateAndCheckState(seed, nbRounds, expectedResultFile,
                                (t) -> { }, generateExpectedResult);
    }

    public void iterateAndCheckState(long seed, int nbRounds, String expectedResultFile,
                                     Consumer<Topology> preIteration,
                                     boolean generateExpectedResult) throws Exception {
        prepareTopology(seed);
        preIteration.accept(testedTopology);
        iterate(nbRounds);
        if (generateExpectedResult)
            generateExpectedFile(expectedResultFile);

        checkTopologyAgainstXMLFile(expectedResultFile);
    }

    @AfterEach
    public void cleanUp() {
        testedTopology = null;
    }
}
