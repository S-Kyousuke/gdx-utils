package com.github.skyousuke.gdxutils;

import java.util.Locale;

public class LocaleUtils {

    private LocaleUtils() { }

    public static Locale toLocale(String localeString) {
        if (localeString == null)
            return null;

        localeString = localeString.trim();

        if (localeString.equalsIgnoreCase("default"))
            return Locale.getDefault();

        int languageIndex = localeString.indexOf('_');

        String language;
        if (languageIndex == -1)
            return new Locale(localeString, "");
        else
            language = localeString.substring(0, languageIndex);

        int countryIndex = localeString.indexOf('_', languageIndex + 1);

        String country;
        if (countryIndex == -1) {
            country = localeString.substring(languageIndex + 1);
            return new Locale(language, country);
        } else {
            country = localeString.substring(languageIndex + 1, countryIndex);
            String variant = localeString.substring(countryIndex + 1);
            return new Locale(language, country, variant);
        }
    }

}
