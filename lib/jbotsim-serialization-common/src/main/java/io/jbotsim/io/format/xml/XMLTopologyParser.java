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
package io.jbotsim.io.format.xml;

import io.jbotsim.core.*;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

import static io.jbotsim.core.Link.Mode.WIRED;
import static io.jbotsim.core.Link.Orientation.DIRECTED;
import static io.jbotsim.core.Link.Orientation.UNDIRECTED;

/**
 * Interpreter for an {@link org.w3c.dom.Document XML document} that represents a {@link Topology}.
 *
 * This class is used through its parent class {@link XMLParser}. It overrides {@link #parseRootElement(Element)} to
 * interpret the document as a {@link Topology}.
 *
 * The class does not create an new topology but populates an existing one passed to the constructor. The client code
 * is in charge of clearing the given topology.
 */
public class XMLTopologyParser extends XMLParser {
    private final Topology tp;

    /**
     * Construct a new topology parser.
     *
     * @param tp the {@link Topology} populated by the parser.
     * @param validateDocument enable or disable the validation of the document using an XSD schema.
     */
    public XMLTopologyParser(Topology tp, boolean validateDocument) {
        super(validateDocument);
        this.tp = tp;
    }

    @Override
    public void parseRootElement(Element element) throws ParserException {
        parseTopologyElement(element, tp);
    }

    /**
     * Interprets the root element of a topology and fulfilled the one passed as argument.
     *
     * @param topo the {@link Element root element} that describes the topology
     * @param tp the {@link Topology topology} that is populated with XML data.
     * @throws ParserException is raised if the given XML document is malformed.
     */
    public static void parseTopologyElement(Element topo, Topology tp) throws ParserException {
        if (!XMLKeys.TOPOLOGY.equals(topo.getNodeName()))
            throw new ParserException("invalid node '" + topo.getNodeName() + "' where '" + XMLKeys.TOPOLOGY + "' was expected");

        if (XMLKeys.WIRELESS_ENABLED_ATTR.isAttributeOf(topo)) {
            if (XMLKeys.WIRELESS_ENABLED_ATTR.getValueFor(topo, XMLKeys.BooleanFromString)) {
                tp.enableWireless();
            } else
                tp.disableWireless();
        }

        if (XMLKeys.TIME_UNIT_ATTR.isAttributeOf(topo)) {
            tp.setTimeUnit(XMLKeys.TIME_UNIT_ATTR.getValueFor(topo, XMLKeys.IntegerFromString));
        }

        int width = XMLKeys.WIDTH_ATTR.getValueFor(topo, Topology.DEFAULT_WIDTH);
        int height = XMLKeys.HEIGHT_ATTR.getValueFor(topo, Topology.DEFAULT_HEIGHT);
        double sr = XMLKeys.SENSING_RANGE_ATTR.getValueFor(topo, Topology.DEFAULT_SENSING_RANGE);
        double cr = XMLKeys.COMMUNICATION_RANGE_ATTR.getValueFor(topo, Topology.DEFAULT_COMMUNICATION_RANGE);

        tp.setDimensions(width, height);
        tp.setSensingRange(sr);
        tp.setCommunicationRange(cr);

        mapElementChildrenOf(topo, e -> {
            if (XMLKeys.CLASSES.labelsElement(e))
                mapElementChildrenOf(e, el -> parseClass(el,tp) );
            else if (XMLKeys.GRAPH.labelsElement(e))
                parseGraphElement(e, tp);
        });
    }

    private static void parseClass(Element C, Topology tp) throws ParserException {
        String ID = XMLKeys.IDENTIFIER_ATTR.getValueFor(C, "default");
        String className = XMLKeys.CLASS_ATTR.getValueFor(C, (String) null);
        if (className == null) // this case should be handled by XSD validator
            throw new ParserException("missing 'class' attribute in element:" + C.getNodeName());

        try {
            Class<?> c = Class.forName(className);
            if (XMLKeys.LINK_RESOLVER.labelsElement(C)) {
                Class<? extends LinkResolver> cls = c.asSubclass(LinkResolver.class);
                tp.setLinkResolver(cls.getConstructor().newInstance());
            } else if (XMLKeys.MESSAGE_ENGINE.labelsElement(C)) {
                Class<? extends MessageEngine> cls = c.asSubclass(MessageEngine.class);
                tp.setMessageEngine(cls.getConstructor(Topology.class).newInstance(tp));
            } else if (XMLKeys.SCHEDULER.labelsElement(C)) {
                Class<? extends Scheduler> cls = c.asSubclass(Scheduler.class);
                tp.setScheduler(cls.getConstructor().newInstance());
            } else if (XMLKeys.CLOCKCLASS.labelsElement(C)) {
                Class<? extends Clock> cls = c.asSubclass(Clock.class);
                tp.setClockModel(cls);
            } else if (XMLKeys.NODECLASS.labelsElement(C)) {
                Class<? extends Node> cls = c.asSubclass(Node.class);
                if ("default".equals(ID))
                    tp.setDefaultNodeModel(cls);
                else
                    tp.setNodeModel(ID, cls);
            } else {
                // this case should be handled by XSD validator
                throw new ParserException("unknown model class: " + C.getNodeName());
            }
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private static void parseGraphElement(Element ge, Topology tp) throws ParserException {
        HashMap<String, Node> nodeids = new HashMap<>();
        mapElementChildrenOf(ge, e -> {
            if (XMLKeys.NODE.labelsElement(e))
                parseNode(e, tp, nodeids);
            else if (XMLKeys.LINK.labelsElement(e))
                parseLink(e, tp, nodeids);
            else throw new EnumConstantNotPresentException(XMLKeys.class, e.getTagName());
        });
    }

    private static Color parseColor(Element e, Color default_color) {
        Color result = default_color;
        String color = XMLKeys.COLOR_ATTR.getValueFor(e, (String) null);
        if (color != null && !color.equals("None")) {
            result = new Color((int) Long.parseLong(color, 16));
        }
        return result;
    }

    private static Class<? extends Node> getNodeClass(Topology tp, String className) throws ParserException {
        try {
            Class<? extends Node> nodeClass;
            if ("default".equals(className) || className == null) {
                nodeClass = tp.getDefaultNodeModel();
                if (nodeClass == null) {
                    nodeClass = Node.class;
                }
            } else {
                nodeClass = tp.getNodeModel(className);
            }
            if (nodeClass == null) {
                nodeClass = Class.forName(className).asSubclass(Node.class);
            }
            return nodeClass;
        } catch (ClassNotFoundException e) {
            throw new ParserException(e);
        }
    }

    private static void parseNode(Element ne, Topology tp, Map<String, Node> nodeids) throws ParserException {
        Node n;

        try {
            Class<? extends Node> nodeClass = tp.getDefaultNodeModel();

            if (XMLKeys.CLASS_ATTR.isAttributeOf(ne)) {
                nodeClass = getNodeClass(tp, XMLKeys.CLASS_ATTR.getValueFor(ne));
            }
            if (nodeClass == null) {
                nodeClass = Node.class;
            }
            n = nodeClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new ParserException(ex);
        }

        if (!XMLKeys.IDENTIFIER_ATTR.isAttributeOf(ne))
            throw new ParserException("node identifier is missing");
        String id = XMLKeys.IDENTIFIER_ATTR.getValueFor(ne);
        if (nodeids.containsKey(id))
            throw new ParserException("node identifier is already used: " + id);
        nodeids.put(id, n);

        n.setColor(parseColor(ne, Node.DEFAULT_COLOR));
        if (XMLKeys.ICON_ATTR.isAttributeOf(ne)) {
            n.setIcon(XMLKeys.ICON_ATTR.getValueFor(ne));
        }

        int size = XMLKeys.SIZE_ATTR.getValueFor(ne, Node.DEFAULT_ICON_SIZE);
        n.setIconSize(size);
        double cr = XMLKeys.COMMUNICATION_RANGE_ATTR.getValueFor(ne, tp.getCommunicationRange());
        n.setCommunicationRange(cr);
        double sr = XMLKeys.SENSING_RANGE_ATTR.getValueFor(ne, tp.getSensingRange());
        n.setSensingRange(sr);
        double dir = XMLKeys.DIRECTION_ATTR.getValueFor(ne, Node.DEFAULT_DIRECTION);
        n.setDirection(dir);
        double x = XMLKeys.LOCATION_X_ATTR.getValueFor(ne, -1.0);
        double y = XMLKeys.LOCATION_Y_ATTR.getValueFor(ne, -1.0);
        double z = XMLKeys.LOCATION_Z_ATTR.getValueFor(ne, 0.0);
        n.setLocation(x, y, z);

        tp.addNode(n);
    }

    private static void parseLink(Element e, Topology tp, Map<String, Node> nodeids) throws ParserException {
        Link.Orientation orientation = XMLKeys.DIRECTED_ATTR.getValueFor(e, false) ? DIRECTED : UNDIRECTED;

        Node src = nodeids.get(XMLKeys.SOURCE_ATTR.getValueFor(e));
        if (src == null)
            throw new ParserException("unknown node: " + XMLKeys.SOURCE_ATTR.getValueFor(e));
        Node dst = nodeids.get(XMLKeys.DESTINATION_ATTR.getValueFor(e));
        if (dst == null)
            throw new ParserException("unknown node: " + XMLKeys.DESTINATION_ATTR.getValueFor(e));
        Link l = new Link(src, dst, orientation, WIRED);

        l.setWidth(XMLKeys.WIDTH_ATTR.getValueFor(e, Link.DEFAULT_WIDTH));
        l.setColor(parseColor(e, Link.DEFAULT_COLOR));
        tp.addLink(l, true);
    }

}
