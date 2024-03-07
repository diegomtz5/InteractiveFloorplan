package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Stove extends FurnitureItem {

    public Stove(int x, int y, int width, int height) {
        super(x, y, width, height);
        // No need to set a specific color since we'll be using black lines only
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the stove's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Set the color to black for the outlines

        // Draw the outline of the stove
        Rectangle2D.Double stoveOutline = new Rectangle2D.Double(x, y, width, height);
        g2d.draw(stoveOutline);

        // Burner dimensions
        int burnerDiameter = Math.min(width, height) / 4; // Diameter of each burner
        int innerBurnerDiameter = burnerDiameter / 2; // Diameter of the inner circle of each burner
        int spacing = burnerDiameter / 2; // Spacing between the burners and the edges

        // Function to draw a burner with an inner circle
        drawBurner(g2d, x + spacing, y + spacing, burnerDiameter, innerBurnerDiameter); // Top-left burner
        drawBurner(g2d, x + width - spacing - burnerDiameter, y + spacing, burnerDiameter, innerBurnerDiameter); // Top-right burner
        drawBurner(g2d, x + spacing, y + height - spacing - burnerDiameter, burnerDiameter, innerBurnerDiameter); // Bottom-left burner
        drawBurner(g2d, x + width - spacing - burnerDiameter, y + height - spacing - burnerDiameter, burnerDiameter, innerBurnerDiameter); // Bottom-right burner

        // Reset the stroke to default after drawing
        g2d.setStroke(new BasicStroke(1));

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }

    private void drawBurner(Graphics2D g2d, double x, double y, int burnerDiameter, int innerBurnerDiameter) {
        // Draw the outer circle of the burner
        g2d.draw(new Ellipse2D.Double(x, y, burnerDiameter, burnerDiameter));

        // Calculate the position for the inner circle to center it within the outer circle
        double innerX = x + (burnerDiameter - innerBurnerDiameter) / 2;
        double innerY = y + (burnerDiameter - innerBurnerDiameter) / 2;

        // Draw the inner circle of the burner
        g2d.draw(new Ellipse2D.Double(innerX, innerY, innerBurnerDiameter, innerBurnerDiameter));
    }
}
