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

    public App() {
        initUI();
        add(createDesignPalette(), BorderLayout.WEST); // Add the design palette to the left side
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

    private JPanel createDesignPalette() {
        JPanel palette = new JPanel();
        palette.setLayout(new GridLayout(0, 1)); // Single column layout

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
        selectorButton.addActionListener(e -> currentElement = ElementType.SELECTOR);
        palette.add(selectorButton);
        JButton triangleButton = new JButton("Triangle");
        triangleButton.addActionListener(e -> currentElement = ElementType.SELECTOR);
        palette.add(triangleButton);

        // Add more buttons for other elements like doors, windows, furniture, etc.
        return palette;
    }
    enum ElementType {
        WALL, DOOR, WINDOW, FURNITURE, DELETE, CIRCLE, SMALL_WALL, MEDIUM_WALL, LARGE_WALL, VERTICAL_SMALL_WALL, VERTICAL_MEDIUM_WALL, VERTICAL_LARGE_WALL, SELECTOR, TRIANGLE // Added SELECTOR for the selection tool
    }




    class DrawingArea extends JPanel {
        private List<Shape> shapes = new ArrayList<>();
        private Point startPoint = null;
        private Rectangle selectionRect = null;
        private Shape selectedShape = null; // Variable to hold the selected shape
        private Point dragOffset = null; // Track the offset from the initial click point

        public DrawingArea() {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    startPoint = e.getPoint();
                    System.out.println("Mouse Pressed at: " + startPoint + " with currentElement: " + currentElement); // Debugging print

                    if (currentElement == ElementType.SELECTOR) {
                        // Select a shape if the selector tool is active
                        selectedShape = findShapeAtPoint(startPoint);
                        if (selectedShape != null) {
                            Rectangle bounds = (Rectangle) selectedShape.getBounds();

                            dragOffset = new Point(startPoint.x - bounds.x, startPoint.y - bounds.y);
                        }
                    } else {
                    switch (currentElement) {
                        case SMALL_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX() + 50, e.getY(), 4)); // Example size for small wall
                            break;
                        case MEDIUM_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX() + 100, e.getY(), 4)); // Example size for medium wall
                            break;
                        case LARGE_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX() + 150, e.getY(), 4)); // Example size for large wall
                            break;
                        case WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX(), e.getY(), 4)); // Start a new resizable wall
                            break;
                        case CIRCLE:
                            shapes.add(new Circle(e.getX(), e.getY(), 0)); // Start a new circle
                            break;
                        case DELETE:
                            selectionRect = new Rectangle(e.getX(), e.getY(), 0, 0);
                            break;
                        case VERTICAL_SMALL_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX(), e.getY() + 50, 4)); // 50 pixels high for small vertical wall
                            break;
                        case VERTICAL_MEDIUM_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX(), e.getY() + 100, 4)); // 100 pixels high for medium vertical wall
                            break;
                        case VERTICAL_LARGE_WALL:
                            shapes.add(new Wall(e.getX(), e.getY(), e.getX(), e.getY() + 150, 4)); // 150 pixels high for large vertical wall
                            break;
                        case TRIANGLE:
                            shapes.add(new Triangle(e.getX(), e.getY(), 0)); // Start a new circle
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
                        selectionRect = null;
                        repaint();
                    }
                    startPoint = null;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (currentElement == ElementType.DELETE && selectionRect != null) {
                        int x = Math.min(startPoint.x, e.getX());
                        int y = Math.min(startPoint.y, e.getY());
                        int width = Math.abs(e.getX() - startPoint.x);
                        int height = Math.abs(e.getY() - startPoint.y);
                        selectionRect.setBounds(x, y, width, height);
                        repaint();
                    } else if (currentElement == ElementType.CIRCLE && startPoint != null) {
                        Circle lastCircle = (Circle) shapes.get(shapes.size() - 1);
                        lastCircle.setRadius((int) startPoint.distance(e.getPoint()));
                        repaint();
                    } else if (currentElement == ElementType.WALL && startPoint != null) {
                        Wall lastWall = (Wall) shapes.get(shapes.size() - 1);
                        lastWall.x2 = e.getX();
                        lastWall.y2 = e.getY();
                        repaint();
                    }
                    else if (currentElement == ElementType.TRIANGLE && startPoint != null) {
                        Triangle lastTriangle = (Triangle) shapes.get(shapes.size() - 1);
                        lastTriangle.x = e.getX();
                        lastTriangle.y = e.getY();
                        repaint();
                    }
                    if (currentElement == ElementType.SELECTOR && selectedShape != null && dragOffset != null) {
                        // Calculate the new top-left corner of the shape based on the drag offset
                        int newX = e.getX() - dragOffset.x;
                        int newY = e.getY() - dragOffset.y;

                        // Move the selected shape to the new location
                        selectedShape.moveTo(newX, newY);

                        repaint(); // Repaint the panel to update the shape's position
                    } else if (currentElement == ElementType.CIRCLE && startPoint != null) {
                        Circle lastCircle = (Circle) shapes.get(shapes.size() - 1);
                        lastCircle.setRadius((int) startPoint.distance(e.getPoint()));
                        repaint();
                    } else if (currentElement == ElementType.WALL && startPoint != null) {
                        Wall lastWall = (Wall) shapes.get(shapes.size() - 1);
                        lastWall.x2 = e.getX();
                        lastWall.y2 = e.getY();
                        repaint();
                    }
                    else if (currentElement == ElementType.TRIANGLE && startPoint != null) {
                        Triangle lastTriangle = (Triangle) shapes.get(shapes.size() - 1);
                        lastTriangle.x = e.getX();
                        lastTriangle.y = e.getY();
                        repaint();
                    }
                    
                    // Additional code for other element types if necessary
                }
            });

        }
        private Shape findShapeAtPoint(Point point) {
            for (Shape shape : shapes) {
                if (shape instanceof Wall && ((Wall) shape).contains(point)) {
                    return shape;
                } else if (shape instanceof Circle && ((Circle) shape).contains(point)) {
                    return shape;
                }
            }
            return null; // No shape found at the point
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            for (Shape shape : shapes) {
                if (shape instanceof Wall) {
                    ((Wall) shape).draw(g2d);
                } else if (shape instanceof Circle) {
                    ((Circle) shape).draw(g2d);
                }
            }

            if (selectionRect != null) {
                g2d.setColor(Color.BLUE);
                g2d.draw(selectionRect);
            }
        }

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

        class Circle implements Shape {
            int x, y, radius;

            public Circle(int x, int y, int radius) {
                this.x = x;
                this.y = y;
                this.radius = radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            public void draw(Graphics2D g2d) {
                // Save the current stroke
                Stroke originalStroke = g2d.getStroke();

                // Set the stroke for the circle to be thin
                g2d.setStroke(new BasicStroke(1)); // Set the thickness for the circle

                // Draw the circle
                g2d.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

                // Restore the original stroke so that other shapes are not affected
                g2d.setStroke(originalStroke);
            }
            public boolean contains(Point p) {
                double dist = Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
                return dist <= radius;
            }

            public void moveTo(int x, int y) {
                this.x = x;
                this.y = y;
            }
            public Rectangle getBounds() {
                return new Rectangle(x - radius, y - radius, 2 * radius, 2 * radius);
            }


        }
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
    }
}
