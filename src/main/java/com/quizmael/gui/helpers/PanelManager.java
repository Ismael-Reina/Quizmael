package com.quizmael.gui.helpers;

import com.quizmael.util.LoggerUtil;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Manages screen transitions using CardLayout and logs navigation events.
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public class PanelManager extends javax.swing.JPanel {

    // ------------------------------------------------------------
    //                      Constants (Panel Keys)
    // ------------------------------------------------------------
    /** Constants for the keys of each panel used in CardLayout navigation. */
    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String MAIN_MENU = "MAIN_MENU";
    public static final String ADMIN = "ADMIN";
    public static final String PLAY = "PLAY";
    public static final String QUIZ_SELECTION = "QUIZ_SELECTION";
    public static final String RESULTS = "RESULTS";
    public static final String APP_SETTINGS = "APP_SETTINGS";

    // ------------------------------------------------------------
    //                      Attributes
    // ------------------------------------------------------------
    private final CardLayout cardLayout;
    private final Map<String, JPanel> panels;
    
    // ------------------------------------------------------------
    //                      Public Methods
    // ------------------------------------------------------------
    
    /**
     * Creates new form PanelManager
     */
    public PanelManager() {

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
        LoggerUtil.debug(getClass(), "Panel registered: " + name);
    }

    /**
     * Shows the panel identified by the given name.
     *
     * @param name Name of the panel to show
     */
    public void showPanel(String name) {
        if (panels.containsKey(name)) {
            cardLayout.show(this, name);
            LoggerUtil.info(getClass(), "Navigated to: " + name);
        } else {
            LoggerUtil.warn(getClass(), "Attempted to show non-existent panel: " + name);
        }
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
    
}
