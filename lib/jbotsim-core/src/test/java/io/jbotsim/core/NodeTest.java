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
package io.jbotsim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    public static final int DEFAULT_ICON_SIZE = 10;

    private Node createDefaultNode() {
        return new Node();
    }

    // region iconSize
    @Test
    void iconSize_defaultValue_ok() {
        Node node = createDefaultNode();
        assertEquals(DEFAULT_ICON_SIZE, node.getIconSize());
    }

    @Test
    void iconSize_setZero_ok() {
        testIconSizeGetterSetter(0);
    }

    @Test
    void iconSize_setNegative_ok() {
        testIconSizeGetterSetter(-15);
    }

    @Test
    void iconSize_setPositive_ok() {
        testIconSizeGetterSetter(15);
    }

    private void testIconSizeGetterSetter(int testedSize) {
        Node node = createDefaultNode();
        node.setIconSize(testedSize);
        assertEquals(testedSize, node.getIconSize());
    }

    // endregion


    // region equals

    @Test
    void equals_null_notEqual() {
        Node node = new Node();

        assertNotEquals(node, null);
    }

    // endregion

    // region compareTo

    @Test
    void compareTo_null_throwsNPE (){
        Node node = new Node();

        assertThrows(NullPointerException.class, () -> {node.compareTo(null);});
    }

    @Test
    void compareTo_greaterId_ok(){
        checkComparison(1, 2, -1);
    }
    @Test
    void compareTo_lesserId_ok(){
        checkComparison(2, 1, 1);
    }
    @Test
    void compareTo_sameId_ok(){
        checkComparison(1, 1, 0);
    }

    private void checkComparison(int id1, int id2, int expectedResult) {
        Node n1 = new Node();
        n1.setID(id1);
        Node n2 = new Node();
        n2.setID(id2);

        assertEquals(expectedResult, n1.compareTo(n2));
    }

    // endregion
}