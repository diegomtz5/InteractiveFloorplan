package com.floorplan.maven.classes;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class App extends JFrame implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private final DrawingArea drawingArea = new DrawingArea();

    public App() {
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PanelUtil panelUtil = new PanelUtil(this, drawingArea, 1, ElementType.WALL);
        add(panelUtil.createMainPanel());
        pack(); // You might also experiment with removing this line to see if it affects the behavior.
        setResizable(true);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
  
    class DrawingArea extends JPanel {
        private static final long serialVersionUID = 1L; // Recommended for Serializable classes
        private ElementType currentElement = ElementType.WALL; // Default to wall drawing mode
        private double zoomFactor = 1.0;
        private Integer thickness =  1;
        private Shape selectedShape = null; // For holding selected shapes

        private List<Shape> shapes = new ArrayList<>();
        private Point startPoint = null;
        private Rectangle selectionRect = null;
        private Point dragOffset = null; // Track the offset from the initial click point
        private double translateX = 0;
        private double translateY = 0;
        private Point initialClickPoint = null;
        private boolean resizing = false; // Flag to indicate a resize operation is in progress
        private Shape resizingShape = null; // The shape being resized
        private Point resizeStartPoint = null; // The 
        private CompositeShape selectedComposite = null; // For holding selected shapes

        
        public DrawingArea() {
            initializeListeners();
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            setBackground(Color.WHITE);
        }

        private void initializeListeners() {
            addMouseWheelListener(this::handleMouseWheel);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleMousePressed(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    handleMouseReleased(e);
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    handleMouseMoved(e);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    handleMouseDragged(e);
                }
            });
        }

        public void clear() {
       	 shapes.clear(); // Clear the list of shapes
            repaint(); // Repaint to update the display
       }
       public List<Shape> getShapes() {
			return shapes;
		}
       public void setCurrentElement(ElementType elementType) {
    	    this.currentElement = elementType;
    	}

	public void addShapes(List<Shape> newShapes) {
       	   shapes.addAll(newShapes); // Add all new shapes to the list
              repaint(); // Repaint to update the display
       }
	  public void setThickness(int thickness) {
	        this.thickness = thickness; // Update the thickness value used for drawing
	    }

	private Shape findShapeAtPoint(Point point) {
	    // Check if there's a selected composite and the click is inside its bounds
	    if (selectedComposite != null && selectedComposite.contains(point, zoomFactor)) {
	        return selectedComposite; // Return the selected composite as it contains the click point
	    }

	    // If no selected composite contains the point, search through all shapes
	    for (int i = shapes.size() - 1; i >= 0; i--) {
	        Shape shape = shapes.get(i);

	        // Check if the shape contains the point, considering the zoom factor
	        if (shape.contains(point, zoomFactor)) {
	            return shape; // Return the shape, which could be an individual shape or a composite
	        }
	    }
	    return null; // No shape found at the point
	}
   @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            applyTransformations(g2d);
            drawGrid(g2d);

            // Draw all shapes if there's no active selection, or just the selected composite if there is
                drawShapes(g2d); // Draw all individual shapes
        

            drawSelectionIndicatorIfNeeded(g2d);
            drawMeasurements(g2d);
            g2d.setTransform(new AffineTransform()); // Reset transformations after drawing

            drawRuler(g2d);
        }

        private void handleMouseWheel(MouseWheelEvent e) {
            // Your existing zoom logic
      	    double delta = 0.05f * e.getPreciseWheelRotation();
    	    
    	    // Store the old zoom factor for later calculations
    	    double zoomFactorOld = zoomFactor;

    	    // Adjust the zoom factor, ensuring it doesn't go below a minimum level
    	    zoomFactor -= delta;
    	    zoomFactor = Math.max(zoomFactor, 0.1); // Prevent zooming too far out

    	    // Calculate the ratio of the new zoom to the old zoom
    	    double zoomDivisor = zoomFactor / zoomFactorOld;

    	    // Get the mouse's position within the component
    	    int mouseX = e.getX();
    	    int mouseY = e.getY();

    	    // Adjust the translation so the point under the mouse stays stationary
    	    translateX += (mouseX - translateX) * (1 - zoomDivisor);
    	    translateY += (mouseY - translateY) * (1 - zoomDivisor);

    	    // Request a repaint of the component to apply the new zoom and translation
    	    repaint();
    	
        }
        private void handleMousePressed(MouseEvent e) {
            // Simplified version of your mousePressed logic
            int x = (int) ((e.getX() - translateX) / zoomFactor);
            int y = (int) ((e.getY() - translateY) / zoomFactor);
            startPoint = new Point(x, y);

            System.out.println("Mouse Pressed at: " + startPoint + " with currentElement: " + currentElement); // Debugging print


            if (currentElement == ElementType.ROTATE && selectedShape != null) {
                // For rotation, the initial click point is crucial
                initialClickPoint = startPoint; // Use adjusted startPoint
            }
            
            if (currentElement == ElementType.SELECT) {
                	selectedComposite = new CompositeShape();
                
                selectionRect = new Rectangle(startPoint); // Initialize selection rectangle
            } 

            Shape shapeUnderMouse = findShapeAtPoint(new Point(x, y));
            if (shapeUnderMouse != null && currentElement == ElementType.SELECT) {
                // Add shape to composite only if a shape is found and in SELECT mode
                selectedComposite.add(shapeUnderMouse);
            }
            // Check if the click is on a resize handle using the adjusted point
            if (selectedShape != null && isClickOnHandle(new Point(x, y), selectedShape, selectedShape.getRotationAngle())) {
                resizing = true;
                resizingShape = selectedShape;
                resizeStartPoint = new Point(x, y); // Use adjusted coordinates
            }
            if (currentElement == ElementType.MOVE && shapeUnderMouse != null) {
                // Here, you don't need to know if it's a composite or not. You just work with it as a shape.
                selectedShape = shapeUnderMouse; // This will hold any Shape, including CompositeShape.

                // Get the reference point from the shape, which might be the composite's reference or an individual shape's.
                Point refPoint = selectedShape.getReferencePoint();

                // Calculate the drag offset.
                dragOffset = new Point(x - refPoint.x, y - refPoint.y);
            }
            	else {
                switch (currentElement) {
                    case SMALL_WALL:
                        shapes.add(new Wall(x, y, x + 100, y, 4, Color.BLACK)); // Adjusted for zoom
                        break;
                    case MEDIUM_WALL:
                        shapes.add(new Wall(x, y, x + 200, y, 4, Color.BLACK)); // Adjusted for zoom
                        break;
                    case LARGE_WALL:
                        shapes.add(new Wall(x, y, x + 300, y, 4, Color.BLACK)); // Adjusted for zoom
                        break;
                    case WALL:
                        shapes.add(new Wall(x, y, x, y, 4, Color.BLACK)); // Adjusted for zoom, start a new resizable wall
                        break;
                    case OPENING:
                        shapes.add(new Wall(x, y, x + 50, y, 8, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
                        break;
                    case VERTICAL_OPENING:
                        shapes.add(new Wall(x, y, x, y + 50, 8, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
                        break;
                    case OPENING_CUSTOM:
                        shapes.add(new Wall(x, y, x, y, 8, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
                        break;
                    case CIRCLE:
                        shapes.add(new Circle(x, y, 0, thickness)); // Adjusted for zoom, start a new circle
                        break;
                    case DELETE:
                        selectionRect = new Rectangle(x, y, 0, 0); // Adjusted for zoom
                        break;
                    case VERTICAL_SMALL_WALL:
                        shapes.add(new Wall(x, y, x, y + 100, 4, Color.BLACK)); // Adjusted for zoom, 50 pixels high for small vertical wall
                        break;
                    case VERTICAL_MEDIUM_WALL:
                        shapes.add(new Wall(x, y, x, y + 200, 4, Color.BLACK)); // Adjusted for zoom, 100 pixels high for medium vertical wall
                        break;
                    case VERTICAL_LARGE_WALL:
                        shapes.add(new Wall(x, y, x, y + 300, 4, Color.BLACK)); // Adjusted for zoom, 150 pixels high for large vertical wall
                        break;
                    case TRIANGLE:
                        shapes.add(new Triangle(x, y, 0, thickness)); // Adjusted for zoom, start a new triangle
                        break;  
                    case RECTANGLE:
                        shapes.add(new RectangleShape(x, y, 0, 0, thickness)); // Adjusted for zoom, start a new triangle
                        break;  
                    case ROOM:
                        shapes.add(new RectangleShape(x, y, 0, 0, 4)); // Adjusted for zoom, start a new triangle
                        break;  
                    case TABLE:
                    	shapes.add(new Table(x,y,70,70));
                    	break;
                    case BED:
                    	shapes.add(new Bed(x,y,100,120));
                    	break;
                    case DOOR:
                    	shapes.add(new Door(x,y,55,40));
                    	break;
                    case DOORREVERSE:
                    	shapes.add(new ReverseDoor(x,y,55,40));
                    	break;
                    case TOILET:
                    	shapes.add(new Toilet(x,y,40,75));
                    	break;
                    case BATHTUB:
                    	shapes.add(new Bathtub(x,y,50,85));
                    	break;
                    case SINK:
                    	shapes.add(new Sink(x,y,40,50));
                    	break;
                    case STOVE:
                    	shapes.add(new Stove(x,y, 70,60));
                    	break;
                    case FRIDGE:
                    	shapes.add(new Fridge(x,y,60,70));
                    	break;
                    case SHOWER:
                    	shapes.add(new Shower(x,y,60,70));
                    	break;
                    case LINE:
                        shapes.add(new Wall(x, y, x, y, thickness, Color.BLACK)); // Adjusted for zoom, start a new resizable wall
                        break;
                    default:
                        break;
                }
            }
            repaint();
        }

        private void handleMouseReleased(MouseEvent e) {
            // Simplified version of your mouseReleased logic
        	 if (resizing) {
                 resizing = false;
                 resizingShape = null;
                 resizeStartPoint = null;
             }
             if (currentElement == ElementType.SELECT && selectionRect != null) {
            	 selectedComposite = new CompositeShape();
                 for (Shape shape : shapes) {
                     if (selectionRect.intersects(shape.getBounds())) {
                         selectedComposite.add(shape);
                     }
                 }
                 selectedShape = selectedComposite;
                 selectionRect = null; // Reset selection rectangle
                 repaint();
             }
             if (currentElement == ElementType.DELETE && selectionRect != null) {
                 shapes.removeIf(shape -> shape instanceof Wall && selectionRect.intersectsLine(((Wall) shape).x1, ((Wall) shape).y1, ((Wall) shape).x2, ((Wall) shape).y2));
                 shapes.removeIf(shape -> shape instanceof Circle && selectionRect.contains(((Circle) shape).x, ((Circle) shape).y));
                 shapes.removeIf(shape -> shape instanceof Triangle && selectionRect.contains(((Triangle) shape).x, ((Triangle) shape).y));
                 shapes.removeIf(shape -> shape instanceof RectangleShape && selectionRect.contains(((RectangleShape) shape).x, ((RectangleShape) shape).y));
                 shapes.removeIf(shape -> shape instanceof FurnitureItem && selectionRect.contains(((FurnitureItem) shape).x, ((FurnitureItem) shape).y));
                 selectedComposite = null;
                 selectedShape = null;
                 selectionRect = null;
                 repaint();
             }
             else if (startPoint != null) {
                 // This is where a shape was just added
                 // Switch to MOVE mode after adding a shape
                currentElement = ElementType.MOVE;
             }
             startPoint = null;
        }

        private void handleMouseMoved(MouseEvent e) {
            // Simplified version of your mouseMoved logic
        	   if (selectedShape != null) {
   	            Cursor newCursor = getCursorForHandle(e.getPoint(), selectedShape, selectedShape.getRotationAngle());
   	            setCursor(newCursor);
   	        } else {
   	            setCursor(Cursor.getDefaultCursor()); // Reset to default cursor when not over a handle
   	        }
        }

        private void handleMouseDragged(MouseEvent e) {
            // Simplified version of your mouseDragged logic
            int x = (int) ((e.getX() - translateX) / zoomFactor);
            int y = (int) ((e.getY() - translateY) / zoomFactor);
            if (currentElement == ElementType.SELECT && selectionRect != null) {
                selectionRect.setBounds(Math.min(startPoint.x, e.getX()), Math.min(startPoint.y, e.getY()),
                                        Math.abs(e.getX() - startPoint.x), Math.abs(e.getY() - startPoint.y));
                repaint();
            }
            if (currentElement == ElementType.ROTATE && selectedShape != null && initialClickPoint != null) {
                // Calculate the rotation amount based on mouse movement
                Point currentPoint = new Point(x, y);
                double rotationAmount = calculateRotationAmount(initialClickPoint, currentPoint, selectedShape.getReferencePoint());
                selectedShape.rotate(rotationAmount);

                initialClickPoint = currentPoint; // Update initial point for continuous rotation
                repaint();
            } else if (((currentElement == ElementType.RECTANGLE)||(currentElement == ElementType.ROOM))  && startPoint != null && !shapes.isEmpty()) {
                // Get the last shape added, which should be the rectangle being drawn
                Shape lastShape = shapes.get(shapes.size() - 1);

                if (lastShape instanceof RectangleShape) {
                    RectangleShape rect = (RectangleShape) lastShape;

                    // Calculate new width and height based on drag distance
                    int newWidth = Math.abs(x - startPoint.x);
                    int newHeight = Math.abs(y - startPoint.y);

                    // Update the rectangle's dimensions
                    rect.setDimensions(newWidth, newHeight);
                }
                repaint();
            }
            else if (currentElement == ElementType.DELETE && selectionRect != null) {
                // Use adjusted startPoint for consistent width and height calculation
                int width = Math.abs(x - startPoint.x);
                int height = Math.abs(y - startPoint.y);
                selectionRect.setBounds(startPoint.x, startPoint.y, width, height);
                repaint();
            } else if (currentElement == ElementType.CIRCLE && startPoint != null) {
                Circle lastCircle = (Circle) shapes.get(shapes.size() - 1);
                // Calculate the radius based on the distance between startPoint and currentPoint
                lastCircle.setRadius((int) startPoint.distance(x, y));
                repaint();
            } else if ((currentElement == ElementType.WALL || currentElement == ElementType.LINE)  && startPoint != null) {
                Wall lastWall = (Wall) shapes.get(shapes.size() - 1);

                // Adjust the mouse event coordinates for zoom and translation
                int adjustedX = (int) ((e.getX() - translateX) / zoomFactor);
                int adjustedY = (int) ((e.getY() - translateY) / zoomFactor);

                lastWall.x2 = adjustedX;
                lastWall.y2 = adjustedY;
                repaint();
            }
            else if (currentElement == ElementType.OPENING_CUSTOM && startPoint != null) {
            	Wall lastWall = (Wall) shapes.get(shapes.size() - 1);

                // Adjust the mouse event coordinates for zoom and translation
                int adjustedX = (int) ((e.getX() - translateX) / zoomFactor);
                int adjustedY = (int) ((e.getY() - translateY) / zoomFactor);

                lastWall.x2 = adjustedX;
                lastWall.y2 = adjustedY;
                repaint();
            }

           	else if (currentElement == ElementType.TRIANGLE && startPoint != null) {
					Triangle lastTriangle = (Triangle) shapes.get(shapes.size() - 1);
                // Calculate the side length based on the distance between startPoint and currentPoint
                lastTriangle.setSide((int) startPoint.distance(x, y));
                repaint();
            }

            if (currentElement == ElementType.MOVE && selectedShape != null && dragOffset != null) {
                // First, adjust the mouse event coordinates for zoom and translation to get the "world" coordinates
                int mouseXAdjusted = (int) ((e.getX() - translateX) / zoomFactor);
                int mouseYAdjusted = (int) ((e.getY() - translateY) / zoomFactor);

                // Then, apply the dragOffset to these adjusted coordinates to get the new position for the shape
                int newX = mouseXAdjusted - dragOffset.x;
                int newY = mouseYAdjusted - dragOffset.y;

                // Move the selected shape to this new position
                selectedShape.moveTo(newX, newY);
                repaint();
            }
            if (resizing && resizingShape != null) {
                // Calculate new size based on drag distance
                // This is a simplification, actual calculation depends on the shape and which handle is being dragged
                int deltaX = e.getX() - resizeStartPoint.x;
                int deltaY = e.getY() - resizeStartPoint.y;

                // Apply the resize to the shape
                // This method would need to be implemented for each shape type
                resizeShape(resizingShape, resizeStartPoint, new Point(e.getX(), e.getY()), resizingShape.getRotationAngle());
                repaint();
            }
        }
        private void drawMeasurements(Graphics2D g2d) {
            if (selectedShape instanceof Wall) {
                drawWallMeasurements(g2d, (Wall) selectedShape);
            } else if (selectedShape instanceof RectangleShape) {
                drawRectangleMeasurements(g2d, (RectangleShape) selectedShape);
            }
            else if (selectedShape instanceof CompositeShape) {
                drawCompositeMeasurements(g2d, (CompositeShape) selectedShape);
            }
            // Add more else if blocks for other shape types if needed
        }

        private void drawWallMeasurements(Graphics2D g2d, Wall wall) {
            double length = calculateWallLength(wall);
            String lengthText = String.format("%.2f feet", length / 20); // Assuming 20 pixels = 1 foot for this example

            int midX = (wall.x1 + wall.x2) / 2;
            int midY = (wall.y1 + wall.y2) / 2;

            midX = (int) (midX * zoomFactor + translateX);
            midY = (int) (midY * zoomFactor + translateY);

            g2d.setColor(Color.RED);
            g2d.drawString(lengthText, midX, midY);
        }

        private void drawRectangleMeasurements(Graphics2D g2d, RectangleShape rectangle) {
            String widthText = String.format("%.2f feet", (rectangle.width / 20.0));
            String heightText = String.format("%.2f feet", (rectangle.height / 20.0));

            int midX = rectangle.x + rectangle.width / 2;
            int midY = rectangle.y + rectangle.height / 2;

            midX = (int) (midX * zoomFactor + translateX);
            midY = (int) (midY * zoomFactor + translateY);

            g2d.setColor(Color.RED);
            g2d.drawString(widthText, midX, rectangle.y - 5); // Top side
            g2d.drawString(heightText, rectangle.x - 40, midY); // Left side
        }
        private void drawCompositeMeasurements(Graphics2D g2d, CompositeShape composite) {
            Rectangle bounds = composite.getBounds(); // Get the bounding rectangle of the composite

            // Calculate the center position for the width and height annotations
            int midX = bounds.x + bounds.width / 2;
            int midY = bounds.y + bounds.height / 2;

            // Adjust for zoom and translation
            midX = (int) (midX * zoomFactor + translateX);
            midY = (int) (midY * zoomFactor + translateY);

            String widthText = String.format("%.2f feet", bounds.width / 20.0); // Assuming 20 pixels = 1 unit for this example
            String heightText = String.format("%.2f feet", bounds.height / 20.0);

            g2d.setColor(Color.RED); // Set color for measurement annotations

            // Draw width measurement text
            g2d.drawString(widthText, midX - g2d.getFontMetrics().stringWidth(widthText) / 2, bounds.y - 5);
            
            // Draw height measurement text
            g2d.drawString(heightText, bounds.x - g2d.getFontMetrics().stringWidth(heightText) - 5, midY + g2d.getFontMetrics().getHeight() / 2);
        }

        private void applyTransformations(Graphics2D g2d) {
            // Apply translation and then zoom
            g2d.translate(translateX, translateY);
            g2d.scale(zoomFactor, zoomFactor);
        }

        private void drawGrid(Graphics2D g2d) {
            g2d.setColor(Color.LIGHT_GRAY);
            int gridSize = 25;
            int visibleLeft = (int) (-translateX / zoomFactor);
            int visibleTop = (int) (-translateY / zoomFactor);
            int visibleRight = (int) ((getWidth() - translateX) / zoomFactor);
            int visibleBottom = (int) ((getHeight() - translateY) / zoomFactor);

            // Vertical lines
            for (int i = visibleLeft - (visibleLeft % gridSize); i <= visibleRight; i += gridSize) {
                g2d.drawLine(i, visibleTop, i, visibleBottom);
            }
            // Horizontal lines
            for (int i = visibleTop - (visibleTop % gridSize); i <= visibleBottom; i += gridSize) {
                g2d.drawLine(visibleLeft, i, visibleRight, i);
            }
        }

        private void drawShapes(Graphics2D g2d) {
            for (Shape shape : shapes) {
                shape.draw(g2d); // Assuming each shape knows how to draw itself
            }
        }

        private void drawSelectionIndicatorIfNeeded(Graphics2D g2d) {
            if (selectedShape != null) {
                double rotationAngle = selectedShape.getRotationAngle();
                drawSelectionIndicator(g2d, selectedShape, rotationAngle);
            }
            if (selectionRect != null) {
                g2d.setColor(Color.BLUE);
                g2d.draw(selectionRect);
            }
        }

        
        private void drawSelectionIndicator(Graphics2D g2d, Shape selectedShape, double angle) {
            // Scale the handle size based on the zoom factor
            int handleSize = (int) (6 * zoomFactor); // Adjust the base handle size as needed

            Rectangle originalBounds = (Rectangle) selectedShape.getBounds();
            if (originalBounds == null) {
                // Optionally, handle the case where there are no shapes (e.g., skip drawing or draw a default indicator)
                return; // Skip drawing the selection indicator
            }
            // Calculate the top-left corner of the transformed (zoomed and translated) shape
            int x = originalBounds.x;
            int y = originalBounds.y;
            int width = originalBounds.width;
            int height = originalBounds.height;


            // Calculate center of the transformed shape for rotation
            int centerX = x + width / 2;
            int centerY = y + height / 2;

            // Define handle points on the corners of the transformed shape
            Point[] handlePoints = {
                new Point(x, y), // Top-left
                new Point(x + width, y), // Top-right
                new Point(x + width, y + height), // Bottom-right
                new Point(x, y + height), // Bottom-left
                // Add midpoints if needed
            };

            g2d.setColor(Color.BLUE); // Handle color

            for (Point point : handlePoints) {
                // Rotate each point around the center of the transformed shape
                Point rotatedPoint = rotatePoint(point, new Point(centerX, centerY), angle);

                // Draw handle at the rotated position, adjusting to center the handle on its point
                g2d.fillRect(rotatedPoint.x - handleSize / 2, rotatedPoint.y - handleSize / 2, handleSize, handleSize);
            }
        }


        private Point rotatePoint(Point point, Point pivot, double angleDegrees) {
            double radians = Math.toRadians(angleDegrees);
            double sin = Math.sin(radians);
            double cos = Math.cos(radians);

            // Translate point to origin
            Point translated = new Point(point.x - pivot.x, point.y - pivot.y);

            // Rotate point
            int xNew = (int) (translated.x * cos - translated.y * sin);
            int yNew = (int) (translated.x * sin + translated.y * cos);

            // Translate point back
            return new Point(xNew + pivot.x, yNew + pivot.y);
        }

    
    private double calculateRotationAmount(Point initialPoint, Point currentPoint, Point shapeCenter) {
        // Calculate angle between initial click and current point relative to shape center
        double initialAngle = Math.atan2(initialPoint.y - shapeCenter.y, initialPoint.x - shapeCenter.x);
        double currentAngle = Math.atan2(currentPoint.y - shapeCenter.y, currentPoint.x - shapeCenter.x);
        // Return the change in angle in degrees
        return Math.toDegrees(currentAngle - initialAngle);
    }

    private boolean isClickOnHandle(Point clickPoint, Shape selectedShape, double angle) {
        Rectangle bounds = (Rectangle) selectedShape.getBounds();
        if (bounds == null) {
            // Optionally, handle the case where there are no shapes (e.g., skip drawing or draw a default indicator)
            return false; // Skip drawing the selection indicator
        }
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        int handleSize = 6; // Assuming a handle size of 6x6 pixels
        // Adjust click point for zoom and translation
        Point adjustedClickPoint = new Point(
            (int) ((clickPoint.x - translateX) / zoomFactor),
            (int) ((clickPoint.y - translateY) / zoomFactor)
        );
        Point[] points = {
            new Point(bounds.x, bounds.y), // Top-left
            new Point(bounds.x + bounds.width, bounds.y), // Top-right
            new Point(bounds.x + bounds.width, bounds.y + bounds.height), // Bottom-right
            new Point(bounds.x, bounds.y + bounds.height), // Bottom-left
        };

        for (Point point : points) {
            Point rotatedPoint = rotatePoint(point, new Point(centerX, centerY), angle);
            Rectangle handleRect = new Rectangle(
                (int) ((rotatedPoint.x - handleSize / 2) * zoomFactor + translateX),
                (int) ((rotatedPoint.y - handleSize / 2) * zoomFactor + translateY),
                handleSize,
                handleSize
            );

            if (handleRect.contains(adjustedClickPoint)) {
                return true;
            }
        }

        return false;
    }
    private void resizeShape(Shape shape, Point dragStartPoint, Point currentPoint, double angle) {
   

        // Calculate the drag distance
        int dx = currentPoint.x - dragStartPoint.x;
        int dy = currentPoint.y - dragStartPoint.y;

        // Adjust dx and dy based on the zoom factor
        dx /= zoomFactor;
        dy /= zoomFactor;

        // For simplicity, let's assume we're resizing from the bottom-right handle
        // You would need logic here to determine which handle is being dragged and adjust accordingly
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            // Assuming dragging the bottom-right increases the radius
            int newRadius = circle.radius + Math.max(dx, dy); // Simple example, likely needs refinement
            circle.setRadius(Math.max(newRadius, 10)); // Set a minimum size
        } else if (shape instanceof Wall) {
            Wall wall = (Wall) shape;
            // Adjust one endpoint of the wall based on which handle is dragged
            // This is a simplification; you'll need to handle different handles and possibly maintain aspect ratio
            wall.x2 += dx;
            wall.y2 += dy;
        }
        // Similar adjustments would be needed for other shapes like Triangle

        repaint();
    }
    private Cursor getCursorForHandle(Point mousePoint, Shape shape, double angle) {
        Rectangle bounds = (Rectangle) shape.getBounds();
        if (bounds == null) {
            // Optionally, handle the case where there are no shapes (e.g., skip drawing or draw a default indicator)
            return null; // Skip drawing the selection indicator
        }
        int handleSize = 6; // Assuming handle size is 6x6 pixels

        // Define handle points (corners of the bounding box, for example)
        Point[] handlePoints = {
            new Point(bounds.x, bounds.y), // Top-left
            new Point(bounds.x + bounds.width, bounds.y), // Top-right
            new Point(bounds.x + bounds.width, bounds.y + bounds.height), // Bottom-right
            new Point(bounds.x, bounds.y + bounds.height), // Bottom-left
            // Add more points if needed
        };

        for (Point handlePoint : handlePoints) {
            Point rotatedPoint = rotatePoint(handlePoint, new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2), angle);
            
            // Adjust for zoom and translation
            int handleX = (int) ((rotatedPoint.x - handleSize / 2) * zoomFactor + translateX);
            int handleY = (int) ((rotatedPoint.y - handleSize / 2) * zoomFactor + translateY);

            Rectangle handleRect = new Rectangle(handleX, handleY, handleSize, handleSize);
            if (handleRect.contains(mousePoint)) {
                // Return a specific cursor based on which handle the mouse is over
                // This is a simplified example; you might want different cursors for different handles
                return Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
            }
        }

        return Cursor.getDefaultCursor(); // Return the default cursor if the mouse isn't over a handle
    }
    private double calculateWallLength(Wall wall) {
        return Point2D.distance(wall.x1, wall.y1, wall.x2, wall.y2); // Adjust for zoom
    }

    private void drawRuler(Graphics2D g2d) {
        int rulerLengthPixels = 20; // Length of each ruler segment, adjust based on your scale
        int rulerUnits = 10; // Distance between labels on the ruler
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK); // Set the color to black for the outlines
        // Draw top ruler
        for (int i = 0; i < getWidth(); i += rulerLengthPixels) {
            // Draw a small line for each unit
            g2d.drawLine(i, 0, i, 5);

            // Label every 'rulerUnits' units
            if ((i / rulerLengthPixels) % rulerUnits == 0) {
                String label = String.valueOf(i / rulerLengthPixels);
                g2d.drawString(label, i, 15);
            }
        }

        // Draw left ruler
        for (int i = 0; i < getHeight(); i += rulerLengthPixels) {
            // Draw a small line for each unit
            g2d.drawLine(0, i, 5, i);

            // Label every 'rulerUnits' units
            if ((i / rulerLengthPixels) % rulerUnits == 0) {
                String label = String.valueOf(i / rulerLengthPixels);
                g2d.drawString(label, 5, i + 5);
            }
        }
    }

    }
}
