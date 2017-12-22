package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.skyosuke.gdxutils.thaifont.ThaiFont;
import com.github.skyosuke.gdxutils.thaifont.ThaiString;
import com.github.skyosuke.gdxutils.thaifont.ThaiUnicode;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ThaiFontTest extends GameTest {

    @Test
    public void getGlyph_SaraAm_Null() {
        ThaiFont font = new ThaiFont("font/chiangsaen-16.fnt",
                new ThaiFont.ThaiFontParameter());
        assertNull(font.getData().getGlyph(ThaiUnicode.SARA_AM));
        font.dispose();
    }

    @Test
    public void getGlyph_Nikhahit_NotNull() {
        ThaiFont font = new ThaiFont("font/chiangsaen-16.fnt",
                new ThaiFont.ThaiFontParameter());
        assertNotNull(font.getData().getGlyph(ThaiUnicode.NIKHAHIT));
        font.dispose();
    }

    @Test
    public void draw_TextWithSaraAm_layoutHasNoSaraAm() {
        ThaiFont font = new ThaiFont("font/chiangsaen-16.fnt",
                new ThaiFont.ThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, new ThaiString("ฝึกอ่านคำที่ประสมสระอำ เช่น ทำดีไปทำไม"), 0, 0);
        batch.end();
        for (int i = 0; i < layout.runs.first().glyphs.size; i++) {
            BitmapFont.Glyph glyph = layout.runs.first().glyphs.get(i);
            assertNotEquals(glyph.id, ThaiUnicode.SARA_AM);
        }
        batch.dispose();
    }

    @Test
    public void draw_SaraAmWord_layoutHasCorrectGlyph() {
        ThaiFont font = new ThaiFont("font/chiangsaen-16.fnt",
                new ThaiFont.ThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, new ThaiString("ทำ"), 0, 0);
        batch.end();
        assertTrue(layout.runs.first().glyphs.get(0).id == 'ท');
        assertTrue(layout.runs.first().glyphs.get(1).id == ThaiUnicode.NIKHAHIT);
        assertTrue(layout.runs.first().glyphs.get(2).id == ThaiUnicode.SARA_AA);
        batch.dispose();
    }

    @Test
    public void draw_SaraAmWordWithToneMark_layoutHasCorrectGlyph() {
        ThaiFont font = new ThaiFont("font/chiangsaen-16.fnt",
                new ThaiFont.ThaiFontParameter());
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, new ThaiString("กล้ำ"), 0, 0);
        batch.end();
        assertTrue(layout.runs.first().glyphs.get(0).id == 'ก');
        assertTrue(layout.runs.first().glyphs.get(1).id == 'ล');
        assertTrue(layout.runs.first().glyphs.get(2).id == ThaiUnicode.NIKHAHIT);
        assertTrue(layout.runs.first().glyphs.get(3).id == ThaiUnicode.MAI_THO);
        assertTrue(layout.runs.first().glyphs.get(4).id == ThaiUnicode.SARA_AA);
        batch.dispose();
    }

}