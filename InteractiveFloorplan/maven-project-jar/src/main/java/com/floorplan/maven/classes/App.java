package com.floorplan.maven.classes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    private final DrawingArea drawingArea = new DrawingArea();
    private ElementType currentElement = ElementType.WALL; // Default to wall drawing mode
    private double zoomFactor = 1.0;

    public App() {
        initUI();
        // Replace createDesignPalette with createMainPanel to include left, right, and top components
        add(createMainPanel());
    }


    private void initUI() {
        add(drawingArea, BorderLayout.CENTER);
        setTitle("Interactive Floor Plan Designer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }
 // Main panel with BorderLayout to include left, right, and top toolbars
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left tools panel
        JPanel leftPanel = createDesignPalette();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right tools panel
        JPanel rightPanel = createRightToolsPalette();
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Top toolbar
        JToolBar topToolBar = createTopToolBar();
        mainPanel.add(topToolBar, BorderLayout.NORTH);

        // Integrating drawingArea in the center
        mainPanel.add(drawingArea, BorderLayout.CENTER);

        return mainPanel;
    }

    // Method for left tools panel (your original method)
    private JPanel createDesignPalette() {
        JPanel palette = new JPanel(new GridLayout(0, 1)); // Single column layout
        // Add your buttons here...
        // Example:
        JButton wallButton = new JButton("Wall");
        wallButton.addActionListener(e -> currentElement = ElementType.WALL);
        palette.add(wallButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> currentElement = ElementType.DELETE);
        palette.add(deleteButton);

        JButton circleButton = new JButton("Circle");
        circleButton.addActionListener(e -> currentElement = ElementType.CIRCLE);
        palette.add(circleButton);
        
        JButton smallWallButton = new JButton("Small Wall");
        smallWallButton.addActionListener(e -> currentElement = ElementType.SMALL_WALL);
        palette.add(smallWallButton);

        JButton mediumWallButton = new JButton("Medium Wall");
        mediumWallButton.addActionListener(e -> currentElement = ElementType.MEDIUM_WALL);
        palette.add(mediumWallButton);

        JButton largeWallButton = new JButton("Large Wall");
        largeWallButton.addActionListener(e -> currentElement = ElementType.LARGE_WALL);
        palette.add(largeWallButton);
        
        JButton smallVerticalWallButton = new JButton("Small Vertical Wall");
        smallVerticalWallButton.addActionListener(e -> currentElement = ElementType.VERTICAL_SMALL_WALL);
        palette.add(smallVerticalWallButton);

        JButton mediumVerticalWallButton = new JButton("Medium Vertical Wall");
        mediumVerticalWallButton.addActionListener(e -> currentElement = ElementType.VERTICAL_MEDIUM_WALL);
        palette.add(mediumVerticalWallButton);

        JButton largeVerticalWallButton = new JButton("Large Vertical Wall");
        largeVerticalWallButton.addActionListener(e -> currentElement = ElementType.VERTICAL_LARGE_WALL);
        palette.add(largeVerticalWallButton);
        
        JButton selectorButton = new JButton("Move");
        selectorButton.addActionListener(e -> currentElement = ElementType.MOVE);
        palette.add(selectorButton);
        
        JButton triangleButton = new JButton("Triangle");
        triangleButton.addActionListener(e -> currentElement = ElementType.TRIANGLE);
        palette.add(triangleButton);
        // Add more buttons...
        JButton rotateButton = new JButton("Rotate");
        rotateButton.addActionListener(e -> currentElement = ElementType.ROTATE);
        palette.add(rotateButton);
        return palette;
    }

    // New method for right tools panel, similar to createDesignPalette
    private JPanel createRightToolsPalette() {
        JPanel palette = new JPanel(new GridLayout(0, 1)); // Single column layout
        // Add buttons here...
        // Example:
        JButton doorButton = new JButton("Door");
        doorButton.addActionListener(e -> currentElement = ElementType.DOOR);
        palette.add(doorButton);
        // Add more buttons...

        return palette;
    }

    // Method for creating the top toolbar
    private JToolBar createTopToolBar() {
        JToolBar toolBar = new JToolBar();

        // Example of adding a button to the toolbar
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Action for saving the design
        });
        toolBar.add(saveButton);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            // Action for loading a design
        });
        toolBar.add(loadButton);

        // Add more buttons as needed...

        return toolBar;
    }

   
    class DrawingArea extends JPanel {
        private List<Shape> shapes = new ArrayList<>();
        private Point startPoint = null;
        private Rectangle selectionRect = null;
        private Shape selectedShape = null; // Variable to hold the selected shape
        private Point dragOffset = null; // Track the offset from the initial click point
        private double translateX = 0;
        private double translateY = 0;
        private Point initialClickPoint = null;
        public DrawingArea() {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            setBackground(Color.WHITE);
            addMouseWheelListener(new MouseWheelListener() {
                
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double delta = 0.05f * e.getPreciseWheelRotation();
                    double zoomFactorOld = zoomFactor;
                    zoomFactor -= delta;
                    zoomFactor = Math.max(zoomFactor, 0.1); // Prevent zooming too far out

                    double zoomDivisor = zoomFactor / zoomFactorOld;

                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    // Adjust the translation based on the zoom change
                    translateX += (mouseX - translateX) * (1 - zoomDivisor);
                    translateY += (mouseY - translateY) * (1 - zoomDivisor);

                    repaint(); // Repaint to apply the zoom and translation
                }

            });


            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Adjust mouse coordinates by the current zoom factor
                	  int x = (int) ((e.getX() - translateX) / zoomFactor);
                	    int y = (int) ((e.getY() - translateY) / zoomFactor);
                    startPoint = new Point(x, y);
                    
                    
                    System.out.println("Mouse Pressed at: " + startPoint + " with currentElement: " + currentElement); // Debugging print
                    if (currentElement == ElementType.ROTATE) {
                        selectedShape = findShapeAtPoint(new Point(e.getX(), e.getY()));
                        if (selectedShape != null) {
                            // Setup for rotation, like recording the initial click position
                            initialClickPoint = new Point(e.getX(), e.getY());
                        }
                    }
                    if (currentElement == ElementType.MOVE) {
                        selectedShape = findShapeAtPoint(new Point(x, y)); // Use adjusted x, y for finding the shape
                        if (selectedShape != null) {
                            // Assume getReferencePoint() gives you the top-left point or some logical "handle" point of the shape
                            Point refPoint = selectedShape.getReferencePoint();

                            // dragOffset is the difference between where you clicked and the reference point of the shape
                            dragOffset = new Point(x - refPoint.x, y - refPoint.y);
                        }
                    }
                    	else {
                        switch (currentElement) {
                            case SMALL_WALL:
                                shapes.add(new Wall(x, y, x + 50, y, 4)); // Adjusted for zoom
                                break;
                            case MEDIUM_WALL:
                                shapes.add(new Wall(x, y, x + 100, y, 4)); // Adjusted for zoom
                                break;
                            case LARGE_WALL:
                                shapes.add(new Wall(x, y, x + 150, y, 4)); // Adjusted for zoom
                                break;
                            case WALL:
                                shapes.add(new Wall(x, y, x, y, 4)); // Adjusted for zoom, start a new resizable wall
                                break;
                            case CIRCLE:
                                shapes.add(new Circle(x, y, 0)); // Adjusted for zoom, start a new circle
                                break;
                            case DELETE:
                                selectionRect = new Rectangle(x, y, 0, 0); // Adjusted for zoom
                                break;
                            case VERTICAL_SMALL_WALL:
                                shapes.add(new Wall(x, y, x, y + 50, 4)); // Adjusted for zoom, 50 pixels high for small vertical wall
                                break;
                            case VERTICAL_MEDIUM_WALL:
                                shapes.add(new Wall(x, y, x, y + 100, 4)); // Adjusted for zoom, 100 pixels high for medium vertical wall
                                break;
                            case VERTICAL_LARGE_WALL:
                                shapes.add(new Wall(x, y, x, y + 150, 4)); // Adjusted for zoom, 150 pixels high for large vertical wall
                                break;
                            case TRIANGLE:
                                shapes.add(new Triangle(x, y, 0)); // Adjusted for zoom, start a new triangle
                                break;  
                            default:
                                break;
                        }
                    }
                    repaint();
                }


                @Override
                public void mouseReleased(MouseEvent e) {
                    if (currentElement == ElementType.DELETE && selectionRect != null) {
                        shapes.removeIf(shape -> shape instanceof Wall && selectionRect.intersectsLine(((Wall) shape).x1, ((Wall) shape).y1, ((Wall) shape).x2, ((Wall) shape).y2));
                        shapes.removeIf(shape -> shape instanceof Circle && selectionRect.contains(((Circle) shape).x, ((Circle) shape).y));
                        shapes.removeIf(shape -> shape instanceof Triangle && selectionRect.contains(((Triangle) shape).x, ((Triangle) shape).y));

                        selectionRect = null;
                        repaint();
                    }
                    startPoint = null;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // Adjust mouse coordinates by the current zoom factor and translation for consistent usage
                    int x = (int) ((e.getX() - translateX) / zoomFactor);
                    int y = (int) ((e.getY() - translateY) / zoomFactor);
                    if (currentElement == ElementType.ROTATE && selectedShape != null && initialClickPoint != null) {
                        // Calculate the rotation amount based on mouse movement
                        Point currentPoint = new Point(x, y);
                        double rotationAmount = calculateRotationAmount(initialClickPoint, currentPoint, selectedShape.getReferencePoint());
                        selectedShape.rotate(rotationAmount);

                        initialClickPoint = currentPoint; // Update initial point for continuous rotation
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
                    } else if (currentElement == ElementType.WALL && startPoint != null) {
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

                }


            });

        }
        private Shape findShapeAtPoint(Point point) {
            for (int i = shapes.size() - 1; i >= 0; i--) { // Iterate backwards to get the topmost shape first
                Shape shape = shapes.get(i);
                if (shape.contains(point, zoomFactor)) {
                    return shape; // Return the first shape that contains the point
                }
            }
            return null; // No shape found at the point
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Apply translation
            g2d.translate(translateX, translateY);

            // Then apply zoom
            g2d.scale(zoomFactor, zoomFactor);

            // Set the color for the grid
            g2d.setColor(Color.LIGHT_GRAY);

            // Determine the size of each cell in the grid
            int gridSize = 25;

            // Calculate the bounds of the visible area considering translation and zoom
            int visibleLeft = (int) (-translateX / zoomFactor);
            int visibleTop = (int) (-translateY / zoomFactor);
            int visibleRight = (int) ((getWidth() - translateX) / zoomFactor);
            int visibleBottom = (int) ((getHeight() - translateY) / zoomFactor);

            // Draw the vertical lines of the grid over the visible area
            for (int i = visibleLeft - (visibleLeft % gridSize); i <= visibleRight; i += gridSize) {
                g2d.drawLine(i, visibleTop, i, visibleBottom);
            }

            // Draw the horizontal lines of the grid over the visible area
            for (int i = visibleTop - (visibleTop % gridSize); i <= visibleBottom; i += gridSize) {
                g2d.drawLine(visibleLeft, i, visibleRight, i);
            }

            // Now draw the shapes on top of the grid as before
            for (Shape shape : shapes) {
                if (shape instanceof Wall) {
                    ((Wall) shape).draw(g2d, zoomFactor );
                } else if (shape instanceof Circle) {
                    ((Circle) shape).draw(g2d);
                }
                if (shape instanceof Triangle) {
                    ((Triangle) shape).draw(g2d);
                }
            }

            // Draw the selection rectangle if it's not null
            if (selectionRect != null) {
                g2d.setColor(Color.BLUE);
                g2d.draw(selectionRect);
            }
        }
    }
    private double calculateRotationAmount(Point initialPoint, Point currentPoint, Point shapeCenter) {
        // Calculate angle between initial click and current point relative to shape center
        double initialAngle = Math.atan2(initialPoint.y - shapeCenter.y, initialPoint.x - shapeCenter.x);
        double currentAngle = Math.atan2(currentPoint.y - shapeCenter.y, currentPoint.x - shapeCenter.x);
        // Return the change in angle in degrees
        return Math.toDegrees(currentAngle - initialAngle);
    }
}

