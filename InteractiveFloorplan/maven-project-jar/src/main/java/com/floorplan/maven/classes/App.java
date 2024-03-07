package com.floorplan.maven.classes;
import javax.swing.*;


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
    private ElementType currentElement = ElementType.WALL; // Default to wall drawing mode
    private double zoomFactor = 1.0;
    private Integer thickness =  1;
    public App() {
        initUI();
        // Replace createDesignPalette with createMainPanel to include left, right, and top components
        add(createMainPanel());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.setVisible(true);
        });
    }

    private void initUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createMainPanel());
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the top which includes both the toolbar and the top panel
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.PAGE_AXIS)); // Vertical box layout

    
        JPanel topPanel = createTopPanel(); // Create the top panel
        topContainer.add(topPanel); // Add the top panel to the top container

        mainPanel.add(topContainer, BorderLayout.NORTH); // Add the top container to the main panel at the top

        // Integrating tool panels
        mainPanel.add(createConstructionToolsPanel(), BorderLayout.WEST);
        mainPanel.add(createFurnitureAndUtilitiesPanel(), BorderLayout.EAST);

        mainPanel.add(drawingArea, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createConstructionToolsPanel() {
        // Main panel with BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Create the construction tools section
        JPanel constructionPanel = createSectionPanel("Construction Tools");
        addToolButton(constructionPanel, "Room", ElementType.ROOM);

        addToolButton(constructionPanel, "Custom Wall", ElementType.WALL);
        addToolButton(constructionPanel, "Horizontal Small Wall", ElementType.SMALL_WALL);
        addToolButton(constructionPanel, "Horizontal Medium Wall", ElementType.MEDIUM_WALL);
        addToolButton(constructionPanel, "Horizontal Large Wall", ElementType.LARGE_WALL);
        addToolButton(constructionPanel, "Vertical Small Wall", ElementType.VERTICAL_SMALL_WALL);
        addToolButton(constructionPanel, "Vertical Medium Wall", ElementType.VERTICAL_MEDIUM_WALL);
        addToolButton(constructionPanel, "Vertical Large Wall", ElementType.VERTICAL_LARGE_WALL);
        addToolButton(constructionPanel, "Horizontal Wall Opening", ElementType.OPENING);
        addToolButton(constructionPanel, "Vertical Wall Opening", ElementType.VERTICAL_OPENING);
        addToolButton(constructionPanel, "Custom Wall Opening", ElementType.OPENING_CUSTOM);

        // Add more construction tool buttons...


        // Add sections to the main panel
        mainPanel.add(constructionPanel);
      
        // Add more sections as needed...

        return mainPanel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new GridLayout(0, 1)); // Or use another layout if preferred
        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));
        return sectionPanel;
    }
    private JScrollPane createFurnitureAndUtilitiesPanel() {
        // Your ImageIcon declarations here...
    	ImageIcon fridgeIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Fridge.png"));
    	ImageIcon sinkIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Sink.png"));
    	ImageIcon toiletIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Toilet.png"));
    	ImageIcon leftDoorIcon = new ImageIcon(getClass().getClassLoader().getResource("images/DoorLeft.png"));
    	ImageIcon rightDoorIcon = new ImageIcon(getClass().getClassLoader().getResource("images/DoorRight.png"));
    	ImageIcon tableIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Table.png"));
    	ImageIcon bedIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Bed.png"));
    	ImageIcon showerIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Shower.png"));
    	ImageIcon stoveIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Stove.png"));
    	ImageIcon bathIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Bath.png"));
    	ImageIcon lineIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Line.png"));
    	ImageIcon circleIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Circle.png"));
    	ImageIcon rectangleIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Rectangle.png"));
    	ImageIcon triangleIcon = new ImageIcon(getClass().getClassLoader().getResource("images/Triangle.png"));
        // Main panel to hold everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Panel for Furniture
        JPanel furniturePanel = new JPanel(new GridLayout(0, 2));
        furniturePanel.setBorder(BorderFactory.createTitledBorder("Furniture"));

        // Using the helper method to create buttons with icons and text
        createButtonWithIconAndText(furniturePanel, "Fridge", fridgeIcon, ElementType.FRIDGE);
        createButtonWithIconAndText(furniturePanel, "Sink", sinkIcon, ElementType.SINK);
        createButtonWithIconAndText(furniturePanel, "Toilet", toiletIcon, ElementType.TOILET);
        createButtonWithIconAndText(furniturePanel, "Door Right", rightDoorIcon, ElementType.DOORREVERSE);
        createButtonWithIconAndText(furniturePanel, "Door Left", leftDoorIcon, ElementType.DOOR);
        createButtonWithIconAndText(furniturePanel, "Table", tableIcon, ElementType.TABLE);
        createButtonWithIconAndText(furniturePanel, "Bed", bedIcon, ElementType.BED);
        createButtonWithIconAndText(furniturePanel, "Shower", showerIcon, ElementType.SHOWER);
        createButtonWithIconAndText(furniturePanel, "Stove", stoveIcon, ElementType.STOVE);
        createButtonWithIconAndText(furniturePanel, "Bathtub", bathIcon, ElementType.BATHTUB);

        // Panel for Shapes
        JPanel shapesPanel = new JPanel(new GridLayout(0, 2));
        shapesPanel.setBorder(BorderFactory.createTitledBorder("Shapes"));

        // Using the helper method to create buttons with icons and text for shapes
        createButtonWithIconAndText(shapesPanel, "Circle", circleIcon, ElementType.CIRCLE);
        createButtonWithIconAndText(shapesPanel, "Triangle", triangleIcon, ElementType.TRIANGLE);
        createButtonWithIconAndText(shapesPanel, "Rectangle", rectangleIcon, ElementType.RECTANGLE);
        createButtonWithIconAndText(shapesPanel, "Line", lineIcon, ElementType.LINE);

        // Add subpanels to the main panel
        mainPanel.add(furniturePanel);
        mainPanel.add(shapesPanel);

        // Wrap the main panel inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private void createButtonWithIconAndText(JPanel panel, String text, ImageIcon icon, ElementType elementType) {
        JButton button = new JButton(text, icon);
        button.addActionListener(e -> currentElement = elementType);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setToolTipText(text); // Set the tooltip as the text
        panel.add(button);
    }

    private void addToolButton(JPanel panel, String label, ElementType elementType) {
        addToolButton(panel, label, elementType, () -> currentElement = elementType);
    }

    private void addToolButton(JPanel panel, String label, ElementType elementType, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }
    private JPanel createTopPanel() {
        // Main top panel using BorderLayout
        JPanel topPanel = new JPanel(new BorderLayout());

        // Create the toolbar and add it to the left of the top panel
        JToolBar topToolBar = createTopToolBar();
        topPanel.add(topToolBar, BorderLayout.WEST); // Add the toolbar on the left side

        // Create an actions panel for the action buttons
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout for center alignment
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        addToolButton(actionsPanel, "Rotate", ElementType.ROTATE);
        addToolButton(actionsPanel, "Move", ElementType.MOVE);
        addToolButton(actionsPanel, "Delete", ElementType.DELETE);
        addToolButton(actionsPanel, "Line Thickness", null, () -> {
            // Line Thickness logic
            String thicknessValue = JOptionPane.showInputDialog(this, "Enter line thickness:", "Line Thickness", JOptionPane.PLAIN_MESSAGE);
            try {
                thickness = Integer.parseInt(thicknessValue);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the thickness.", "Invalid Thickness", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add the actions panel to the center of the top panel
        topPanel.add(actionsPanel, BorderLayout.CENTER);

        return topPanel;
    }


    private JToolBar createTopToolBar() {
        JToolBar toolBar = new JToolBar();

        addButtonToToolBar(toolBar, "Save", () -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveDrawingToFile(drawingArea.getShapes(), file.getAbsolutePath());
            }
        });
        addButtonToToolBar(toolBar, "Load", () -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                List<Shape> shapes = loadDrawingFromFile(file.getAbsolutePath());
                if (shapes != null) {
                    loadDrawing(shapes);
                }
            }        });
        // Add more toolbar buttons as needed
        return toolBar;
    }

    private void addButtonToToolBar(JToolBar toolBar, String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        toolBar.add(button);
    }

    public void saveDrawingToFile(List<Shape> shapes, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(shapes);
            System.out.println("Drawing saved to " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public List<Shape> loadDrawingFromFile(String filename) {
        List<Shape> loadedShapes = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            loadedShapes = (List<Shape>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedShapes;
    }
    public void loadDrawing(List<Shape> shapes) {
        drawingArea.clear(); // Implement a method to clear the current drawing
        drawingArea.addShapes(shapes); // Implement a method to add a list of shapes to the drawing
        drawingArea.repaint();
    }
  
    class DrawingArea extends JPanel {
        private static final long serialVersionUID = 1L; // Recommended for Serializable classes

        private List<Shape> shapes = new ArrayList<>();
        private Point startPoint = null;
        private Rectangle selectionRect = null;
        private Shape selectedShape = null; // Variable to hold the selected shape
        private Point dragOffset = null; // Track the offset from the initial click point
        private double translateX = 0;
        private double translateY = 0;
        private Point initialClickPoint = null;
        private boolean resizing = false; // Flag to indicate a resize operation is in progress
        private Shape resizingShape = null; // The shape being resized
        private Point resizeStartPoint = null; // The 
        public void clear() {
       	 shapes.clear(); // Clear the list of shapes
            repaint(); // Repaint to update the display
       }
       public List<Shape> getShapes() {
			return shapes;
		}
	public void addShapes(List<Shape> newShapes) {
       	   shapes.addAll(newShapes); // Add all new shapes to the list
              repaint(); // Repaint to update the display
       }
        public DrawingArea() {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            setBackground(Color.WHITE);
            addMouseWheelListener(new MouseWheelListener() {
            	@Override
            	public void mouseWheelMoved(MouseWheelEvent e) {
            	    // Determine the direction and amount to zoom
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


            });


            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                	   // Adjust mouse coordinates by the current zoom factor
                    int x = (int) ((e.getX() - translateX) / zoomFactor);
                    int y = (int) ((e.getY() - translateY) / zoomFactor);
                    startPoint = new Point(x, y);

                    System.out.println("Mouse Pressed at: " + startPoint + " with currentElement: " + currentElement); // Debugging print

                    // Find the shape under the mouse using adjusted coordinates
                    Shape shapeUnderMouse = findShapeAtPoint(new Point(x, y));
                    selectedShape = shapeUnderMouse; // This will be null if no shape is found, effectively deselecting

                    if (currentElement == ElementType.ROTATE && selectedShape != null) {
                        // For rotation, the initial click point is crucial
                        initialClickPoint = startPoint; // Use adjusted startPoint
                    }

                    // Check if the click is on a resize handle using the adjusted point
                    if (selectedShape != null && isClickOnHandle(new Point(x, y), selectedShape, selectedShape.getRotationAngle())) {
                        resizing = true;
                        resizingShape = selectedShape;
                        resizeStartPoint = new Point(x, y); // Use adjusted coordinates
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
                                shapes.add(new Wall(x, y, x + 50, y, 6, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
                                break;
                            case VERTICAL_OPENING:
                                shapes.add(new Wall(x, y, x, y + 50, 6, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
                                break;
                            case OPENING_CUSTOM:
                                shapes.add(new Wall(x, y, x, y, 6, Color.WHITE)); // Adjusted for zoom, start a new resizable wall
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
                            case COUCH:
                            	shapes.add(new Couch(x,y,70,50));
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


                @Override
                public void mouseReleased(MouseEvent e) {
                    if (resizing) {
                        resizing = false;
                        resizingShape = null;
                        resizeStartPoint = null;
                    }
                    if (currentElement == ElementType.DELETE && selectionRect != null) {
                        shapes.removeIf(shape -> shape instanceof Wall && selectionRect.intersectsLine(((Wall) shape).x1, ((Wall) shape).y1, ((Wall) shape).x2, ((Wall) shape).y2));
                        shapes.removeIf(shape -> shape instanceof Circle && selectionRect.contains(((Circle) shape).x, ((Circle) shape).y));
                        shapes.removeIf(shape -> shape instanceof Triangle && selectionRect.contains(((Triangle) shape).x, ((Triangle) shape).y));
                        shapes.removeIf(shape -> shape instanceof RectangleShape && selectionRect.contains(((RectangleShape) shape).x, ((RectangleShape) shape).y));
                        shapes.removeIf(shape -> shape instanceof FurnitureItem && selectionRect.contains(((FurnitureItem) shape).x, ((FurnitureItem) shape).y));

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
            });

            addMouseMotionListener(new MouseMotionAdapter() {
            	 @Override
            	    public void mouseMoved(MouseEvent e) {
            	        if (selectedShape != null) {
            	            Cursor newCursor = getCursorForHandle(e.getPoint(), selectedShape, selectedShape.getRotationAngle());
            	            setCursor(newCursor);
            	        } else {
            	            setCursor(Cursor.getDefaultCursor()); // Reset to default cursor when not over a handle
            	        }
            	    }
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

            applyTransformations(g2d);
            drawGrid(g2d);
            drawShapes(g2d);
            drawSelectionIndicatorIfNeeded(g2d);
            // Display the lengths of the sides of the selected rectangle
            if (selectedShape instanceof Wall) {
                Wall selectedWall = (Wall) selectedShape;
                double length = calculateWallLength(selectedWall);
                String lengthText = String.format("%.2f feet", length / 20); // Length calculation remains the same

                // Calculate the midpoint of the wall for text placement without zoom adjustment for length
                int midX = (selectedWall.x1 + selectedWall.x2) / 2;
                int midY = (selectedWall.y1 + selectedWall.y2) / 2;

                // Adjust only the position (midX, midY) for zoom and translation to correctly place the text
                midX = (int) (midX * zoomFactor + translateX);
                midY = (int) (midY * zoomFactor + translateY);

                g2d.setColor(Color.RED); // Set text color
                g2d.drawString(lengthText, midX, midY); // Draw length near the wall
            }
            if (selectedShape instanceof RectangleShape) {
                RectangleShape selectedRectangle = (RectangleShape) selectedShape;
                int width = selectedRectangle.width;
                int height = selectedRectangle.height;

                // Calculate lengths adjusted for zoom
                String widthText = String.format("%.2f feet", (width/20.0));
                String heightText = String.format("%.2f feet", (height/20.0));

                // Calculate midpoints for text placement
                int midX = selectedRectangle.x + width / 2;
                int midY = selectedRectangle.y + height / 2;

                // Adjust for zoom and translation
                midX = (int) (midX * zoomFactor + translateX);
                midY = (int) (midY * zoomFactor + translateY);

                g2d.setColor(Color.RED); // Set text color

                // Draw length texts near the sides, adjust positions as needed
                g2d.drawString(widthText, midX, selectedRectangle.y - 5); // Top side
                g2d.drawString(widthText, midX, selectedRectangle.y + height + 15); // Bottom side
                g2d.drawString(heightText, selectedRectangle.x - 40, midY); // Left side
                g2d.drawString(heightText, selectedRectangle.x + width + 5, midY); // Right side
            }
            g2d.setTransform(new AffineTransform()); // This line resets all prior transformations

            drawRuler(g2d);

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
