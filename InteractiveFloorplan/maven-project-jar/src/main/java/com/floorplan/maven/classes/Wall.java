package com.floorplan.maven.classes;
import java.awt.Color;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

class Wall implements Shape {
    int x1, y1, x2, y2, thickness;
    Color color = Color.BLACK; // Default color, can be changed as needed

    public Wall(int x1, int y1, int x2, int y2, int thickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
    }

    public boolean contains(Point p, double zoomFactor) {
        double threshold = 10.0 / zoomFactor; // Smaller threshold when zoomed in for finer selection control
        return Line2D.ptSegDist(x1, y1, x2, y2, p.x, p.y) < threshold;
    }

    public void draw(Graphics2D g2d, double zoomFactor) {
        int scaledThickness = (int) Math.max(1, thickness * zoomFactor); // Ensure at least 1px thickness
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(scaledThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public void moveTo(int x, int y) {
        int dx = x - ((x1 + x2) / 2); // Difference from the midpoint's x to new x
        int dy = y - ((y1 + y2) / 2); // Difference from the midpoint's y to new y
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

    public Point getReferencePoint() {
        // Return the midpoint of the wall as the reference point
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        return new Point(midX, midY);
    }

    public Rectangle getBounds() {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        int extra = thickness / 2;
        return new Rectangle(xMin - extra, yMin - extra, (xMax - xMin) + thickness, (yMax - yMin) + thickness);
    }

    // Methods for color, etc.
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}


