package com.github.skyousuke.gdxutils;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import java.text.DecimalFormat;

/**
 * The utility class contains any objects that use for changing any number to specific strings.
 */
public class NumberFormat {

    private NumberFormat() {}

    /**
     * Format an integer to produce an integer string with thousands separator.
     *
     * @param value an integer to be formatted.
     * @return integer string with thousands separator.
     */
    public static String formatInteger(long value) {
        final String integerString = Long.toString(value);
        final int length = integerString.length();
        final char[] buffer = new char[length + ((length -1) / 3)];
        int arrayIndex = 0;
        for (int i = 0; i < length; i++) {
            final boolean appendComma;
            if (value >= 0)
                appendComma = (length - i) % 3 == 0 && i > 0;
             else
                appendComma = (length - (i - 1)) % 3 == 0 && i > 1;

            if (appendComma) {
                buffer[arrayIndex] = ',';
                arrayIndex++;
            }
            buffer[arrayIndex] = integerString.charAt(i);
            arrayIndex++;
        }
        return new String(buffer);
    }

    /**
     * Format a decimal to produce an decimal string with thousands separator and a specific decimal places.
     *
     * @param value  a decimal to be formatted.
     * @param places a number of decimal places.
     * @return decimal string with thousands separator.
     */
    public static String formatDecimal(double value, int places) {
        if (Double.isInfinite(value))
            return value > 0 ? "Infinity" : "-Infinity";
        if (places == 0)
            return formatInteger(Math.round(value));

        char[] patternBuffer = new char[6 + places];
        patternBuffer[0] = '#';
        patternBuffer[1] = ',';
        patternBuffer[2] = '#';
        patternBuffer[3] = '#';
        patternBuffer[4] = '0';
        patternBuffer[5] = '.';
        for (int i = 0; i < places; i++) {
            patternBuffer[6 + i] = '0';
        }
        final String pattern = new String(patternBuffer);

        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            com.google.gwt.i18n.client.NumberFormat formatter =
                    com.google.gwt.i18n.client.NumberFormat.getFormat(pattern);
            return formatter.format(value);
        } else {
            DecimalFormat formatter = new DecimalFormat(pattern);
            return formatter.format(value);
        }
    }
}