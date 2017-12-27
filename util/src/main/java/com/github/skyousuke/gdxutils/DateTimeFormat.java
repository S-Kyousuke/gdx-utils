package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Formats and parses dates and times in default locale using locale-sensitive patterns.
 * <p style="color:red">Warning: Locale in html platform (GWT) is determined at compile time</p>
 * <h2><i>Patterns</i></h2>
 * <table border=1 cellspacing=0 cellpadding=5 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 * <tr>
 * <th align=left>Letter
 * <th align=left>Date or Time Component
 * <th align=left>Presentation
 * <th align=left>Examples
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td>Text
 * <td><code>AD</code>
 * <tr>
 * <td><code>y</code>
 * <td>Year
 * <td>Number
 * <td><code>1996</code>
 * <tr>
 * <td><code>M</code>
 * <td>Month in year
 * <td>Text or Number
 * <td><code>July</code>; <code>Jul</code>; <code>07</code>
 * <tr>
 * <td><code>d</code>
 * <td>Day in month
 * <td>Number
 * <td><code>10</code>
 * <tr>
 * <td><code>E</code>
 * <td>Day in week
 * <td>Text
 * <td><code>Tuesday</code>; <code>Tue</code>
 * <tr>
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td>Text
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td>Number
 * <td><code>0</code>
 * <tr>
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td>Number
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td>Number
 * <td><code>0</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td>Number
 * <td><code>30</code>
 * <tr>
 * <td><code>s</code>
 * <td>Second in minute
 * <td>Number
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td>Number
 * <td><code>978</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td>ISO 8601 time zone
 * <td><code>-08;00</code>
 * <tr>
 * <td><code>'</code>
 * <td>escape for text
 * <td>Delimiter
 * <td><code>'Date='</code>
 * <tr>
 * <td><code>''</code>
 * <td>single quote
 * <td>Literal
 * <td><code>'o''clock'</code>
 * </table>
 * <p>
 * The number of pattern letters influences the format, as follows:
 * </p>
 * <dl>
 * <dt>Text</dt>
 * <dd>if 4 or more, then use the full form; if less than 4, use short or
 * abbreviated form if it exists (e.g., <code>"EEEE"</code> produces
 * <code>"Monday"</code>, <code>"EEE"</code> produces <code>"Mon"</code>)</dd>
 * <dt>Number</dt>
 * <dd>the minimum number of digits. Shorter numbers are zero-padded to this
 * amount (e.g. if <code>"m"</code> produces <code>"6"</code>, <code>"mm"</code>
 * produces <code>"06"</code>). Year is handled specially; that is, if the count
 * of 'y' is 2, the Year will be truncated to 2 digits. (e.g., if
 * <code>"yyyy"</code> produces <code>"1997"</code>, <code>"yy"</code> produces
 * <code>"97"</code>.) Unlike other fields, fractional seconds are padded on the
 * right with zero.</dd>
 * <dt>Text or Number</dt>
 * <dd>3 or more, use text, otherwise use number. (e.g. <code>"M"</code>
 * produces <code>"1"</code>, <code>"MM"</code> produces <code>"01"</code>,
 * <code>"MMM"</code> produces <code>"Jan"</code>, and <code>"MMMM"</code>
 * produces <code>"January"</code>.  Some pattern letters also treat a count
 * of 5 specially, meaning a single-letter abbreviation: <code>L</code>,
 * <code>M</code>, <code>E</code>, and <code>c</code>.</dd>
 * </dl>
 * <p>
 * Any characters in the pattern that are not in the ranges of ['<code>a</code>
 * '..'<code>z</code>'] and ['<code>A</code>'..'<code>Z</code>'] will be treated
 * as quoted text. For instance, characters like '<code>:</code>', '
 * <code>.</code>', '<code> </code>' (space), '<code>#</code>' and '
 * <code>@</code>' will appear in the resulting time text even they are not
 * embraced within single quotes.
 * </p>
 */
public class DateTimeFormat {

    private static final ObjectMap<String, com.google.gwt.i18n.client.DateTimeFormat> gwtFormatters;
    private static final ObjectMap<String, ObjectMap<Locale, SimpleDateFormat>> formatters;

    static {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            gwtFormatters = new ObjectMap<String, com.google.gwt.i18n.client.DateTimeFormat>(3, 0.8f);
            formatters = null;
        } else {
            gwtFormatters = null;
            formatters = new ObjectMap<String, ObjectMap<Locale, SimpleDateFormat>>(3, 0.8f);
        }
    }

    private DateTimeFormat() {
    }

    /**
     * @param timeZoneOffsetInMinutes time zone offset in minutes to GMT.
     */
    public static String format(Date date, String pattern, int timeZoneOffsetInMinutes) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            final com.google.gwt.i18n.client.DateTimeFormat formatter = getGwtFormatter(fixPatternGwt(pattern));
            return formatter.format(date, com.google.gwt.i18n.client.TimeZone.createTimeZone(-timeZoneOffsetInMinutes));
        } else {
            final SimpleDateFormat formatter = getFormatter(fixPattern(pattern, timeZoneOffsetInMinutes));
            formatter.setTimeZone(TimeZone.getTimeZone(TimeZone.getAvailableIDs(timeZoneOffsetInMinutes * 60 * 1000)[0]));
            return formatter.format(date);
        }
    }

    public static String format(Date date, String pattern) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            final com.google.gwt.i18n.client.DateTimeFormat formatter = getGwtFormatter(fixPatternGwt(pattern));
            return formatter.format(date);
        } else {
            final int timeZoneOffsetInMillis = TimeZone.getDefault().getOffset(date.getTime());
            final int timeZoneOffsetInMinutes = (timeZoneOffsetInMillis / 1000) / 60;
            final SimpleDateFormat formatter = getFormatter(fixPattern(pattern, timeZoneOffsetInMinutes));
            return formatter.format(date);
        }
    }

    private static String fixPattern(String pattern, int timeZoneOffsetInMinutes) {
        final Array<String> subPatterns = splitPattern(pattern);
        final StringBuilder sb = new StringBuilder(MathUtils.nextPowerOfTwo(pattern.length() + 5));
        for (int i = 0; i < subPatterns.size; i++) {
            String subPattern = subPatterns.get(i);
            if (subPattern.charAt(0) != '\'' && subPattern.charAt(subPattern.length() - 1) != '\'') {
                ensureNotContainsInvalidCharacter(subPattern);
                subPattern = replaceTimeZoneSymbol(subPattern, timeZoneOffsetInMinutes);
            }
            sb.append(subPattern);
        }
        return sb.toString();
    }

    private static String fixPatternGwt(String pattern) {
        final Array<String> subPatterns = splitPattern(pattern);
        final StringBuilder sb = new StringBuilder(MathUtils.nextPowerOfTwo(pattern.length() + 5));
        for (int i = 0; i < subPatterns.size; i++) {
            String subPattern = subPatterns.get(i);
            if (subPattern.charAt(0) != '\'' && subPattern.charAt(subPattern.length() - 1) != '\'') {
                ensureNotContainsInvalidCharacter(subPattern);
                subPattern = replaceTimeZoneSymbolGwt(subPattern);
            }
            sb.append(subPattern);
        }
        return sb.toString();
    }

    private static String replaceTimeZoneSymbol(String pattern, int timeZoneOffsetInMinutes) {
        final int patternLength = pattern.length();
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < patternLength) {
            char c = pattern.charAt(i);
            if (c == 'Z') {
                if (timeZoneOffsetInMinutes != 0)
                    sb.append("XXX");
                else
                    sb.append("\'+00:00\'");
            }
            while (c == 'Z' && i + 1 < patternLength) {
                i++;
                c = pattern.charAt(i);
            }
            if (c != 'Z')
                sb.append(c);
            i++;
        }
        return sb.toString();
    }

    private static String replaceTimeZoneSymbolGwt(String pattern) {
        final int patternLength = pattern.length();
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < patternLength) {
            char c = pattern.charAt(i);
            if (c == 'Z') {
                sb.append("ZZZ");
            }
            while (c == 'Z' && i + 1 < patternLength) {
                i++;
                c = pattern.charAt(i);
            }
            if (c != 'Z')
                sb.append(c);
            i++;
        }
        return sb.toString();
    }

    private static void ensureNotContainsInvalidCharacter(String pattern) {
        final char[] unSupportedCharacters = new char[]{'L', 'Y', 'W', 'w', 'D', 'F', 'u', 'h', 'z', 'v', 'X'};

        for (char c : unSupportedCharacters) {
            if (pattern.indexOf(c) != -1)
                throw new IllegalArgumentException("Illegal pattern character " + c);
        }
    }

    private static Array<String> splitPattern(String pattern) {
        final int patternLength = pattern.length();
        final StringBuilder sb = new StringBuilder();
        final Array<String> subPattern = new Array<String>();
        int i = 0;
        while (i < patternLength) {
            char c = pattern.charAt(i);
            if (c == '\'') {
                subPattern.add(sb.toString());
                sb.setLength(0);
                sb.append(c);
                do {
                    i++;
                    if (i < patternLength) {
                        c = pattern.charAt(i);
                        sb.append(c);
                    } else {
                        throw new IllegalArgumentException("Illegal pattern: unterminated quote");
                    }
                } while (c != '\'');
                subPattern.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
            i++;
        }
        if (sb.length > 0) {
            subPattern.add(sb.toString());
        }
        return subPattern;
    }

    private static SimpleDateFormat getFormatter(String pattern) {
        ObjectMap<Locale, SimpleDateFormat> allLocaleFormatters = formatters.get(pattern);
        if (allLocaleFormatters == null) {
            allLocaleFormatters = new ObjectMap<Locale, SimpleDateFormat>(1, 0.8f);
            formatters.put(pattern, allLocaleFormatters);
        }
        final Locale defaultLocale = Locale.getDefault();
        SimpleDateFormat formatter = allLocaleFormatters.get(defaultLocale);
        if (formatter == null) {
            formatter = new SimpleDateFormat(pattern, defaultLocale);
            allLocaleFormatters.put(defaultLocale, formatter);
        }
        return formatter;
    }

    private static com.google.gwt.i18n.client.DateTimeFormat getGwtFormatter(String pattern) {
        com.google.gwt.i18n.client.DateTimeFormat formatter = gwtFormatters.get(pattern);
        if (formatter == null) {
            formatter = com.google.gwt.i18n.client.DateTimeFormat.getFormat(pattern);
            gwtFormatters.put(pattern, formatter);
        }
        return formatter;
    }

}
