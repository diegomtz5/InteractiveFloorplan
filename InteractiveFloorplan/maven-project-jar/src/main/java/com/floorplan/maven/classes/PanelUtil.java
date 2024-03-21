package com.floorplan.maven.classes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.floorplan.maven.classes.App.DrawingArea;

public class PanelUtil {
    private final DrawingArea drawingArea;
    private Integer thickness;
    private ElementType currentElement;
    private JFrame parentFrame; // Add a reference to the parent JFrame

    public PanelUtil(JFrame parentFrame, DrawingArea drawingArea, Integer thickness, ElementType currentElement) {
        this.parentFrame = parentFrame; // Initialize the parent JFrame reference
        this.drawingArea = drawingArea;
        this.currentElement = currentElement;
        this.thickness = thickness;
    }

	  public JPanel createMainPanel() {
	        JPanel mainPanel = new JPanel(new BorderLayout());
	 
	        // Create a panel for the top which includes both the toolbar and the top panel
	        JPanel topContainer = new JPanel();
	        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.PAGE_AXIS)); // Vertical box layout

	    
	        JPanel topPanel = createTopPanel(); // Create the top panel
	        topContainer.add(topPanel); // Add the top panel to the top container
// Add the top container to the main panel at the top
	        mainPanel.add(topContainer, BorderLayout.NORTH); // Add the top container to the main panel at the top

	        // Integrating tool panels
	        mainPanel.add(createConstructionToolsPanel(), BorderLayout.WEST);
	        mainPanel.add(createFurnitureAndUtilitiesPanel(), BorderLayout.EAST);

	        mainPanel.add(drawingArea, BorderLayout.CENTER);

	        return mainPanel;
	    }

	    public JPanel createConstructionToolsPanel() {
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

	    public JPanel createSectionPanel(String title) {
	        JPanel sectionPanel = new JPanel();
	        sectionPanel.setLayout(new GridLayout(0, 1)); // Or use another layout if preferred
	        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));
	        return sectionPanel;
	    }
	    public JScrollPane createFurnitureAndUtilitiesPanel() {
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

	    public void createButtonWithIconAndText(JPanel panel, String text, ImageIcon icon, ElementType elementType) {
	        JButton button = new JButton(text, icon);
	        button.addActionListener(e -> {
	            currentElement = elementType;
	            drawingArea.setCurrentElement(elementType); // Update the currentElement in DrawingArea
	        });
	        button.setHorizontalTextPosition(JButton.CENTER);
	        button.setVerticalTextPosition(JButton.BOTTOM);
	        button.setToolTipText(text); // Set the tooltip as the text
	        panel.add(button);
	    }

	  
	    public void addToolButton(JPanel panel, String label, ElementType elementType) {
	        JButton button = new JButton(label);
	        button.addActionListener(e -> {
	            currentElement = elementType; // Update the current element
	            drawingArea.setCurrentElement(elementType); // Inform DrawingArea of the new mode
	        });
	        panel.add(button);
	    }

	    private void addToolButton(JPanel panel, String label, ElementType elementType, Runnable action) {
	        JButton button = new JButton(label);
	        button.addActionListener(e -> {
	            currentElement = elementType; // Update the current element in PanelUtil
	            drawingArea.setCurrentElement(elementType); // Update the current element in DrawingArea
	            action.run(); // Perform the additional action
	        });
	        panel.add(button);
	    }

	    public JPanel createTopPanel() {
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
	        addToolButton(actionsPanel, "Select", ElementType.SELECT);

	        addToolButton(actionsPanel, "Line Thickness", null, () -> {
	            // Line Thickness logic
	            String thicknessValue = JOptionPane.showInputDialog(parentFrame, "Enter line thickness:", "Line Thickness", JOptionPane.PLAIN_MESSAGE);
	            try {
	                thickness = Integer.parseInt(thicknessValue);
	                drawingArea.setThickness(thickness); // Update the thickness in DrawingArea
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(parentFrame, "Please enter a valid number for the thickness.", "Invalid Thickness", JOptionPane.ERROR_MESSAGE);
	            }
	        });
	        // Add the actions panel to the center of the top panel
	        topPanel.add(actionsPanel, BorderLayout.CENTER);

	        return topPanel;
	    }


	    public JToolBar createTopToolBar() {
	        JToolBar toolBar = new JToolBar();

	        addButtonToToolBar(toolBar, "Save", () -> {
	            JFileChooser fileChooser = new JFileChooser();
	            if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                saveDrawingToFile(drawingArea.getShapes(), file.getAbsolutePath());
	            }
	        });
	        addButtonToToolBar(toolBar, "Load", () -> {
	            JFileChooser fileChooser = new JFileChooser();
	            if (fileChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                List<Shape> shapes = loadDrawingFromFile(file.getAbsolutePath());
	                if (shapes != null) {
	                    loadDrawing(shapes);
	                }
	            }        });
	        // Add more toolbar buttons as needed
	        return toolBar;
	    }

	    public void addButtonToToolBar(JToolBar toolBar, String label, Runnable action) {
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
	  
}
