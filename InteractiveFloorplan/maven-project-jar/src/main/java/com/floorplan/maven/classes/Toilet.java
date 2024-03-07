package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Toilet extends FurnitureItem {

    public Toilet(int x, int y, int width, int height) {
        super(x, y, width, height);
        // No need to set a specific color since we'll be using black lines only
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the toilet's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Set the color to black for the outlines

        // Draw the elongated outline of the toilet bowl
        Ellipse2D.Double bowl = new Ellipse2D.Double(
                x + width * 0.2, y + height * 0.4, width * 0.6, height * 0.5);
        g2d.draw(bowl);

        // Draw the outline of the toilet base
        RoundRectangle2D.Double base = new RoundRectangle2D.Double(
                x, y + height * 0.7, width, height * 0.3, 10, 10);
        g2d.draw(base);

        // Add a line for the flush handle to the base
        int handleWidth = width / 15; // Handle width as a fraction of the total width
        int handleHeight = height / 15; // Handle height as a fraction of the total height
        g2d.draw(new RoundRectangle2D.Double(
                x + width * 0.85, y + height * 0.85, handleWidth, handleHeight, 5, 5));

        // Reset the stroke to default after drawing
        g2d.setStroke(new BasicStroke(1));

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
