package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Sink extends FurnitureItem {

    public Sink(int x, int y, int width, int height) {
        super(x, y, width, height);
        // No need to set a specific color since we'll be using black lines only
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the sink's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Set the color to black for the outlines

        // Draw the outline of the countertop
        Rectangle2D.Double countertop = new Rectangle2D.Double(x, y, width, height);
        g2d.draw(countertop);

        // Adjust the oval to fill more of the rectangle
        Ellipse2D.Double bowl = new Ellipse2D.Double(
            x + width * 0.1, y + height * 0.1, width * 0.8, height * 0.7);
        g2d.draw(bowl);

        // Add a small faucet line on the left edge of the oval
        int faucetX = (int) (x + width * 0.1); // Position at the left edge of the bowl
        int faucetYStart = (int) (y + height * 0.4); // Start point of the faucet line
        int faucetYEnd = (int) (y + height * 0.5); // End point of the faucet line
        g2d.drawLine(faucetX, faucetYStart, faucetX + (int) (width * 0.25), faucetYStart); // Draw faucet facing right

        // Reset the stroke to default after drawing
        g2d.setStroke(new BasicStroke(1));

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
