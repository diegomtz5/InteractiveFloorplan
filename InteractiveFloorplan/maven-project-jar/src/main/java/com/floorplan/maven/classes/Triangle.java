package com.floorplan.maven.classes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

class Triangle implements Shape {
    int x, y; // Center point
    int side; // Length of each side

    public Triangle(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public void draw(Graphics2D g2d) {
        int height = (int) (Math.sqrt(3) / 2 * side);
        int[] xPoints = {x - side / 2, x + side / 2, x};
        int[] yPoints = {y + height / 2, y + height / 2, y - height / 2};
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    public boolean contains(Point p) {
        // This is a simplified version. Accurate triangle containment is more complex and depends on the triangle type.
        return new java.awt.Polygon(new int[]{x - side / 2, x + side / 2, x}, new int[]{y + side / 2, y + side / 2, y - side / 2}, 3).contains(p);
    }

    public void moveTo(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public Rectangle getBounds() {
        int height = (int) (Math.sqrt(3) / 2 * side);
        return new Rectangle(x - side / 2, y - height / 2, side, height);
    }
}
