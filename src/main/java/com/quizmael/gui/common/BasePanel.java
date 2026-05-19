package com.quizmael.gui.common;

import com.quizmael.util.I18nUtil;
import com.quizmael.util.LoggerUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for all application view panels.
 * <p>
 * This class acts as a blueprint, centralizing UI standards to ensure
 * visual consistency across the application. It provides:
 * </p>
 * <ul>
 * <li><b>Standard Dimensions:</b> Enforces the 1280x720 resolution.</li>
 * <li><b>Layout Helpers:</b> Simplifies {@link java.awt.GridBagLayout} configurations.</li>
 * <li><b>I18n Dialogs:</b> Standardized information and error messages using internationalization.</li>
 * <li><b>Logging:</b> Automatic logging of UI events and errors.</li>
 * </ul>
 *
 * @author Ismael Reina Muñoz
 * @version 1.0
 */
public abstract class BasePanel extends JPanel {

    // ------------------------------------------------------------
    //                      Constructor
    // ------------------------------------------------------------

    /**
     * Sets the panel's preferred size
     */
    public BasePanel() {
        setPreferredSize(new Dimension(1280, 720));
    }
    
    // ------------------------------------------------------------
    //                  Utility Methods (optional)
    // ------------------------------------------------------------

    /**
     * Helper to create GridBagConstraints for custom layouts.
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
        // gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL; // TODO: originalmente NONE complementado con la línea anterior, comprobar cómo queda esto con el fill horizontal
        return gbc;
    }

    /**
     * Shows an information dialog with a translated title.
     * @param messageKey The i18n key for the message or the message itself.
     */
    protected void showInfo(String messageKey) {
        String translatedMessage = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage("dialog.title.info");
        JOptionPane.showMessageDialog(this, translatedMessage, title, JOptionPane.INFORMATION_MESSAGE);
        LoggerUtil.info(getClass(), "Info dialog shown: " + translatedMessage);
    }

    /**
     * Shows an error dialog with a translated title and logs the event.
     * @param messageKey The i18n key for the message or the message itself.
     */
    protected void showError(String messageKey) {
        String translatedMessage = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage("dialog.title.error");
        JOptionPane.showMessageDialog(this, translatedMessage, title, JOptionPane.ERROR_MESSAGE);
        LoggerUtil.error(getClass(), "Error dialog shown: " + translatedMessage, null);
    }

    /**
     * Shows a temporary information dialog that closes automatically.
     * Useful for non-intrusive success notifications (Toasts).
     *
     * @param messageKey   The i18n key for the message to display.
     * @param titleKey     The i18n key for the dialog title.
     * @param milliseconds Duration in milliseconds before auto-closing.
     */
    public static void showTimedMessage(String messageKey, String titleKey, int milliseconds) {
        String message = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage(titleKey);

        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setModal(false);

        // We start the safety timer
        Timer timer = new Timer(milliseconds, e -> {
            dialog.dispose();
            LoggerUtil.debug(BasePanel.class, "Timed dialog closed automatically: " + message);
        });

        // Forces the Timer to run ONLY ONCE and then be destroyed
        timer.setRepeats(false);

        // We start the safety time
        timer.start();

        dialog.setVisible(true);
    }

    /**
     * Shows a standardized confirmation dialog using internationalized keys.
     * @return true if the user clicks YES, false otherwise.
     */
    protected boolean showConfirmDialog(String messageKey, String titleKey) {
        String message = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage(titleKey);
        int result = JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a standardized input dialog using internationalized keys.
     * @return the entered String, or null if the user cancels.
     */
    protected String showInputDialog(String messageKey, String titleKey) {
        String message = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage(titleKey);
        return JOptionPane.showInputDialog(this, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Shows a standardized options dialog mapping an array of choices to clickable buttons.
     * @param messageKey internationalized key for the descriptive prompt text
     * @param titleKey internationalized key for the dialog window title banner
     * @param options array of selectable components (like Enum values) mapped to buttons
     * @param defaultOption the pre-focused default selection component
     * @return the selected index position within the array, or -1 if closed explicitly
     */
    protected int showOptionDialog(String messageKey, String titleKey, Object[] options, Object defaultOption) {
        String message = I18nUtil.getMessage(messageKey);
        String title = I18nUtil.getMessage(titleKey);
        return JOptionPane.showOptionDialog(
                this, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, defaultOption
        );
    }
}