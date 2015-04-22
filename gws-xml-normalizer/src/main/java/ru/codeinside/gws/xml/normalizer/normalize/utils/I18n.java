package ru.codeinside.gws.xml.normalizer.normalize.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Internationalization (I18N) pack.
 *
 * @author Christian Geuer-Pollmann
 */
public class I18n {

    /**
     * Field NOT_INITIALIZED_MSG
     */
    public static final String NOT_INITIALIZED_MSG =
            "You must initialize the xml-security library correctly before you use it. "
                    + "Call the static method \"org.apache.xml.security.Init.init();\" to do that "
                    + "before you use any functionality from that library.";

    /**
     * Field resourceBundle
     */
    private static ResourceBundle resourceBundle;

    /**
     * Field alreadyInitialized
     */
    private static boolean alreadyInitialized = false;

    /**
     * Constructor I18n
     */
    private I18n() {
        // we don't allow instantiation
    }

    /**
     * Method translate
     * <p/>
     * translates a message ID into an internationalized String, see alse
     * <CODE>XMLSecurityException.getExceptionMEssage()</CODE>. The strings are
     * stored in the <CODE>ResourceBundle</CODE>, which is identified in
     * <CODE>exceptionMessagesResourceBundleBase</CODE>
     *
     * @param message
     * @param args    is an <CODE>Object[]</CODE> array of strings which are inserted into
     *                the String which is retrieved from the <CODE>ResouceBundle</CODE>
     * @return message translated
     */
    public static String translate(String message, Object[] args) {
        return getExceptionMessage(message, args);
    }

    /**
     * Method translate
     * <p/>
     * translates a message ID into an internationalized String, see also
     * <CODE>XMLSecurityException.getExceptionMessage()</CODE>
     *
     * @param message
     * @return message translated
     */
    public static String translate(String message) {
        return getExceptionMessage(message);
    }

    /**
     * Method getExceptionMessage
     *
     * @param msgID
     * @return message translated
     */
    public static String getExceptionMessage(String msgID) {
        try {
            return resourceBundle.getString(msgID);
        } catch (Throwable t) {
            return I18n.NOT_INITIALIZED_MSG;
        }
    }

    /**
     * Method getExceptionMessage
     *
     * @param msgID
     * @param originalException
     * @return message translated
     */
    public static String getExceptionMessage(String msgID, Exception originalException) {
        try {
            Object exArgs[] = {originalException.getMessage()};
            return MessageFormat.format(resourceBundle.getString(msgID), exArgs);
        } catch (Throwable t) {
            return I18n.NOT_INITIALIZED_MSG;
        }
    }

    /**
     * Method getExceptionMessage
     *
     * @param msgID
     * @param exArgs
     * @return message translated
     */
    public static String getExceptionMessage(String msgID, Object exArgs[]) {
        try {
            return MessageFormat.format(resourceBundle.getString(msgID), exArgs);
        } catch (Throwable t) {
            return I18n.NOT_INITIALIZED_MSG;
        }
    }

    /**
     * Method init
     *
     * @param languageCode
     * @param countryCode
     */
    public synchronized static void init(String languageCode, String countryCode) {
        if (alreadyInitialized) {
            return;
        }

        I18n.resourceBundle =
                ResourceBundle.getBundle(
                        Constants.exceptionMessagesResourceBundleBase,
                        new Locale(languageCode, countryCode)
                );
        alreadyInitialized = true;
    }
}

