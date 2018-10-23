/**
 * This package gathers classes used to load/save XML documents that store different kind of JBotsim objects.
 *
 * <p>Currently two kind of objects can be stored using XML format:</p>
 * <ul>
 *     <li>{@link io.jbotsim.core.Topology Topologies}</li>
 *     <li>Traces of simulations of topologies (see {@link io.jbotsim.dynamicity.movement.trace.TraceRecorder} and
 *     {@link io.jbotsim.dynamicity.movement.trace.TracePlayer}).</li>
 * </ul>
 *
 * <p>In order to store (into a file or elsewhere) one of these objects, an {@link io.jbotsim.format.xml.XMLBuilder}
 * must be instantiated with the object to be stored as argument of the constructor. Once the document is terminated
 * (e.g. when all events of the trace have been collected), {@code write} methods are used to output the
 * {@link org.w3c.dom.Document} (the helper methods of {@link io.jbotsim.format.xml.XMLIO} can also be used).</p>
 *
 * <p>Following example shows how to output a {@link io.jbotsim.core.Topology} on standard output (using XML format):</p>
 * <pre>
 *  Topology tp = new Topology();
 *  TopologyGenerator.generateKN(tp,5);
 *  try {
 *    XMLTopologyBuilder bd = new XMLTopologyBuilder(tp);
 *    bd.write(System.out);
 *  } catch (XMLTopologyBuilder.BuilderException e) {
 *    e.printStackTrace();
 *  }
 * </pre>
 *
 * <p>Similarly the loading of an XML document is realized given a destination object i.e. either a
 * {@link io.jbotsim.core.Topology} or a {@link io.jbotsim.dynamicity.movement.trace.TracePlayer}. For instance the following code initializes
 * a topology {@code tp} with the first parameter of the {@code main} method: </p>
 * <pre>
 *  Topology tp = new Topology();
 *  try {
 *    XMLTopologyParser parser = new XMLTopologyParser(tp);
 *    parser.parse(args[0]);
 *  } catch (XMLParser.ParserException e) {
 *    e.printStackTrace();
 *  }
 * </pre>
 */
package io.jbotsim.format.xml;