package com.quizmael.gui.windows;

import com.quizmael.gui.helpers.PanelManager;

import java.awt.*;
import javax.swing.*;

/**
 * Main application window that hosts the main content panel (PanelManager).
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class MainWindow extends JFrame {

    // ------------------------------------------------------------
    //                      Attributes
    // ------------------------------------------------------------

    private PanelManager panelManager;

    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------

    /**
     * Creates the main window of the application.
     */
    public MainWindow() {

        initComponents();

        // Configures the main window
        setTitle("Quizmael");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Configures the main window
        panelManager = new PanelManager();      // Initializes the PanelManager (with CardLayout)
        add(panelManager, BorderLayout.CENTER); // Add to the center of the window

        // Shows the window
        setVisible(true);

    }

    /**
     * Gets the panel manager that controls screen switching.
     * @return the PanelManager instance
     */
    public PanelManager getPanelManager() {
        return panelManager;
    }


    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Ismael Reina

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Ismael Reina
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
