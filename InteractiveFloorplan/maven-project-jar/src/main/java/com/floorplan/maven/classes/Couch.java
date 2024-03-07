package com.floorplan.maven.classes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Couch extends FurnitureItem {

    public Couch(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.color = new Color(169, 169, 169); // A shade of dark gray

    }

    
    @Override
    public void draw(Graphics2D g2d) {
    	   AffineTransform originalTransform = g2d.getTransform();

           // Apply rotation around the stair's center
           AffineTransform transform = AffineTransform.getRotateInstance(
               Math.toRadians(rotationAngle), x + width / 2.0, y + height / 2.0);
           g2d.transform(transform);

           g2d.setColor(color); // Set the color for the stairs

           // Calculate the width and height for each step
           int stepWidth = width / 3;
           int stepHeight = height / 3;

           // Draw each step as a rectangle, slightly offset to create an ascending effect
           for (int i = 0; i < 3; i++) {
               // Each step is a rectangle that is progressively smaller and offset to create depth
               Rectangle2D.Double step = new Rectangle2D.Double(
                   x + i * stepWidth / 2.0, 
                   y + i * stepHeight / 2.0, 
                   width - i * stepWidth, 
                   height - i * stepHeight
               );
               g2d.draw(step);
           }

           // Restore the original transform to not affect subsequent drawing
           g2d.setTransform(originalTransform);
       }
    

    private void drawLeg(Graphics2D g2d, double x, double y, double height) {
        // Slightly curved leg for a more refined look
        Path2D.Double leg = new Path2D.Double();
        leg.moveTo(x, y);
        leg.curveTo(x - 5, y + height * 0.5, x + 5, y + height * 0.5, x, y + height);
        g2d.draw(leg);
    }
}
