package com.floorplan.maven.classes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

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
    public double getRotationAngle() {
		return rotationAngle;
	}

    public boolean contains(Point p, double zoomFactor) {
        // Adjust the point for zoom before applying the inverse rotation
        Point2D.Double src = new Point2D.Double(p.x / zoomFactor, p.y / zoomFactor);

        // Calculate the center point of the triangle, adjusted for zoom
        Point2D.Double center = new Point2D.Double(x / zoomFactor, y / zoomFactor);

        // Create an AffineTransform for the inverse rotation around the zoom-adjusted center
        AffineTransform inverseTransform = AffineTransform.getRotateInstance(
            -Math.toRadians(rotationAngle), center.x, center.y);

        // Apply the inverse rotation to the zoom-adjusted point
        Point2D.Double dst = new Point2D.Double();
        inverseTransform.transform(src, dst);

        // Calculate the height of the triangle based on the side length, adjusted for zoom
        int height = (int) (Math.sqrt(3) / 2 * side / zoomFactor);

        // Calculate vertices of the triangle, adjusted for zoom
        int[] xPoints = {
            (int) (center.x - side / 2 / zoomFactor),
            (int) (center.x + side / 2 / zoomFactor),
            (int) center.x
        };
        int[] yPoints = {
            (int) (center.y + height / 2),
            (int) (center.y + height / 2),
            (int) (center.y - height / 2)
        };

        // Use the transformed point to check if it's inside the zoom-adjusted triangle
        Polygon poly = new Polygon(xPoints, yPoints, 3);
        return poly.contains(dst.x, dst.y);
    }
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void resize(int newX, int newY) {
        // Calculate the new side length in some way based on newX and newY
        // This is a bit more complex for a triangle and depends on how you define its size
        // For example, you might calculate the distance from the center to one of the vertices and use that to set a new size
       // int newSide = ...; // Calculate new side length
        //setSide(newSide);
    }
    public void resize(int newSide) {
        this.side = newSide;
    }

    public Point getReferencePoint() {
        return new Point(x, y); // The center point is used as the reference point
    }

    public Rectangle getBounds() {
        int height = (int) (Math.sqrt(3) / 2 * side);
        return new Rectangle(x - side / 2, y - height / 2, side, height);
    }
}
