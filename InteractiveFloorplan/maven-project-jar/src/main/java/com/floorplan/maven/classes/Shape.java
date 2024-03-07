package com.floorplan.maven.classes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public interface Shape  {

    public Rectangle getBounds();

	void moveTo(int newX, int newY);

	boolean contains(Point point, double zoomFactor);
	Point getReferencePoint();
	void rotate(double angle);
	void resize(int x, int y);
	double getRotationAngle();

	public void draw(Graphics2D g2d);
}
