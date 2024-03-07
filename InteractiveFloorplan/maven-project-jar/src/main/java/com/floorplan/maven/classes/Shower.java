package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Shower extends FurnitureItem {

    public Shower(int x, int y, int width, int height) {
        super(x, y, width, height);
        // Set a default color for the shower area
        this.color = new Color(173, 216, 230); // Light blue to represent water
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the shower's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Set the color to black for the outlines

        // Draw the outline of the shower area
        Rectangle2D.Double showerOutline = new Rectangle2D.Double(x, y, width, height);
        g2d.draw(showerOutline);

        // Draw the showerhead at the top
        int showerheadWidth = width / 3; // Showerhead width as a fraction of the shower area width
        int showerheadHeight = height / 10; // Showerhead height as a fraction of the shower area height
        Ellipse2D.Double showerhead = new Ellipse2D.Double(
            x + width / 2.0 - showerheadWidth / 2.0, y + showerheadHeight, showerheadWidth, showerheadHeight);
        g2d.fill(showerhead);

        // Optionally, add a line to represent the shower door or curtain
        g2d.setStroke(new BasicStroke(1)); // Thinner line for the door/curtain
        g2d.drawLine(x, y + height / 10, x, y + height); // Simple vertical line on one side

        // Draw an "X" in the middle of the shower area
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int xSize = Math.min(width, height) / 4; // Adjust the size of the "X" as needed
        g2d.drawLine(centerX - xSize, centerY - xSize, centerX + xSize, centerY + xSize); // Diagonal line 1 for "X"
        g2d.drawLine(centerX - xSize, centerY + xSize, centerX + xSize, centerY - xSize); // Diagonal line 2 for "X"

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}

