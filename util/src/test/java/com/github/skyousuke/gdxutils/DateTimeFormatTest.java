package com.github.skyousuke.gdxutils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;

public class DateTimeFormatTest extends GameTest {

    @Test
    public void format() {
        Date date = new Date(1515487984000L); //01/09/2018 @ 08:53:04(UTC)

        Locale.setDefault(LocaleUtils.toLocale("en_US"));
        Assert.assertEquals("08:53:04", DateTimeFormat.format(date, "HH:mm:ss", 0));
        Assert.assertEquals("15:53:04", DateTimeFormat.format(date, "HH:mm:ss", 7 * 60));
        Assert.assertEquals("08:53:04 +00:00", DateTimeFormat.format(date, "HH:mm:ss Z", 0));
        Assert.assertEquals("15:53:04 +07:00", DateTimeFormat.format(date, "HH:mm:ss Z", 7 * 60));
        Assert.assertEquals("15:53:04 01 +07:00", DateTimeFormat.format(date, "HH:mm:ss MM Z", 7 * 60));
        Assert.assertEquals("15:53:04 Jan +07:00", DateTimeFormat.format(date, "HH:mm:ss MMM Z", 7 * 60));
        Assert.assertEquals("15:53:04 January +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM Z", 7 * 60));
        Assert.assertEquals("15:53:04 January 18 +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM yy Z", 7 * 60));
        Assert.assertEquals("15:53:04 January 2018 +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM yyyy Z", 7 * 60));

        Locale.setDefault(LocaleUtils.toLocale("th_TH"));
        Assert.assertEquals("08:53:04", DateTimeFormat.format(date, "HH:mm:ss", 0));
        Assert.assertEquals("15:53:04", DateTimeFormat.format(date, "HH:mm:ss", 7 * 60));
        Assert.assertEquals("08:53:04 +00:00", DateTimeFormat.format(date, "HH:mm:ss Z", 0));
        Assert.assertEquals("15:53:04 +07:00", DateTimeFormat.format(date, "HH:mm:ss Z", 7 * 60));
        Assert.assertEquals("15:53:04 01 +07:00", DateTimeFormat.format(date, "HH:mm:ss MM Z", 7 * 60));
        Assert.assertEquals("15:53:04 ม.ค. +07:00", DateTimeFormat.format(date, "HH:mm:ss MMM Z", 7 * 60));
        Assert.assertEquals("15:53:04 มกราคม +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM Z", 7 * 60));
        Assert.assertEquals("15:53:04 มกราคม 61 +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM yy Z", 7 * 60));
        Assert.assertEquals("15:53:04 มกราคม 2561 +07:00", DateTimeFormat.format(date, "HH:mm:ss MMMM yyyy Z", 7 * 60));
    }
}