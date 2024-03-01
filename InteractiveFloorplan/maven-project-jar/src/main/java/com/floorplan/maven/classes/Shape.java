package com.floorplan.maven.classes;

import java.awt.Point;

public interface Shape {

	Object getBounds();

	void moveTo(int newX, int newY);

	boolean contains(Point point, double zoomFactor);
	Point getReferencePoint();
	void rotate(double angle);
}
