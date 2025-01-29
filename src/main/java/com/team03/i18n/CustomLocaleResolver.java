package com.team03.i18n;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class CustomLocaleResolver extends AcceptHeaderLocaleResolver {
    private static final String DEFAULT_LOCALE = "en-US";
    private static final List<Locale> LOCALES = Arrays.asList(
            Locale.forLanguageTag("en"),
            Locale.forLanguageTag("no"),
            Locale.forLanguageTag("tr"),
            Locale.forLanguageTag("de"),
            Locale.forLanguageTag("fr"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            return Locale.forLanguageTag(DEFAULT_LOCALE);
        }
        var list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        return Locale.lookup(list, LOCALES);
    }

}
