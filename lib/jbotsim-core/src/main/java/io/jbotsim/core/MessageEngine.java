/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.MessageListener;

import java.util.ArrayList;

/**
 * <p>The {@link MessageEngine} is responsible for regularly (it is a {@link ClockListener}) transmitting available
 * {@link Message}s from any sender {@link Node} of the {@link Topology} to their destination.</p>
 */
public class MessageEngine implements ClockListener {
    protected Topology topology;
    protected boolean debug = false;

    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    public void setSpeed(int speed) {
        topology.removeClockListener(this);
        topology.addClockListener(this, speed);
    }

    @Override
    public void onClock() {
        clearMailboxes();
        processMessages(collectMessages());
    }

    private void clearMailboxes() {
        for (Node node : topology.getNodes())
            node.getMailbox().clear();
    }

    protected ArrayList<Message> collectMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        for (Node n : topology.getNodes()) {
            for (Message m : n.sendQueue) {
                if (m.destination == null)
                    for (Node ng : m.sender.getOutNeighbors())
                        messages.add(m.withDestination(ng));
                else
                    messages.add(m);
            }
            n.sendQueue.clear();
        }
        return messages;
    }

    protected void processMessages(ArrayList<Message> messages) {
        for (Message m : messages)
            if (m.sender.getOutLinkTo(m.destination) != null)
                deliverMessage(m);
            else if (m.retryMode)
                m.sender.sendQueue.add(m);
    }

    protected void deliverMessage(Message m) {
        m.destination.getMailbox().add(m);
        m.destination.onMessage(m);
        for (MessageListener ml : topology.messageListeners)
            ml.onMessage(m);
        if (debug)
            System.err.println(topology.getTime() + ": " + m);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
