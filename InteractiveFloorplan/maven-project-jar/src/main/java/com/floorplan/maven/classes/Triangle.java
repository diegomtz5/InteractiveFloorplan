package com.floorplan.maven.classes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

class Triangle implements Shape {
    int x, y; // Center point
    int side; // Length of each side
    Color color = Color.BLACK; // Default color

    public Triangle(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color); // Set the color for the triangle

        // Calculate vertices of the triangle
        int height = (int) (Math.sqrt(3) / 2 * side);
        int[] xPoints = {x - side / 2, x + side / 2, x};
        int[] yPoints = {y + height / 2, y + height / 2, y - height};

        // Draw the triangle
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    public void setSide(int side) {
        this.side = side;
    }

    public boolean contains(Point p, double zoomFactor) {
        // Adjust the hit-test threshold based on the zoom factor
        java.awt.Polygon poly = new java.awt.Polygon(
            new int[]{x - side / 2, x + side / 2, x},
            new int[]{y + (int) (Math.sqrt(3) / 2 * side) / 2, y + (int) (Math.sqrt(3) / 2 * side) / 2, y - (int) (Math.sqrt(3) / 2 * side) / 2},
            3
        );
        return poly.contains(p.x / zoomFactor, p.y / zoomFactor);
    }
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getReferencePoint() {
        // The reference point for a triangle could be its centroid or any vertex
        // Here we use the center point as the reference
        return new Point(x, y);
    }

    public Rectangle getBounds() {
        int height = (int) (Math.sqrt(3) / 2 * side);
        return new Rectangle(x - side / 2, y - height / 2, side, height);
    }
}
