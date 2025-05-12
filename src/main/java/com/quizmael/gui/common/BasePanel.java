package com.quizmael.gui.common;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for all application panels.
 * <p>
 * Provides a common structure and utility methods for child panels, including:
 * - Standard panel dimensions
 * - Shared layout helpers (e.g., GridBagConstraints builder)
 * - Standardized dialogs (info and error messages)
 * <p>
 * This class is intended to be extended by all view panels in the application,
 * allowing centralized control for future enhancements such as:
 * - Styling and themes
 * - Internationalization via ResourceBundles
 * - Logging
 * - Accessibility features
 * - Animations or transitions
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public abstract class BasePanel extends JPanel {

    // ------------------------------------------------------------
    //                      Constructor
    // ------------------------------------------------------------

    /**
     * Sets the panel's preferred size and layout, and calls initialize().
     */
    public BasePanel() {
        setPreferredSize(new Dimension(1280, 720));
        // customInit();
    }

    // ------------------------------------------------------------
    //                     Abstract Methods
    // ------------------------------------------------------------

    // Maybe useful in the future
    /**
     * Subclasses must implement this to define and arrange their UI components.
     */
    // protected abstract void customInit();
    
    // ------------------------------------------------------------
    //                  Utility Methods (optional)
    // ------------------------------------------------------------

    /**
     * Creates GridBagConstraints with standard spacing.
     *
     * @param x column position
     * @param y row position
     * @return GridBagConstraints with default insets and position
     */
    protected GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    }
    
    /**
     * Utility to show an info message.
     * @param title The dialog title
     * @param message The message to show
     */
    protected void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Utility to show an error or info dialog.
     *
     * @param message the message to show
     * @param title dialog title
     */
    protected void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    // TODO: ¿agregar el resto de tipos de mensaje de showMessageDialog: QUESTION_MESSAGE, WARNING_MESSAGE?
}