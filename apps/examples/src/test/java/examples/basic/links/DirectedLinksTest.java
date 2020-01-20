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

package examples.basic.links;

import examples.ExampleTestHelper;
import examples.Random;
import examples.basic.icons.MainIcons;
import examples.basic.mobilebroadcast.MobileBroadcastNode;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DirectedLinksTest extends ExampleTestHelper {
    @Override
    protected Class getTestedClass() {
        return DirectedLinksMain.class;
    }

    private static Stream<Arguments> provideSeedsAndRounds() {
        return Stream.of(
                Arguments.of(275662412l, 6461, "directed-links-state-00.xml"),
                Arguments.of(3066721431l, 4632, "directed-links-state-01.xml"),
                Arguments.of(2315217891l, 3792, "directed-links-state-02.xml"),
                Arguments.of(314986383l, 4071, "directed-links-state-03.xml"),
                Arguments.of(508225197l, 6615, "directed-links-state-04.xml"),
                Arguments.of(203698725l, 6234, "directed-links-state-05.xml"),
                Arguments.of(1317213446l, 5230, "directed-links-state-06.xml"),
                Arguments.of(2296516590l, 4025, "directed-links-state-07.xml"),
                Arguments.of(159154406l, 1674, "directed-links-state-08.xml"),
                Arguments.of(881115604l, 677, "directed-links-state-09.xml"),
                Arguments.of(190005456l, 620, "directed-links-state-10.xml"),
                Arguments.of(613910246l, 487, "directed-links-state-11.xml"),
                Arguments.of(202041725l, 6106, "directed-links-state-12.xml"),
                Arguments.of(714627694l, 357, "directed-links-state-13.xml")
        );
    }

    @ParameterizedTest
    @MethodSource("provideSeedsAndRounds")
    public void checkStates(long seed, int nbRounds, String expectedResult)
            throws Exception {
        boolean generateExpectedResult = false;
        iterateAndCheckState(seed, nbRounds, expectedResult, generateExpectedResult);
    }
}
