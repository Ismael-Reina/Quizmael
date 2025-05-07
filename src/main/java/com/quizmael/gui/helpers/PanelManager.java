/*
 * Created by JFormDesigner on Wed May 07 22:55:50 CEST 2025
 */

package com.quizmael.gui.helpers;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Central panel manager using CardLayout to switch between different panels.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class PanelManager extends JPanel {

    // ------------------------------------------------------------
    //                      Attributes
    // ------------------------------------------------------------
    private CardLayout cardLayout;
    private Map<String, JPanel> panels;


    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------

    /**
     * Initializes the panel manager with a CardLayout.
     */
    public PanelManager() {
        initComponents();

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        panels = new HashMap<>(); // To save the panels
    }


    /**
     * Adds a new panel to the manager with a given name.
     *
     * @param name  Unique name for the panel
     * @param panel The JPanel to add
     */
    public void addPanel(String name, JPanel panel) {
        panels.put(name, panel); // Saves on the map
        add(panel, name);        // Adds to the CardLayout with that key
    }

    /**
     * Shows the panel identified by the given name.
     *
     * @param name Name of the panel to show
     */
    public void showPanel(String name) {
        cardLayout.show(this, name); // Switch to the indicated panel
    }

    /**
     * Gets the panel previously registered with a given name.
     *
     * @param name The name of the panel
     * @return The JPanel instance
     */
    public JPanel getPanel(String name) {
        return panels.get(name);
    }

    // ------------------------------------------------------------
    //                      Private Methods
    // ------------------------------------------------------------

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Ismael Reina

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
        . EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder. CENTER, javax
        . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font .BOLD ,
        12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans
        . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er" .equals (e .
        getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new CardLayout(5, 5));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Ismael Reina
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
