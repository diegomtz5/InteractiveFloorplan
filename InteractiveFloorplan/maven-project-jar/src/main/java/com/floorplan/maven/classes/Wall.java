package com.floorplan.maven.classes;
import java.awt.Color;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

class Wall implements Shape, Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    int x1, y1, x2, y2, thickness;
    Color color = Color.BLACK; // Default color, can be changed as needed
    private double rotationAngle = 0; // Degrees

    public Wall(int x1, int y1, int x2, int y2, int thickness, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
        this.color = color;
    }

    public boolean contains(Point p, double zoomFactor) {
        // Calculate the midpoint for the rotation pivot
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        // Create an AffineTransform for the inverse rotation
        AffineTransform inverseTransform = AffineTransform.getRotateInstance(
            -Math.toRadians(rotationAngle), midX, midY);

        // Create a new Point2D from the point to be checked
        Point2D.Double src = new Point2D.Double(p.x, p.y);

        // Apply the inverse rotation to the point
        Point2D.Double dst = new Point2D.Double();
        inverseTransform.transform(src, dst);

        // Use the transformed point for the distance check
        double threshold = 10.0 / zoomFactor; // Smaller threshold when zoomed in for finer selection control
        return Line2D.ptSegDist(x1, y1, x2, y2, dst.x, dst.y) < threshold;
    }

	public void rotate(double angle) {
        rotationAngle += angle;

	}

    public void draw(Graphics2D g2d) {
    	  // Calculate midpoint for the rotation pivot
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        // Save the current transform of the graphics context
        AffineTransform originalTransform = g2d.getTransform();

        // Rotate around the midpoint
        g2d.rotate(Math.toRadians(rotationAngle), midX, midY);

        // Set color and stroke for drawing
        int scaledThickness = (int) Math.max(1, thickness); // Ensure at least 1px thickness
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(scaledThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Draw the line representing the wall
        g2d.drawLine(x1, y1, x2, y2);

        // Restore the original transform to avoid affecting subsequent drawing operations
        g2d.setTransform(originalTransform);
    }
    public void resizeStartPoint(int newX, int newY) {
        x1 = newX;
        y1 = newY;
    }

    public void resizeEndPoint(int newX, int newY) {
        x2 = newX;
        y2 = newY;
    }
    public void resize(int x, int y) {}
    public void moveTo(int x, int y) {
        int dx = x - ((x1 + x2) / 2); // Difference from the midpoint's x to new x
        int dy = y - ((y1 + y2) / 2); // Difference from the midpoint's y to new y
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
    public void resize(int x, int y, boolean isStartPoint) {
        if (isStartPoint) {
            // If the start point is being resized, update x1 and y1
            x1 = x;
            y1 = y;
        } else {
            // Otherwise, update x2 and y2
            x2 = x;
            y2 = y;
        }
    }
    public Point getReferencePoint() {
        // Return the midpoint of the wall as the reference point
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        return new Point(midX, midY);
    }

    public Rectangle getBounds(double zoomFactor) {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int extra = thickness / 2;
        return new Rectangle(xMin - extra, yMin - extra, (xMax - xMin) + thickness, (yMax - yMin) + thickness);
}
    public Rectangle getBounds() {
        // Calculate the minimum and maximum coordinates, accounting for the thickness of the wall
        int xMin = Math.min(x1, x2) - thickness / 2;
        int xMax = Math.max(x1, x2) + thickness / 2;
        int yMin = Math.min(y1, y2) - thickness / 2;
        int yMax = Math.max(y1, y2) + thickness / 2;

        // Apply zoom factor and translation to the coordinates
        int adjustedXMin = (int) ((xMin ));
        int adjustedYMin = (int) ((yMin ));
        int adjustedWidth = (int) ((xMax - xMin) );
        int adjustedHeight = (int) (yMax - yMin);

        // Return the adjusted bounding rectangle
        return new Rectangle(adjustedXMin, adjustedYMin, adjustedWidth, adjustedHeight);
    }

	public double getRotationAngle() {
		return rotationAngle;
	}
    // Methods for color, etc.
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}


