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

import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.MovementListener;

import java.util.*;

import static io.jbotsim.core.Node.PropString.*;

/**
 * <p>The {@link Node} object is one of the main component of JBotSim, since it encodes an element of the graph/network.
 * It is used by the {@link Topology} along with the {@link Link} object to represent the graph.</p>
 *
 * <p>Any behavior modification should be implemented by subclassing this class.</p>
 */
public class Node extends Properties implements ClockListener, Comparable<Node> {
    public static final Color DEFAULT_COLOR = null;
    public static final int DEFAULT_ICON_SIZE = 10;
    public static final double DEFAULT_DIRECTION =  -Math.PI / 2;
    List<Message> mailBox = new ArrayList<>();
    List<Message> sendQueue = new ArrayList<>();
    HashMap<Node, Link> outLinks = new HashMap<>();
    Point coords = new Point(0, 0, 0);
    double direction = DEFAULT_DIRECTION;
    Double communicationRange = null;
    Double sensingRange = null;
    List<Node> sensedNodes = new ArrayList<>();
    boolean isWirelessEnabled = true;
    Topology topo;
    Color color = null;
    Object label = null;
    Integer ID = -1;
    int iconSize = DEFAULT_ICON_SIZE;
    private boolean die = false;

    enum PropString {
        COLOR("color"),
        ICON("icon"),
        LABEL("label"),
        SIZE("size");

        PropString(String str) { value = str; };

        public String toString() {
            return value;
        }

        private final String value;
    };

    /**
     * Returns the identifier of this node.
     *
     * @return The identifier as an integer.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the identifier of this node. {@link Node}s have an identifier by default,
     * which is the smallest available integer.
     * @param ID the new identifier.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Returns the parent {@link Topology} of this node, if any.
     *
     * @return The parent {@link Topology}, or <code>null</code> if the node is orphan.
     */
    public Topology getTopology() {
        return topo;
    }

    /**
     * Called when this node is selected (e.g. middle click in the UI)
     */
    public void onSelection() {
    }

    /**
     * Override this method to re-initialise your node (e.g. your variables).
     * This method is also called when a node is added to the topology
     * (or when the topology starts, if not yet started).
     */
    public void onStart() {
    }

    /**
     * This method is called just before
     * the node is removed from the topology.
     */
    public void onStop() {
    }

    /**
     * Override this method to perform some action just before clock pulse.
     */
    public void onPreClock() {
    }

    /**
     * Override this method to perform some action upon clock pulse.
     */
    public void onClock() {
    }

    /**
     * Override this method to perform some action just after clock pulse.
     */
    public void onPostClock() {
    }

    /**
     * Override this method to perform some action when the node moves.
     */
    public void onMovement() {
    }

    /**
     * Called when this node receives a {@link Message}.
     * @param message the received {@link Message}.
     */
    public void onMessage(Message message) {
    }

    /**
     * Called when an adjacent undirected link is added.
     * @param link the added {@link Link}.
     */
    public void onLinkAdded(Link link) {
    }

    /**
     * Called when an adjacent undirected link is removed.
     * @param link the removed {@link Link}.
     */
    public void onLinkRemoved(Link link) {
    }

    /**
     * Called when an adjacent directed link is added.
     * @param link the added {@link Link}.
     */
    public void onDirectedLinkAdded(Link link) {
    }

    /**
     * Called when an adjacent directed link is removed.
     * @param link the removed {@link Link}.
     */
    public void onDirectedLinkRemoved(Link link) {
    }

    /**
     * Called when another node is sensed for the first time.
     * @param node the {@link Node} that has been sensed.
     */
    public void onSensingIn(Node node) {
    }

    /**
     * Called when a sensed node is no more sensed.
     * @param node the {@link Node} that is not sensed anymore.
     */
    public void onSensingOut(Node node) {
    }

    /**
     * Request the {@link Node} to die by the end of the round.
     */
    public void die() {
        die = true;
    }

    /**
     * Returns the dying status of the {@link Node}.
     * @return {@code true} if the {@link Node} is dying or already dead; returns {@code false} otherwise.
     */
    public boolean isDying() {
        return die;
    }

    /**
     * Returns the x-coordinate of this node.
     * @return the x-coordinate, as a double.
     */
    public double getX() {
        return coords.getX();
    }

    /**
     * Returns the y-coordinate of this node.
     * @return the y-coordinate, as a double.
     */
    public double getY() {
        return coords.getY();
    }

    /**
     * Returns the z-coordinate of this node.
     * @return the z-coordinate, as a double.
     */
    public double getZ() {
        return coords.getZ();
    }

    /**
     * Returns the color of this node.
     * @return the {@link Node}'s {@link Color}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of this node.
     * @param color the {@link Node}'s new {@link Color}.
     */
    public void setColor(Color color) {
        this.color = color;
        setProperty(COLOR.toString(), color); // Used for property notification
    }

    /**
     * Sets the icon of this node. The argument must be an absolute path to
     * either a file in the file system, or a resource in the application.
     * Examples:
     * <pre>
     * {@code
     *     node.setIcon("/filesystem/path/to/image");
     *     node.setIcon("/package/path/to/image");
     * }
     * </pre>
     * @param fileName a {@link String} leading to the icon.
     */
    public void setIcon(String fileName) {
        setProperty(ICON.toString(), fileName);
    }

    /**
     * Returns the icon of the this node.
     * @return a {@link String} leading to the icon.
     */
    public String getIcon() {
        return (String) getProperty(ICON.toString());
    }

    /**
     * Returns the {@link Node}'s icon's desired size.
     * @return the desired size of this {@link Node}'s icon, as an integer.
     */
    public int getIconSize() {
        return iconSize;
    }

    /**
     * Sets the {@link Node}'s icon's desired size.
     * @param iconSize the new desired size of this {@link Node}'s icon, as an integer.
     */
    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        setProperty(SIZE.toString(), iconSize); // used for property notification
    }

    /**
     * Returns the label of this node.
     * @return the label of the node, as an {@link Object}.
     */
    public Object getLabel() {
        return label;
    }

    /**
     * Sets the label of this node. Default GUI shows it as tooltip
     * when the mouse cursor is held some time over the node.
     * @param label the {@link Object} representing the new label of the node.
     */
    public void setLabel(Object label) {
        this.label = label;
        setProperty(LABEL.toString(), label); // Used for property notification
    }

    /**
     * Returns the communication range of this node (as a radius).
     * @return the current communication range.
     */
    public double getCommunicationRange() {
        return communicationRange;
    }

    /**
     * Activates the wireless capabilities of this node and sets
     * its communication range to the specified radius. This
     * determines the distance up to which this node can <i>send</i> messages
     * to other nodes.
     * @param range the new communication range, as a double.
     */
    public void setCommunicationRange(double range) {
        communicationRange = range;
        if (topo != null)
            topo.touch(this);
    }

    /**
     * Indicates whether this node has wireless capabilities enabled.
     * @return <code>true</code> if the wireless capabilities are enabled,
     *         <code>false</code> otherwise.
     */
    public boolean isWirelessEnabled() {
        return isWirelessEnabled;
    }

    /**
     * Enables this node's wireless capabilities.
     */
    public void enableWireless() {
        setWirelessStatus(true);
    }

    /**
     * Disables this node's wireless capabilities.
     */
    public void disableWireless() {
        setWirelessStatus(false);
    }

    /**
     * Set wireless capabilities status
     * @param enabled the new wireless status: <code>true</code> to enable,
     *         <code>false</code> otherwise.
     */
    public void setWirelessStatus(boolean enabled) {
        if (enabled == isWirelessEnabled)
            return;
        isWirelessEnabled = enabled;
        if (topo != null)
            topo.touch(this);
    }

    /**
     * Returns the sensing range of this node (as a radius).
     * @return the current sensing range.
     */
    public double getSensingRange() {
        return sensingRange;
    }

    /**
     * Sets the sensing range of this node to the specified radius.
     * @param range the new sensing range, as a double.
     */
    public void setSensingRange(double range) {
        sensingRange = range;
        notifyNodeMoved(); // for GUI refresh FIXME
    }

    /**
     * Returns the location of this node.
     * @return the current location of this node, as a {@link Point}.
     */
    public Point getLocation() {
        return new Point(coords);
    }

    /**
     * Changes this node's location to the specified coordinates.
     *
     * @param x The abscissa of the new location.
     * @param y The ordinate of the new location.
     */
    public void setLocation(double x, double y) {
        coords = new Point(x, y, 0);
        if (topo != null)
            topo.touch(this);
        notifyNodeMoved();
    }

    /**
     * Changes this node's location to the specified coordinates.
     *
     * @param x The abscissa of the new location.
     * @param y The ordinate of the new location.
     * @param z The ordinate of the new location.
     */
    public void setLocation(double x, double y, double z) {
        coords = new Point(x, y, z);
        if (topo != null)
            topo.touch(this);
        notifyNodeMoved();
    }

    /**
     * Changes this node's location to the specified 2D point.
     *
     * @param loc The new location point.
     */
/*    public void setLocation(Point loc) {
        setLocation(loc.getX(), loc.getY());
    }*/

    /**
     * Changes this node's location to the specified 2D point.
     *
     * @param loc The new location point.
     */
    public void setLocation(Point loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Changes this node's location modulo the iconSize of topology.
     */
    public void wrapLocation() {
        setLocation((coords.getX() + topo.getWidth()) % topo.getWidth(), (coords.getY() + topo.getHeight()) % topo.getHeight());
    }

    /**
     * Translates the location of this node by the specified coordinates.
     *
     * @param dx The abscissa component.
     * @param dy The ordinate component.
     */
    public void translate(double dx, double dy) {
        setLocation(coords.getX() + dx, coords.getY() + dy);
    }

    /**
     * Translates the location of this node by the specified coordinates.
     *
     * @param dx The abscissa component.
     * @param dy The ordinate component.
     * @param dz The applicate component.
     */
    public void translate(double dx, double dy, double dz) {
        setLocation(coords.getX() + dx, coords.getY() + dy, coords.getZ() + dz);
    }

    /**
     * Returns the current time (current round number).
     * @return the current time.
     */
    public int getTime() {
        return topo.getTime();
    }

    /**
     * Returns the current direction angle of this node (in radians).
     * @return the current direction.
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Sets the direction angle of this node (in radians).
     *
     * @param angle The angle in radians.
     */
    public void setDirection(double angle) {
        direction = angle;
        notifyNodeMoved();
    }

    /**
     * Sets the direction angle of this node using the specified reference
     * point. Only the resulting angle matters (not the particular location of
     * the reference point).
     *
     * @param p The reference {@link Point}.
     */
    public void setDirection(Point p) {
        setDirection(Math.atan2(p.getY() - coords.getY(), (p.getX() - coords.getX())));
    }

    /**
     * Sets the direction angle of this node using the specified reference
     * point. Only the resulting angle matters (not the particular location of
     * the reference point).
     *
     * @param p The reference {@link Point}.
     * @param opposite <code>true</code> if the new direction should be the opposite of the reference point.
     */
    public void setDirection(Point p, boolean opposite) {
        Point p2 = (Point) p.clone();
        if (opposite)
            p2.setLocation(2 * getX() - p.getX(), 2 * getY() - p.getY());
        setDirection(p2);
    }

    /**
     * Translates the location of this node by one unit towards
     * the node's current direction.
     */
    public void move() {
        move(1);
    }

    /**
     * Translates the location of this node by the specified distance towards
     * the node's current direction.
     * @param distance the distance by which the {@link Node} should be moved.
     */
    public void move(double distance) {
        translate(Math.cos(direction) * distance, Math.sin(direction) * distance);
    }

    /**
     * Returns the directed link whose destination is this node and sender is
     * the specified node, if any such link exists.
     *
     * @param n The sender node.
     * @return The requested link, or <code>null</code> if no such link is found.
     */
    public Link getInLinkFrom(Node n) {
        return topo.getLink(n, this, Link.Orientation.DIRECTED);
    }

    /**
     * Returns the directed link whose sender is this node and destination is
     * the specified node, if any such link exists.
     *
     * @param n The destination node.
     * @return The requested link, or <code>null</code> if no such link is found.
     */
    public Link getOutLinkTo(Node n) {
        return outLinks.get(n);
    }

    /**
     * Returns the undirected link whose endpoints are this node and the
     * specified node, if any such link exists.
     *
     * @param n The node at the opposite endpoint.
     * @return The requested link, or <code>null</code> if no such link is found.
     */
    public Link getCommonLinkWith(Node n) {
        return topo.getLink(this, n, Link.Orientation.UNDIRECTED);
    }

    /**
     * Returns a list containing all links for which this node is the
     * destination. The returned list can be subsequently modified without
     * effect on the topology.
     * @return the {@link List} of inbound {@link Link}s.
     */
    public List<Link> getInLinks() {
        return topo.getLinks(Link.Orientation.DIRECTED, this, 2);
    }

    /**
     * Returns a list containing all links for which this node is the
     * sender. The returned list can be subsequently modified without effect
     * on the topology.
     * @return the {@link List} of outbound {@link Link}s.
     */
    public List<Link> getOutLinks() {
        return new ArrayList<Link>(outLinks.values());
    }

    /**
     * Returns a list containing all links adjacent to this node. The type of
     * links is determined according to hasDirectedLinks() method of the topology.
     * The returned list can be subsequently modified without effect on the
     * topology.
     * @return the {@link List} of {@link Link}s
     */
    public List<Link> getLinks() {
        return getLinks(topo.getOrientation());
    }

    /**
     * Returns a list containing all adjacent links with the specified
     * {@code orientation}.
     *
     * @param orientation the expected orientation of requested links. The
     *                    returned list can be subsequently modified without
     *                    effect on the topology.
     * @return the {@link List} of {@link Link}s
     */
    public List<Link> getLinks(Link.Orientation orientation) {
        return topo.getLinks(orientation, this, 0);
    }

    /**
     * Returns a list containing all adjacent links of the specified type.
     *
     * @param directed <code>true</code> for directed, <code>false</code> for
     *                 undirected. The returned list can be subsequently
     *                 modified without effect on the topology.
     * @return the {@link List} of {@link Link}s
     * @deprecated use {@link #getLinks(Link.Orientation)}
     */
    @Deprecated
    public List<Link> getLinks(boolean directed) {
        Link.Orientation o = (directed
                ? Link.Orientation.DIRECTED
                : Link.Orientation.UNDIRECTED);
        return topo.getLinks(o, this, 0);
    }

    /**
     * <p>Indicates whether the provided node is the other endpoint of one of the node's in-coming links.</p>
     *
     * @param node the {@link Node} to be tested.
     * @return <tt>true</tt> if it does, <tt>false</tt> if it does not.
     */
    public boolean hasInNeighbor(Node node) {
        if (node == null)
            return false;

        return node.hasOutNeighbor(this);
    }

    /**
     * Returns a list containing every node serving as source for an adjacent
     * directed link. The returned list can be subsequently modified
     * without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links come from a same neighbor.
     */
    public List<Node> getInNeighbors() {
        List<Node> neighbors = new ArrayList<>();
        for (Link l : getInLinks())
            neighbors.add(l.source);
        return neighbors;
    }

    /**
     * <p>Indicates whether the provided node is the other endpoint of one of the node's out-going links.</p>
     *
     * @param node the {@link Node} to be tested.
     * @return <tt>true</tt> if it does, <tt>false</tt> if it does not.
     */
    public boolean hasOutNeighbor(Node node) {
        return outLinks.containsKey(node);
    }

    /**
     * Returns a list containing every node serving as destination for an
     * adjacent directed link. The returned list can be subsequently
     * modified without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links go towards a same neighbor.
     */
    public List<Node> getOutNeighbors() {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (Link l : outLinks.values())
            neighbors.add(l.destination);
        return neighbors;
    }

    /**
     * Returns a list containing every node located within the sensing range
     * The returned list can be modified without side effect.
     *
     * @return A list containing all nodes within sensing range
     */
    public List<Node> getSensedNodes() {
        ArrayList<Node> sensedNodes = new ArrayList<>();
        for (Node n : topo.getNodes())
            if (distance(n) < sensingRange && n != this)
                sensedNodes.add(n);
        return sensedNodes;
    }

    /**
     * Indicates whether this node has at least one neighbor (undirected)
     *
     * @return <code>true</code> if it does, <code>false</code> if it does not.
     */
    public boolean hasNeighbors() {
        return getLinks().size() > 0;
    }

    /**
     * <p>Indicates whether the provided node is to be considered as neighbor of the current node, depending on the
     * {@link Topology}'s orientation.</p>
     *
     * @param node the {@link Node} to be tested.
     * @return <tt>true</tt> if it does, <tt>false</tt> if it does not.
     */
    public boolean hasNeighbor(Node node) {
        if (getTopology().isDirected())
            return hasOutNeighbor(node);
        else
            return hasInNeighbor(node) && hasOutNeighbor(node);
    }

    /**
     * Returns a list containing every node located at the opposite endpoint
     * of an adjacent undirected links. The returned list can be
     * subsequently modified without effect on the topology.
     *
     * @return A list containing the neighbors, with possible duplicates
     * when several links are shared with a same neighbor.
     */
    public List<Node> getNeighbors() {
        LinkedHashSet<Node> neighbors = new LinkedHashSet<>();
        for (Link l : getLinks(Link.Orientation.UNDIRECTED))
            neighbors.add(l.getOtherEndpoint(this));
        return new ArrayList<>(neighbors);
    }

    /**
     * Returns a list of messages representing the mailbox of this node.
     * The mailbox can be useful to scrutinize new messages in a non-event,
     * round-based way (as opposed to the onMessage() method), or to clear previous
     * messages (since all received messages are retained in the mailbox). The
     * returned list must be considered as the original copy of the node's
     * mailbox.
     *
     * @return the {@link List} of incoming {@link Message}s
     */
    public List<Message> getMailbox() {
        return mailBox;
    }

    /**
     * Returns a list of the messages that this node is about to send.
     *
     * @return the {@link List} of outgoing {@link Message}s
     */
    public List<Message> getOutbox() {
        return new ArrayList<>(sendQueue);
    }

    /**
     * Sends a message from this node to the specified destination node.
     * The content of the
     * message is specified as an object reference, to be passed 'as is' to the
     * destination(s).
     *
     * @param destination The destination node.
     * @param message     The message to be sent.
     */
    public void send(Node destination, Message message) {
        Message m = new Message(this, destination, message);
        sendQueue.add(m);
    }

    /**
     * Same as <code>send()</code>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param destination The non-null destination.
     * @param content     The object to be sent.
     *
     * @deprecated Please use {@link #send(Node, Message)} instead.
     */
    @Deprecated
    public void send(Node destination, Object content) {
        send(destination, new Message(content));
    }

    /**
     * Same method as <code>send()</code>, but retries to send the message later
     * if the link to the destination disappeared during transmission.
     * (Does not work for <code>null</code> destinations.)
     *
     * @param destination The non-null destination.
     * @param message     The message.
     */
    public void sendRetry(Node destination, Message message) {
        assert (destination != null);
        Message m = new Message(message);
        m.retryMode = true;
        send(destination, m);
    }

    /**
     * Same as <code>sendRetry()</code>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param destination The non-null destination.
     * @param content     The object to be sent.
     *
     * @deprecated Please use {@link #sendRetry(Node, Message)} instead.
     */
    @Deprecated
    public void sendRetry(Node destination, Object content) {
        sendRetry(destination, new Message(content));
    }

    /**
     * <p>Sends a message to all neighbors. The content of the
     * message is specified as an object reference, to be passed 'as is' to the
     * destination(s).</p>
     * <p>The actual list of neighbors to which the message will be sent is decided, by the {@link MessageEngine},
     * at the very start of the next round.</p>
     *
     * <p>If you need to send your message to the exact list of neighbors at another moment, you should implement it
     * with a loop and calls to {@link #send(Node, Message)}.</p>
     *
     * @param message The message to be sent.
     */
    public void sendAll(Message message) {
        send(null, message);
    }

    /**
     * Same as <code>sendAll()</code>, but the content is directly given as parameter
     * (a message will be created to contain it).
     *
     * @param content The object to be sent.
     *
     * @deprecated Please use {@link #sendAll(Message)} instead.
     */
    @Deprecated
    public void sendAll(Object content) {
        sendAll(new Message(content));
    }

    /**
     * Returns the distance between this node and the specified node.
     *
     * @param n The other node.
     * @return the distance, as a double.
     */
    public double distance(Node n) {
        return coords.distance(n.coords);
    }

    /**
     * Returns the distance between this node and the specified 2D location.
     *
     * @param p The location (as a point).
     * @return the distance, as a double.
     */
/*    public double distance(Point p) {
        return coords.distance(p.getX(), p.getY(), 0);
    }*/

    /**
     * Returns the distance between this node and the specified 2D location.
     *
     * @param x The abscissa of the point to which the distance is measured.
     * @param y The ordinate of the point to which the distance is measured.
     * @return the distance, as a double.
     */
    public double distance(double x, double y) {
        return coords.distance(x, y, 0);
    }

    /**
     * Returns the distance between this node and the specified 3D location.
     *
     * @param p The location (as a 3D point).
     * @return the distance, as a double.
     */
    public double distance(Point p) {
        return coords.distance(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Returns the distance between this node and the specified 3D location.
     *
     * @param x The abscissa of the point to which the distance is measured.
     * @param y The ordinate of the point to which the distance is measured.
     * @param z The applicate of the point to which the distance is measured.
     * @return the distance, as a double.
     */
    public double distance(double x, double y, double z) {
        return coords.distance(x, y, z);
    }

    protected void notifyNodeMoved() {
        onMovement();
        if (topo != null)
            for (MovementListener ml : new ArrayList<>(topo.movementListeners))
                ml.onMovement(this);
    }

    @Override
    public int compareTo(Node node) {
        int myId = getID();
        int otherId = node.getID();
        if(myId < otherId) return -1;
        if(myId > otherId) return 1;
        return 0;
    }

    /**
     * Returns a string representation of this node.
     */
    public String toString() {
        if (label == null)
            return ID.toString();
        else
            return label.toString();
    }
}
