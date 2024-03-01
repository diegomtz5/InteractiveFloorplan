package com.floorplan.maven.classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

class Circle implements Shape {
    int x, y, radius;
    Color color = Color.BLACK; // Default color
    private double rotationAngle = 0; // Rotation angle in degrees

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

    public void rotate(double angle) {
        rotationAngle += angle; // Update the rotation angle
    }

    public void draw(Graphics2D g2d) {
        // Save the current stroke and transform
        Stroke originalStroke = g2d.getStroke();
        AffineTransform originalTransform = g2d.getTransform();

        // Set the color for the circle
        g2d.setColor(color);

        // Apply rotation, if there's an aspect of the circle that requires orientation
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotationAngle), x, y);
        g2d.transform(transform);

        // Set the stroke for the circle to be thin
        g2d.setStroke(new BasicStroke(1)); // Set the thickness for the circle

        // Draw the circle (or any oriented feature on the circle)
        g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

        // Restore the original stroke and transform so that other shapes are not affected
        g2d.setStroke(originalStroke);
        g2d.setTransform(originalTransform);
    }

    public boolean contains(Point p, double zoomFactor) {
        double threshold = radius / zoomFactor; // Use the radius adjusted by the zoom factor
        double dist = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
        return dist <= threshold;
    }

    public Point getReferencePoint() {
        return new Point(x, y); // The center point is the logical reference for a circle
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
