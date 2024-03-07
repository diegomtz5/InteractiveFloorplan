package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Table extends FurnitureItem {

    public Table(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Apply rotation around the table's center
        AffineTransform transform = AffineTransform.getRotateInstance(
            Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2)); // Set the stroke to define the line thickness

        // Draw the outline of the tabletop
        g2d.drawRect(x, y, width, height);

        // Leg dimensions
        int legWidth = width / 10;
        int legHeight = height / 4;

        // Draw the outlines of the table legs
        g2d.drawRect(x, y + height - legHeight, legWidth, legHeight); // Bottom-left leg
        g2d.drawRect(x + width - legWidth, y + height - legHeight, legWidth, legHeight); // Bottom-right leg
        g2d.drawRect(x, y, legWidth, legHeight); // Top-left leg
        g2d.drawRect(x + width - legWidth, y, legWidth, legHeight); // Top-right leg

        // Chair dimensions and offset
        int chairWidth = width / 5; // Assuming chair width is one-fifth of table width
        int chairHeight = height / 5; // Assuming chair height is one-fifth of table height
        int chairOffset = 5; // Distance from the table to the chair
        int backrestHeight = chairHeight / 4; // Backrest is one-fourth of chair height

        // Draw chairs with backrests
        drawChairWithBackrest(g2d, x + width / 2 - chairWidth / 2, y - chairHeight - chairOffset, chairWidth, chairHeight, backrestHeight); // Top side
        drawChairWithBackrest(g2d, x + width / 2 - chairWidth / 2, y + height + chairOffset, chairWidth, chairHeight, backrestHeight); // Bottom side
        drawChairWithBackrest(g2d, x - chairWidth - chairOffset, y + height / 2 - chairHeight / 2, chairWidth, chairHeight, backrestHeight); // Left side
        drawChairWithBackrest(g2d, x + width + chairOffset, y + height / 2 - chairHeight / 2, chairWidth, chairHeight, backrestHeight); // Right side

        // Reset the stroke to default
        g2d.setStroke(new BasicStroke(1));

        // Restore the original transform
        g2d.setTransform(originalTransform);
    }

    private void drawChairWithBackrest(Graphics2D g2d, int x, int y, int chairWidth, int chairHeight, int backrestHeight) {
        // Draw the seat
        g2d.drawRect(x, y, chairWidth, chairHeight);

        // Draw the backrest
        g2d.fillRect(x, y - backrestHeight, chairWidth, backrestHeight);

        // Optional: Add shading to the chair for depth (remove if too detailed)
        Color originalColor = g2d.getColor();
        g2d.setColor(new Color(0, 0, 0, 30)); // Semi-transparent black for shading
        g2d.fillRect(x, y, chairWidth, chairHeight);
        g2d.setColor(originalColor); // Reset color
    }
}
