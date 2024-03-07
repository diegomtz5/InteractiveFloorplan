package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Fridge extends FurnitureItem {

    public Fridge(int x, int y, int width, int height) {
        super(x, y, width, height);
        // Set a default color for the fridge, typically white or a metallic color
        this.color = Color.BLACK;
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the fridge's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(color); // Use the fridge's color

        // Draw the outline of the fridge
        Rectangle2D.Double fridgeOutline = new Rectangle2D.Double(x, y, width, height);
        g2d.draw(fridgeOutline);

        // Draw the division between the fridge and the freezer
        int divisionY = y + (int) (height * 0.8); // Assuming the freezer is on the top and smaller
        g2d.drawLine(x, divisionY, x + width, divisionY);

        // Draw handle on the fridge door
        int handleWidth = width / 15; // Handle width as a fraction of the fridge's width
        int handleHeight = height / 10; // Handle height as a fraction of the fridge's height
        int handleX = x + width - handleWidth - (width / 50); // Slightly inset from the right edge
        int handleY = divisionY + ((y + height - divisionY) ) - (handleHeight) + handleHeight; // Centered on the main fridge door
        Rectangle2D.Double handle = new Rectangle2D.Double(handleX, handleY, handleWidth, handleHeight);
        g2d.fill(handle); // Fill the handle for better visibility

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
