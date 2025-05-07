/*
 * Created by JFormDesigner on Wed May 07 21:46:05 CEST 2025
 */

package com.quizmael.gui.windows;

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

    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------

    /**
     * Creates the main window of the application.
     */
    public MainWindow() {

        initComponents();

        setTitle("Quizmael");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        setVisible(true);

    }


    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Ismael Reina

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new CardLayout(5, 5));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Ismael Reina
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
