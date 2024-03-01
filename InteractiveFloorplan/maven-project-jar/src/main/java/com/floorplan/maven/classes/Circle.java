
package com.floorplan.maven.classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

class Circle implements Shape {
    int x, y, radius;
    Color color = Color.BLACK; // Default color, can be changed as needed

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics2D g2d) {
        // Save the current stroke
        Stroke originalStroke = g2d.getStroke();

        // Set the color for the circle
        g2d.setColor(color);

        // Set the stroke for the circle to be thin
        g2d.setStroke(new BasicStroke(1)); // Set the thickness for the circle

        // Draw the circle
        g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

        // Restore the original stroke so that other shapes are not affected
        g2d.setStroke(originalStroke);
    }

    public boolean contains(Point p, double zoomFactor) {
        // Adjust the hit-test threshold based on the zoom factor
        double threshold = radius / zoomFactor; // Use the radius adjusted by the zoom factor
        double dist = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return dist <= threshold;
    }

    public Point getReferencePoint() {
        // For a circle, the logical reference point is its center
        return new Point(x, y);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
