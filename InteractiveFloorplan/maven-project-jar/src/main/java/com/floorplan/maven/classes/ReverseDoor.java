package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class ReverseDoor extends FurnitureItem {

    public ReverseDoor(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.color = Color.BLACK; // Color for the door representation
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the door's center
        AffineTransform transform = AffineTransform.getRotateInstance(
                Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        g2d.setColor(color); // Set the color for the door representation
        g2d.setStroke(new BasicStroke(3)); // Set the thickness of the door line

        // Create the path for the door in reverse direction
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x + width, y + height); // Start from the bottom right corner
        path.lineTo(x + width, y); // Line up to the top right corner
        path.quadTo(x + width / 2.0, y - height / 4.0, x, y + height); // Curve to bottom left corner

        g2d.draw(path); // Draw the reverse door

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
