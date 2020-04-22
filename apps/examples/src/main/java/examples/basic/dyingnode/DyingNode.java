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

package examples.basic.dyingnode;

import examples.Random;
import examples.basic.moving.MovingNode;

public class DyingNode extends MovingNode {
    Random rnd = new Random();

    @Override
    public void onClock() {
        super.onClock();

        dieRandomly(0.999);
    }

    private void dieRandomly(double survivingChance) {
        if(rnd.nextDouble() > survivingChance)
            this.die();
    }
}
