package com.floorplan.maven.classes;

import javax.swing.*;
import java.awt.*;

public class ConstructionToolsPanel extends JPanel {
    private ElementType currentElement;
    public ConstructionToolsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializePanel();
    }
    public ConstructionToolsPanel(ElementType currentElement) {
        this.currentElement = currentElement;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializePanel();
    }

    private void initializePanel() {
        JPanel constructionPanel = createSectionPanel("Construction Tools");
        // Add buttons for various construction tools
        addToolButton(constructionPanel, "Room", ElementType.ROOM);
        addToolButton(constructionPanel, "Custom Wall", ElementType.WALL);
        // Add more tool buttons as needed...

        // Add the construction panel to this ConstructionToolsPanel
        add(constructionPanel);
    }

    private JPanel createSectionPanel(String title) {
        JPanel sectionPanel = new JPanel(new GridLayout(0, 1));
        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));
        return sectionPanel;
    }

    private void addToolButton(JPanel panel, String label, ElementType elementType) {
        JButton button = new JButton(label);
        button.addActionListener(e -> currentElement = elementType);
        panel.add(button);
    }
}
