package com.floorplan.maven.classes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompositeShape implements Shape, Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private List<Shape> children = new ArrayList<>();

    public void add(Shape component) {
        children.add(component);
    }

    public void remove(Shape component) {
        children.remove(component);
    }

    public Shape getChild(int i) {
        return children.get(i);
    }

    public void draw(Graphics2D g2d) {
        for (Shape child : children) {
            child.draw(g2d);
        }
    }
    public List<Shape> getChildren() {
        return new ArrayList<>(children); // Return a copy to protect encapsulation
    }
    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle(0, 0, 0, 0); // Default empty rectangle
        for (Shape child : children) {
            Rectangle childBounds = child.getBounds();
            if (childBounds != null && !childBounds.isEmpty()) {
                if (bounds.isEmpty()) {
                    bounds = new Rectangle(childBounds); // Initialize bounds with the first non-empty child bounds
                } else {
                    bounds.add(childBounds); // Union the current bounds with the child's bounds
                }
            }
        }
        return bounds;
    }


    public void moveTo(int newX, int newY) {
        // Calculate the offset based on the first child's reference point.
        // This assumes all children should move by the same offset.
        if (!children.isEmpty()) {
            Point refPoint = getReferencePoint(); // This could be the top-left point of the bounding rectangle of the composite shape.

            int offsetX = newX - refPoint.x;
            int offsetY = newY - refPoint.y;

            for (Shape child : children) {
                Point childRefPoint = child.getReferencePoint();
                int childNewX = childRefPoint.x + offsetX;
                int childNewY = childRefPoint.y + offsetY;
                child.moveTo(childNewX, childNewY);
            }
        }
    }

    public boolean contains(Point point, double zoomFactor) {
        for (Shape child : children) {
            if (child.contains(point, zoomFactor)) {
                return true;
            }
        }
        return false;
    }

    public Point getReferencePoint() {
        if (!children.isEmpty()) {
            return children.get(0).getReferencePoint();
        }
        return null;
    }

    public void rotate(double angle) {
        for (Shape child : children) {
            child.rotate(angle);
        }
    }

    public void resize(int x, int y) {
        for (Shape child : children) {
            child.resize(x, y);
        }
    }

    public double getRotationAngle() {
        if (!children.isEmpty()) {
            return children.get(0).getRotationAngle();
        }
        return 0;
    }
}
