package com.floorplan.maven.classes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

class Triangle implements Shape {
    int x, y; // Center point
    int side; // Length of each side
    Color color = Color.BLACK; // Default color
    private double rotationAngle = 0; // Rotation angle in degrees

    public Triangle(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void rotate(double angle) {
        rotationAngle += angle; // Update the rotation angle
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color); // Set the color for the triangle

        // Save the current transform of the graphics context
        AffineTransform originalTransform = g2d.getTransform();

        // Translate and rotate the graphics context to the center of the triangle and apply rotation
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(rotationAngle));

        // Calculate vertices of the triangle
        int height = (int) (Math.sqrt(3) / 2 * side);
        int[] xPoints = {-side / 2, side / 2, 0}; // Adjusted for new origin at the center
        int[] yPoints = {height / 2, height / 2, -height / 2};

        // Draw the triangle
        g2d.drawPolygon(xPoints, yPoints, 3);

        // Restore the original transform to not affect subsequent drawing
        g2d.setTransform(originalTransform);
    }

    public void setSide(int side) {
        this.side = side;
    }

    public boolean contains(Point p, double zoomFactor) {
        // Implementing hit detection for a rotated shape can be complex
        // This might require transforming the point by the inverse of the shape's transform and then checking against the original shape
        // For simplicity, this implementation does not account for rotation
        Polygon poly = new Polygon(
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
        return new Point(x, y); // The center point is used as the reference point
    }

    public Rectangle getBounds() {
        int height = (int) (Math.sqrt(3) / 2 * side);
        return new Rectangle(x - side / 2, y - height / 2, side, height);
    }
}
