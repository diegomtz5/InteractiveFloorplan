package com.floorplan.maven.classes;

import javax.swing.*;
import java.awt.*;

public class FurnitureAndUtilitiesPanel extends JScrollPane {
    private ElementType currentElement;

    public FurnitureAndUtilitiesPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Create and add the furniture panel
        JPanel furniturePanel = createFurniturePanel();
        mainPanel.add(furniturePanel);

        // Create and add the shapes panel
        JPanel shapesPanel = createShapesPanel();
        mainPanel.add(shapesPanel);

        // Set the main panel as the viewport view
        setViewportView(mainPanel);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void setCurrentElement(ElementType currentElement) {
        this.currentElement = currentElement;
    }

    private JPanel createFurniturePanel() {
        JPanel furniturePanel = new JPanel(new GridLayout(0, 2));
        furniturePanel.setBorder(BorderFactory.createTitledBorder("Furniture"));
        // Add furniture buttons
        createButtonWithIconAndText(furniturePanel, "Fridge", new ImageIcon("path/to/fridge/icon"), ElementType.FRIDGE);
        // Add more furniture buttons as needed...
        return furniturePanel;
    }

    private JPanel createShapesPanel() {
        JPanel shapesPanel = new JPanel(new GridLayout(0, 2));
        shapesPanel.setBorder(BorderFactory.createTitledBorder("Shapes"));
        // Add shape buttons
        createButtonWithIconAndText(shapesPanel, "Circle", new ImageIcon("path/to/circle/icon"), ElementType.CIRCLE);
        // Add more shape buttons as needed...
        return shapesPanel;
    }

    private void createButtonWithIconAndText(JPanel panel, String text, ImageIcon icon, ElementType elementType) {
        JButton button = new JButton(text, icon);
        button.addActionListener(e -> currentElement = elementType);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        panel.add(button);
    }
}
