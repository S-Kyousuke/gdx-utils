package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.skyousuke.gdxutils.font.ThaiFonts;
import com.github.skyousuke.gdxutils.font.ThaiUnicode;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ThaiFontTest extends GameTest {

    @Test
    public void getGlyph_Nikhahit_NotNull() {
        BitmapFont font = ThaiFonts.CHIANGSAEN_16.createFont();
        assertNotNull(font.getData().getGlyph(ThaiUnicode.NIKHAHIT));
        font.dispose();
    }

    @Test
    public void draw_SaraAmWord_layoutHasSaraAmGlyph() {
        BitmapFont font = ThaiFonts.CHIANGSAEN_16.createFont();
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, "ทำ", 0, 0);
        batch.end();
        assertEquals('ท', layout.runs.first().glyphs.get(0).id);
        assertEquals(ThaiUnicode.SARA_AM, layout.runs.first().glyphs.get(1).id);
        batch.dispose();
    }

    @Test
    public void draw_SaraAmWordWithToneMark_layoutHasCorrectGlyph() {
        BitmapFont font = ThaiFonts.CHIANGSAEN_16.createFont();
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        batch.begin();
        GlyphLayout layout = font.draw(batch, "กล้ำ", 0, 0);
        batch.end();
        assertEquals('ก', layout.runs.first().glyphs.get(0).id);
        assertEquals('ล', layout.runs.first().glyphs.get(1).id);
        assertEquals(ThaiUnicode.MAI_THO, layout.runs.first().glyphs.get(2).id);
        assertEquals(ThaiUnicode.SARA_AM, layout.runs.first().glyphs.get(3).id);
        batch.dispose();
    }

}