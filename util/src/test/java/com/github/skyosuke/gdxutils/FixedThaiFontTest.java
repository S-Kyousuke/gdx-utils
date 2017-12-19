package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class FixedThaiFontTest extends GameTest {

    private static final char SARA_AM = 0x0E33;
    private static final char NIKHAHIT = 0x0E4D;
    private static final char SARA_AA = 0x0E32;
    private static final char MAI_THO = 0x0E49;

    @Test
    public void getGlyph_SaraAm_Null() {
        FixedThaiFont font = new FixedThaiFont("font/chiangsaen-16.fnt",
                new FixedThaiFont.FixedThaiFontParameter());
        assertNull(font.getData().getGlyph(SARA_AM));
        font.dispose();
    }

    @Test
    public void getGlyph_Nikhahit_NotNull() {
        FixedThaiFont font = new FixedThaiFont("font/chiangsaen-16.fnt",
                new FixedThaiFont.FixedThaiFontParameter());
        assertNotNull(font.getData().getGlyph(NIKHAHIT));
        font.dispose();
    }

    @Test
    public void draw_TextWithSaraAm_layoutHasNoSaraAm() {
        FixedThaiFont font = new FixedThaiFont("font/chiangsaen-16.fnt",
                new FixedThaiFont.FixedThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, "ฝึกอ่านคำที่ประสมสระอำ เช่น ทำดีไปทำไม", 0, 0);
        batch.end();
        for (int i = 0; i < layout.runs.first().glyphs.size; i++) {
            BitmapFont.Glyph glyph = layout.runs.first().glyphs.get(i);
            assertNotEquals(glyph.id, SARA_AM);
        }
        batch.dispose();
    }

    @Test
    public void draw_SaraAmWord_layoutHasCorrectGlyph() {
        FixedThaiFont font = new FixedThaiFont("font/chiangsaen-16.fnt",
                new FixedThaiFont.FixedThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, "ทำ", 0, 0);
        batch.end();
        assertTrue(layout.runs.first().glyphs.get(0).id == 'ท');
        assertTrue(layout.runs.first().glyphs.get(1).id == NIKHAHIT);
        assertTrue(layout.runs.first().glyphs.get(2).id == SARA_AA);
        batch.dispose();
    }

    @Test
    public void draw_SaraAmWordWithToneMark_layoutHasCorrectGlyph() {
        FixedThaiFont font = new FixedThaiFont("font/chiangsaen-16.fnt",
                new FixedThaiFont.FixedThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, "กล้ำ", 0, 0);
        batch.end();
        assertTrue(layout.runs.first().glyphs.get(0).id == 'ก');
        assertTrue(layout.runs.first().glyphs.get(1).id == 'ล');
        assertTrue(layout.runs.first().glyphs.get(2).id == NIKHAHIT);
        assertTrue(layout.runs.first().glyphs.get(3).id == MAI_THO);
        assertTrue(layout.runs.first().glyphs.get(4).id == SARA_AA);
        batch.dispose();
    }

}