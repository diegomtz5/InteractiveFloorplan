package com.floorplan.maven.classes;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

class Wall implements Shape {
    int x1, y1, x2, y2, thickness;
    public boolean contains(Point p) {
        // This method checks if a point is near the wall line. You might use Line2D's ptSegDist method.
        return Line2D.ptSegDist(x1, y1, x2, y2, p.x, p.y) < 10.0; // Consider a wall selected if the click is within 10 pixels
    }
    public Wall(int x1, int y1, int x2, int y2, int thickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
    }

    public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1, y1, x2, y2);
    }
    public void moveTo(int x, int y) {
        int dx = x - x1;
        int dy = y - y1;
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
    public Rectangle getBounds() {
        int xMin = Math.min(x1, x2);
        int xMax = Math.max(x1, x2);
        int yMin = Math.min(y1, y2);
        int yMax = Math.max(y1, y2);
        // Adjusting for thickness
        int extra = thickness / 2;
        return new Rectangle(xMin - extra, yMin - extra, (xMax - xMin) + thickness, (yMax - yMin) + thickness);
    }

}
