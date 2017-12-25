package com.github.skyosuke.gdxutils;

import com.github.skyosuke.gdxutils.thaifont.ThaiFonts;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThaiFontsTest extends GameTest {

    @Test
    public void createFont_FontChaingSaen16_NotNull() {
        assertNotNull(ThaiFonts.CHIANGSAEN_16.createFont());
    }

    @Test
    public void createFont_FontChaingSaen20_NotNull() {
        assertNotNull(ThaiFonts.CHIANGSAEN_20.createFont());
    }

    @Test
    public void createFont_FontChaingSaen64_NotNull() {
        assertNotNull(ThaiFonts.CHIANGSAEN_64.createFont());
    }

}