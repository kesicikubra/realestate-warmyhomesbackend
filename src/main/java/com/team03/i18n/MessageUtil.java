package com.team03.i18n;

import com.team03.config.MessageSourceConfig;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;


public class MessageUtil {

    public static String getMessage(String message, Object... dynamicValues) {
        Locale locale = LocaleContextHolder.getLocale();
        String formattedMessage = MessageSourceConfig.messageSource().getMessage(message, null, locale);
        if (dynamicValues != null && dynamicValues.length > 0) {
            return MessageFormat.format(formattedMessage, dynamicValues);
        } else {
            return formattedMessage;
        }
    }

    public static String getMessage(String message, Locale locale, Object... dynamicValues) {
        String formattedMessage = MessageSourceConfig.messageSource().getMessage(message, null, locale);
        if (dynamicValues != null && dynamicValues.length > 0) {
            return MessageFormat.format(formattedMessage, dynamicValues);
        } else {
            return formattedMessage;
        }
    }
}
