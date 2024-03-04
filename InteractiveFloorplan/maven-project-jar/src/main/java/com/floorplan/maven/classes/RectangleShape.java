package com.floorplan.maven.classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class RectangleShape implements Shape {
    int x, y, width, height;
    Color color = Color.BLACK; // Default color
    private double rotationAngle = 0; // Rotation angle in degrees

    public RectangleShape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void rotate(double angle) {
        rotationAngle += angle; // Update the rotation angle
    }

    public void draw(Graphics2D g2d) {
        // Save the current stroke and transform
        Stroke originalStroke = g2d.getStroke();
        AffineTransform originalTransform = g2d.getTransform();

        // Set the color for the rectangle
        g2d.setColor(color);

        // Apply rotation around the rectangle's center
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
        g2d.transform(transform);

        // Set the stroke for the rectangle to be thin
        g2d.setStroke(new BasicStroke(1));

        // Draw the rectangle
        g2d.drawRect(x, y, width, height);

        // Restore the original stroke and transform so that other shapes are not affected
        g2d.setStroke(originalStroke);
        g2d.setTransform(originalTransform);
    }

    public boolean contains(Point p, double zoomFactor) {
        // Create an AffineTransform for the inverse rotation
        // The rotation needs to be around the center of the rectangle
        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;
        AffineTransform inverseTransform = AffineTransform.getRotateInstance(
            -Math.toRadians(rotationAngle), centerX / zoomFactor, centerY / zoomFactor);

        // Adjust the point for zoom and create a Point2D for transformation
        Point2D.Double src = new Point2D.Double(p.x / zoomFactor, p.y / zoomFactor);

        // Apply the inverse rotation to the point
        Point2D.Double dst = new Point2D.Double();
        inverseTransform.transform(src, dst);

        // Now check if the transformed point is inside the non-rotated, zoom-adjusted rectangle
        // The rectangle's bounds need to be adjusted for the zoom as well
        Rectangle zoomedRect = new Rectangle(
            (int) (x / zoomFactor),
            (int) (y / zoomFactor),
            (int) (width / zoomFactor),
            (int) (height / zoomFactor));

        return zoomedRect.contains(dst);
    }



    public Point getReferencePoint() {
        // The top-left corner is used as the reference point
        return new Point(x, y);
    }

    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void resize(int newX, int newY) {
        // Update the dimensions based on the new point, assuming this defines the bottom-right corner
        this.width = newX - x;
        this.height = newY - y;
    }

    public Rectangle getBounds() {
        // Return the bounds of the rectangle without adjusting for zoom, as zoom handling is done elsewhere
        return new Rectangle(x, y, width, height);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }
}
