package io.jbotsim.ui.painting;

import io.jbotsim.core.Color;
import io.jbotsim.core.Link;

import java.awt.*;


/**
 * Created by acasteig on 01/11/17.
 */

/**
 * <p>The {@link JSLGraphLinkPainter} is able to display the links for a <b>s</b>ingle <b>l</b>abeled graph.</p>
 * <p>Each label is displayed on its {@link Link}.</p>
 */
public class JSLGraphLinkPainter extends JLinkPainter {

    /**
     * The default property name used when accessing the label in {@link Link}s properties.
     */
    public static final String DEFAULT_LABEL_PROPERTY_NAME = "date";

    private String labelPropertyName;

    public JSLGraphLinkPainter() {
        this(DEFAULT_LABEL_PROPERTY_NAME);
    }

    /**
     * Create a {@link JSLGraphLinkPainter} with a custom name for the label's property
     *
     * @param labelPropertyName name to use when accessing the label's value in the {@link Link}'s properties.
     */
    public JSLGraphLinkPainter(String labelPropertyName) {
        this.labelPropertyName = labelPropertyName;
    }

    @Override
    public void paintLink(UIComponent uiComponent, Link link) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();

        if (link.source.getColor()!= Color.black && link.destination.getColor()!=Color.black) {
            super.paintLink(uiComponent, link);
            float midx = (float) ((link.source.getX() + 1.3 * link.destination.getX()) / 2.3);
            float midy = (float) ((link.source.getY() + 1.3 * link.destination.getY()) / 2.3);
            g2d.setColor(java.awt.Color.black);
            Integer date = (Integer) link.getProperty(labelPropertyName);
            if (date != null) {
                g2d.drawString(date.toString(), midx, midy);
            }
        }
    }
}
