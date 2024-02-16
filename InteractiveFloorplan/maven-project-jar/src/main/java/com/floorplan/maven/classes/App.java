package com.floorplan.maven.classes;
import javax.swing.*;
import java.awt.*;
/**
 * Hello world!
 *
 */
public class App extends  JFrame
{
    public App() {
        setTitle("My First Swing Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
			    App app = new App();
			    app.setVisible(true);
			}
		});
    }
}
