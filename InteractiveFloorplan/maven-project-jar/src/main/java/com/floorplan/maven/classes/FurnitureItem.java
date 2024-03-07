package com.floorplan.maven.classes;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class FurnitureItem implements Shape, Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    protected int x, y, width, height;
    protected Color color = Color.BLACK;
    protected double rotationAngle = 0;

    public FurnitureItem(int x, int y, int width, int height) {
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
        rotationAngle += angle;
    }

    public abstract void draw(Graphics2D g2d);

    public boolean contains(Point p, double zoomFactor) {
        AffineTransform inverseTransform = AffineTransform.getRotateInstance(
            -Math.toRadians(rotationAngle), (x + width / 2.0) / zoomFactor, (y + height / 2.0) / zoomFactor);
        Point2D.Double src = new Point2D.Double(p.x / zoomFactor, p.y / zoomFactor);
        Point2D.Double dst = new Point2D.Double();
        inverseTransform.transform(src, dst);

        Rectangle zoomedRect = new Rectangle(
            (int) (x / zoomFactor),
            (int) (y / zoomFactor),
            (int) (width / zoomFactor),
            (int) (height / zoomFactor));

        return zoomedRect.contains(dst);
    }

    public Point getReferencePoint() {
        return new Point(x, y);
    }

    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void resize(int newX, int newY) {
        this.width = newX - x;
        this.height = newY - y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }
}
