package com.quizmael.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * Utility class for handling Internationalization (i18n).
 * Loads messages from .properties files based on the current Locale.
 */
public class I18nUtil {

    // The base name of the properties files
    private static final String BUNDLE_NAME = "messages";

    // We store the current Locale here. By default, it grabs the OS language.
    private static Locale currentLocale = Locale.getDefault();

    // The ResourceBundle that holds the loaded messages
    private static ResourceBundle messages = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);

    /**
     * Returns the current locale of the application.
     * @return the active {@link Locale}
     */
    public static Locale getLocale() {
        return currentLocale;
    }

    /**
     * Changes the application's language globally.
     *
     * @param languageCode The ISO 639 language code (e.g., "en", "es").
     * @param countryCode  The ISO 3166 country code (e.g., "US", "ES"). Can be empty.
     */
    public static void setLocale(String languageCode, String countryCode) {
        currentLocale = new Locale(languageCode, countryCode);
        messages = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        LoggerUtil.info(I18nUtil.class, "Language changed to: " + currentLocale.toString());
    }

    /**
     * Retrieves a message from the properties file using a key.
     *
     * @param key The key to look for (e.g., "error.validation.email").
     * @return The translated message, or a placeholder if the key is not found.
     */
    public static String getMessage(String key) {
        try {
            return messages.getString(key);
        } catch (MissingResourceException e) {
            LoggerUtil.warn(I18nUtil.class, "Missing i18n key: " + key);
            return "!" + key + "!"; // Fallback text so the app doesn't crash
        }
    }

}