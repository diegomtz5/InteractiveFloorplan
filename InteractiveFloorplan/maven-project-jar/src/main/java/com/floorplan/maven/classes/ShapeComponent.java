package com.floorplan.maven.classes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public interface ShapeComponent {

    public Rectangle getBounds();
    public void moveTo(int newX, int newY);
    public boolean contains(Point point, double zoomFactor);

    public Point getReferencePoint() ;

    public void rotate(double angle);

    public void resize(int x, int y) ;

    public double getRotationAngle();

    public void draw(Graphics2D g2d);
    // Composite-specific methods
    public void add(ShapeComponent component);

    public void remove(ShapeComponent component);

    public ShapeComponent getChild(int i);
}

