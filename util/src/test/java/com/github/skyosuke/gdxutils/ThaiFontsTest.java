package com.github.skyosuke.gdxutils;

import com.github.skyosuke.gdxutils.thaifont.ThaiFonts;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThaiFontsTest extends GameTest {

    @Test
    public void createFont_NullAsParam_ThrowException() {
        try {
            ThaiFonts.INSTANCE.createFont(null);
            fail("this method didn't throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("font cannot be null!", e.getMessage());
        }
    }

    @Test
    public void createFont_FontChaingSaen16_NotNull() {
        assertNotNull(ThaiFonts.INSTANCE.createFont(ThaiFonts.Font.CHIANGSAEN_16));
    }

    @Test
    public void createFont_FontChaingSaen64_NotNull() {
        assertNotNull(ThaiFonts.INSTANCE.createFont(ThaiFonts.Font.CHIANGSAEN_64));
    }

}