package com.floorplan.maven.classes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

class Circle implements Shape {
    int x, y, radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void draw(Graphics2D g2d) {
        // Save the current stroke
        Stroke originalStroke = g2d.getStroke();

        // Set the stroke for the circle to be thin
        g2d.setStroke(new BasicStroke(1)); // Set the thickness for the circle

        // Draw the circle
        g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

        // Restore the original stroke so that other shapes are not affected
        g2d.setStroke(originalStroke);
    }
    public boolean contains(Point p) {
        double dist = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return dist <= radius;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);
    }


}
