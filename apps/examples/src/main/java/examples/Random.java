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

public class Random {
    public static long INITIAL_SEED = 3436575961636972l;

    private static java.util.Random prng = new java.util.Random();

    public static void setSeed(long seed) {
        prng.setSeed(seed);
    }

    public static double random() {
        return prng.nextDouble();
    }

    public static double nextDouble() {
        return prng.nextDouble();
    }

    public static int nextInt(int bound) {
        return prng.nextInt(bound);
    }
}
