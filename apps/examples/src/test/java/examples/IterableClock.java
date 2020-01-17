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

import io.jbotsim.core.Clock;
import io.jbotsim.core.ClockManager;

public class IterableClock extends Clock {
    private int tick = -1;

    public IterableClock(ClockManager manager) {
        super(manager);
    }

    public void tick() {
        manager.onClock();
        tick++;
    }

    @Override
    public int getTimeUnit() {
        return 1;
    }

    @Override
    public void setTimeUnit(int timeUnit) {
        useless_method("setTimeUnit");
    }

    @Override
    public boolean isRunning() {
        return tick >= 0;
    }

    @Override
    public void start() {
        tick = 0;
    }

    @Override
    public void pause() {
        useless_method("pause");
    }

    @Override
    public void resume() {
        useless_method("resume");
    }

    private void useless_method(String method) {
        // System.err.println("examples.IteratedClock: '"+method+"' is irrelevant.");
    }
}
