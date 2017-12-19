package com.github.skyosuke.gdxutils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThaiFontTest extends GameTest {

    @Test
    public void createFont_NullAsParam_ThrowException() {
        try {
            ThaiFont.INSTANCE.createFont(null);
            fail("this method didn't throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("font cannot be null!", e.getMessage());
        }
    }

    @Test
    public void createFont_FontChaingSaen16_NotNull() {
        assertNotNull(ThaiFont.INSTANCE.createFont(ThaiFont.Font.CHIANGSAEN_16));
    }

    @Test
    public void createFont_FontChaingSaen64_NotNull() {
        assertNotNull(ThaiFont.INSTANCE.createFont(ThaiFont.Font.CHIANGSAEN_64));
    }

}