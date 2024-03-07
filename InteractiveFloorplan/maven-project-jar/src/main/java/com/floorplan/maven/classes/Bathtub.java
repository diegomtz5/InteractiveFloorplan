package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;

public class Bathtub extends FurnitureItem{

    public Bathtub(int x, int y, int width, int height)  {
        super(x, y, width, height);
        // Set a default color for the bathtub, typically white or light gray
        this.color = new Color(224, 255, 255); // A light shade of blue to represent water
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the bathtub's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke and color for the bathtub outline
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Black outline for the bathtub

        // Draw the outline of the bathtub
        RoundRectangle2D.Double outerBathtub = new RoundRectangle2D.Double(
                x, y, width, height, 20, 20);
        g2d.draw(outerBathtub);

        // Draw the inner basin of the bathtub
        g2d.setColor(color); // Use the light color to fill the inner basin
        RoundRectangle2D.Double innerBathtub = new RoundRectangle2D.Double(
                x + 5, y + 5, width - 10, height - 10, 15, 15);
        g2d.fill(innerBathtub);

        // Optionally, add a faucet or drain to suggest more details
        g2d.setColor(Color.GRAY); // Color for metallic parts like the faucet
        g2d.fillRect((int) (x + width * 0.8), y, 5, 10); // Simple representation of a faucet

        // Reset the stroke to default after drawing
        g2d.setStroke(new BasicStroke(1));

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
