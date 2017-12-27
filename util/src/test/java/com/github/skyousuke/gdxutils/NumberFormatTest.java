package com.github.skyousuke.gdxutils;

import org.junit.Assert;
import org.junit.Test;

public class NumberFormatTest extends GameTest{

    @Test
    public void formatDecimal() {
        Assert.assertEquals("12.2300000000", NumberFormat.formatDecimal(12.23, 10));
        Assert.assertEquals("12.2345", NumberFormat.formatDecimal(12.2345f, 4));
        Assert.assertEquals("12.23", NumberFormat.formatDecimal(12.23456789, 2));
        Assert.assertEquals("12.23", NumberFormat.formatDecimal(12.23456789f, 2));
        Assert.assertEquals("12.24", NumberFormat.formatDecimal(12.2388, 2));
        Assert.assertEquals("12.24", NumberFormat.formatDecimal(12.2388f, 2));

        Assert.assertEquals("-12.2300000000", NumberFormat.formatDecimal(-12.23, 10));
        Assert.assertEquals("-12.2345", NumberFormat.formatDecimal(-12.2345f, 4));
        Assert.assertEquals("-12.23", NumberFormat.formatDecimal(-12.23456789, 2));
        Assert.assertEquals("-12.23", NumberFormat.formatDecimal(-12.23456789f, 2));
        Assert.assertEquals("-12.24", NumberFormat.formatDecimal(-12.2388, 2));
        Assert.assertEquals("-12.24", NumberFormat.formatDecimal(-12.2388f, 2));

        Assert.assertEquals("12", NumberFormat.formatDecimal(12., 0));
        Assert.assertEquals("12", NumberFormat.formatDecimal(12.f, 0));
        Assert.assertEquals("12", NumberFormat.formatDecimal(12.23, 0));
        Assert.assertEquals("12", NumberFormat.formatDecimal(12.23f, 0));
        Assert.assertEquals("13", NumberFormat.formatDecimal(12.8, 0));
        Assert.assertEquals("13", NumberFormat.formatDecimal(12.8f, 0));

        Assert.assertEquals("-12", NumberFormat.formatDecimal(-12., 0));
        Assert.assertEquals("-12", NumberFormat.formatDecimal(-12.f, 0));
        Assert.assertEquals("-12", NumberFormat.formatDecimal(-12.23, 0));
        Assert.assertEquals("-12", NumberFormat.formatDecimal(-12.23f, 0));
        Assert.assertEquals("-13", NumberFormat.formatDecimal(-12.8, 0));
        Assert.assertEquals("-13", NumberFormat.formatDecimal(-12.8f, 0));
    }
}