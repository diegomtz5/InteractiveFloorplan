package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bed extends FurnitureItem {

    public Bed(int x, int y, int width, int height) {
        super(x, y, width, height);
        // Set a default color for the bed frame
        this.color = new Color(80, 80, 80); // A shade of dark gray for the bed frame
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the bed's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Draw the bed frame with the set color
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);

        // Set a lighter color for the mattress
        g2d.setColor(new Color(245, 245, 245)); // A light shade for the mattress
        int mattressInset = 5; // Inset from the frame's edge
        g2d.fillRect(x + mattressInset, y + mattressInset, width - 2 * mattressInset, height - 2 * mattressInset);

        // Optionally, add a headboard and footboard
        int boardHeight = height / 6; // Headboard and footboard height as a fraction of the bed height
        g2d.setColor(color.darker()); // Darker shade for the boards
        g2d.fillRect(x, y, width, boardHeight); // Headboard
        g2d.fillRect(x, y + height - boardHeight, width, boardHeight); // Footboard

        // Draw pillows with outlines
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(255, 255, 255)); // White color for the pillows
        int pillowWidth = width / 4;
        int pillowHeight = boardHeight; // Same height as the headboard for simplicity
        int pillowX1 = x + width / 4 - pillowWidth / 2; // Position the first pillow
        int pillowX2 = x + 3 * width / 4 - pillowWidth / 2; // Position the second pillow
        int pillowY = y + boardHeight / 2; // Position the pillows on the headboard
        g2d.fillRoundRect(pillowX1, pillowY, pillowWidth, pillowHeight, pillowHeight / 2, pillowHeight / 2); // Rounded first pillow
        g2d.fillRoundRect(pillowX2, pillowY, pillowWidth, pillowHeight, pillowHeight / 2, pillowHeight / 2); // Rounded second pillow

        // Pillow outlines
        g2d.setColor(Color.GRAY); // Color for the pillow outlines
        g2d.drawRoundRect(pillowX1, pillowY, pillowWidth, pillowHeight, pillowHeight / 2, pillowHeight / 2); // Outline for the first pillow
        g2d.drawRoundRect(pillowX2, pillowY, pillowWidth, pillowHeight, pillowHeight / 2, pillowHeight / 2); // Outline for the second pillow

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }
}
