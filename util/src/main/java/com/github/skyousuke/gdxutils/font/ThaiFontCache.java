package com.github.skyousuke.gdxutils.font;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.IntMap;

/**
 * {@link BitmapFontCache} for a {@link ThaiFont}
 *
 * @author skyousuke
 */
public class ThaiFontCache extends BitmapFontCache {

    private final IntMap<IntMap<BitmapFont.Glyph>> fixedGlyphs;

    public ThaiFontCache(BitmapFont font, ThaiFontLoader.ThaiFontParameter parameter) {
        super(font);
        fixedGlyphs = new IntMap<IntMap<BitmapFont.Glyph>>(16);
        if (parameter != null)
            config(parameter);
    }

    @Override
    public void addText(GlyphLayout layout, float x, float y) {
        fixLayout(layout);
        super.addText(layout, x, y);
    }

    public void config(ThaiFontLoader.ThaiFontParameter parameter) {
        configHorizontalOffset(parameter.horizontalOffset);
        configVerticalOffset(parameter.verticalOffset);
        configYoYing(parameter.yoYingTrim);
        configThoThan(parameter.thoThanTrim);
    }

    public void configHorizontalOffset(int horizontalOffset) {
        setGlyphHorizontalOffset(ThaiUnicode.MAI_HAN_AKAT, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.SARA_I, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.SARA_II, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.SARA_UE, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.SARA_UEE, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.NIKHAHIT, horizontalOffset);

        setGlyphHorizontalOffset(ThaiUnicode.MAI_EK, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.MAI_THO, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.MAI_TRI, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.MAI_CHATTAWA, horizontalOffset);
        setGlyphHorizontalOffset(ThaiUnicode.THANTHAKHAT, horizontalOffset);
    }

    public void configVerticalOffset(int verticalOffset) {
        setGlyphVerticalOffset(ThaiUnicode.MAI_EK, verticalOffset);
        setGlyphVerticalOffset(ThaiUnicode.MAI_THO, verticalOffset);
        setGlyphVerticalOffset(ThaiUnicode.MAI_TRI, verticalOffset);
        setGlyphVerticalOffset(ThaiUnicode.MAI_CHATTAWA, verticalOffset);
        setGlyphVerticalOffset(ThaiUnicode.THANTHAKHAT, verticalOffset);
    }

    public void configYoYing(int yoYingTrim) {
        BitmapFont.Glyph fixedGlyph = getGlyph(ThaiUnicode.YO_YING, CharacterTypes.NO_LOWER_CURVES);
        fixedGlyph.yoffset = getDefaultGlyph(ThaiUnicode.YO_YING).yoffset + yoYingTrim;
        fixedGlyph.height = getDefaultGlyph(ThaiUnicode.YO_YING).height - yoYingTrim;
        getFont().getData().setGlyphRegion(fixedGlyph, getFont().getRegion());
    }

    public void configThoThan(int thoThanTrim) {
        BitmapFont.Glyph fixedGlyph = getGlyph(ThaiUnicode.THO_THAN, CharacterTypes.NO_LOWER_CURVES);
        fixedGlyph.yoffset = getDefaultGlyph(ThaiUnicode.THO_THAN).yoffset + thoThanTrim;
        fixedGlyph.height = getDefaultGlyph(ThaiUnicode.THO_THAN).height - thoThanTrim;
        getFont().getData().setGlyphRegion(fixedGlyph, getFont().getRegion());
    }

    private void setGlyphHorizontalOffset(int codePoint, int horizontalOffset) {
        getGlyph(codePoint, CharacterTypes.ABOVE_LEFT).xoffset = getDefaultGlyph(codePoint).xoffset + horizontalOffset;
        getGlyph(codePoint, CharacterTypes.TOP_LEFT).xoffset = getDefaultGlyph(codePoint).xoffset + horizontalOffset;
    }

    private void setGlyphVerticalOffset(int codePoint, int verticalOffset) {
        if (verticalOffset < 0) {
            getGlyph(codePoint, CharacterTypes.TOP).yoffset = getDefaultGlyph(codePoint).yoffset;
            getGlyph(codePoint, CharacterTypes.TOP_LEFT).yoffset = getDefaultGlyph(codePoint).yoffset;
            getGlyph(codePoint, CharacterTypes.ABOVE).yoffset = getDefaultGlyph(codePoint).yoffset + verticalOffset;
            getGlyph(codePoint, CharacterTypes.ABOVE_LEFT).yoffset = getDefaultGlyph(codePoint).yoffset + verticalOffset;
        } else {
            getGlyph(codePoint, CharacterTypes.TOP).yoffset = getDefaultGlyph(codePoint).yoffset + verticalOffset;
            getGlyph(codePoint, CharacterTypes.TOP_LEFT).yoffset = getDefaultGlyph(codePoint).yoffset + verticalOffset;
            getGlyph(codePoint, CharacterTypes.ABOVE).yoffset = getDefaultGlyph(codePoint).yoffset;
            getGlyph(codePoint, CharacterTypes.ABOVE_LEFT).yoffset = getDefaultGlyph(codePoint).yoffset;
        }
    }

    private void fixLayout(GlyphLayout layout) {
        for (int i = 0; i < layout.runs.size; i++) {
            GlyphLayout.GlyphRun run = layout.runs.get(i);
            for (int j = 0; j < run.glyphs.size; j++) {
                fixUpper(run, j);
                fixLower(run, j);
            }
        }
    }

    private BitmapFont.Glyph getGlyph(int codePoint, int characterType) {
        if (characterType == CharacterTypes.DEFAULT)
            return getDefaultGlyph(codePoint);

        IntMap<BitmapFont.Glyph> glyphs = fixedGlyphs.get(codePoint);
        if (glyphs == null) {
            glyphs = new IntMap<BitmapFont.Glyph>(4);
            fixedGlyphs.put(codePoint, glyphs);
        }
        BitmapFont.Glyph fixedGlyph = glyphs.get(characterType);
        if (fixedGlyph == null) {
            fixedGlyph = cloneGlyph(getDefaultGlyph(codePoint));
            glyphs.put(characterType, fixedGlyph);
        }
        return fixedGlyph;
    }

    private BitmapFont.Glyph getDefaultGlyph(int codePoint) {
        final BitmapFont.Glyph glyph = getFont().getData().getGlyph((char) codePoint);
        return glyph != null ? glyph : getFont().getData().missingGlyph;
    }

    private int getGlyphId(GlyphLayout.GlyphRun run, int runIndex) {
        if (runIndex < 0 || runIndex > run.glyphs.size - 1)
            return -1;

        final BitmapFont.Glyph glyph = run.glyphs.get(runIndex);
        if (glyph != null)
            return glyph.id;
        else
            return -1;
    }

    private void fixUpper(GlyphLayout.GlyphRun run, int runIndex) {
        final int penultimateId = getGlyphId(run, runIndex - 2);
        final int previousId = getGlyphId(run, runIndex - 1);
        final int id = getGlyphId(run, runIndex);
        final int nextId = getGlyphId(run, runIndex + 1);

        final int characterType = findCharacterType(penultimateId, previousId, id, nextId);
        run.glyphs.set(runIndex, getGlyph(id, characterType));
    }

    private void fixLower(GlyphLayout.GlyphRun run, int runIndex) {
        final int id = getGlyphId(run, runIndex);
        if (!isBottomCharacter(id) || runIndex == 0)
            return;

        final int previousId = getGlyphId(run, runIndex - 1);
        if (previousId == ThaiUnicode.YO_YING || previousId == ThaiUnicode.THO_THAN)
            run.glyphs.set(runIndex - 1, getGlyph(previousId, CharacterTypes.NO_LOWER_CURVES));
    }

    private static int findCharacterType(int penultimateId, int previousId, int id, int nextId) {
        if (isAboveCharacter(id)) {
            return findAboveCharacterType(previousId);
        }
        if (isTopCharacter(id)) {
            return findTopCharacterType(penultimateId, previousId, nextId);
        }
        return CharacterTypes.DEFAULT;
    }

    private static int findAboveCharacterType(int previousId) {
        if (isTallCharacter(previousId))
            return CharacterTypes.ABOVE_LEFT;
        else
            return CharacterTypes.ABOVE;
    }

    private static int findTopCharacterType(int penultimateId, int previousId, int nextTd) {
        if (isAboveCharacter(previousId) || nextTd == ThaiUnicode.SARA_AM) {
            if (isTallCharacter(penultimateId))
                return CharacterTypes.TOP_LEFT;
            else
                return CharacterTypes.TOP;
        }
        return findAboveCharacterType(previousId);
    }

    private static boolean isTopCharacter(int id) {
        return id == ThaiUnicode.MAI_EK
                || id == ThaiUnicode.MAI_THO
                || id == ThaiUnicode.MAI_TRI
                || id == ThaiUnicode.MAI_CHATTAWA
                || id == ThaiUnicode.THANTHAKHAT;
    }

    private static boolean isAboveCharacter(int id) {
        return id == ThaiUnicode.MAI_HAN_AKAT
                || id == ThaiUnicode.NIKHAHIT
                || id == ThaiUnicode.SARA_I
                || id == ThaiUnicode.SARA_II
                || id == ThaiUnicode.SARA_UE
                || id == ThaiUnicode.SARA_UEE;
    }

    private static boolean isBottomCharacter(int id) {
        return id == ThaiUnicode.SARA_U
                || id == ThaiUnicode.SARA_UU;
    }

    private static boolean isTallCharacter(int id) {
        return id == ThaiUnicode.PO_PLA
                || id == ThaiUnicode.FO_FA
                || id == ThaiUnicode.FO_FAN;
    }


    private static BitmapFont.Glyph cloneGlyph(BitmapFont.Glyph glyph) {
        BitmapFont.Glyph newGlyph = new BitmapFont.Glyph();
        newGlyph.id = glyph.id;
        newGlyph.srcX = glyph.srcX;
        newGlyph.srcY = glyph.srcY;
        newGlyph.width = glyph.width;
        newGlyph.height = glyph.height;
        newGlyph.u = glyph.u;
        newGlyph.v = glyph.v;
        newGlyph.u2 = glyph.u2;
        newGlyph.v2 = glyph.v2;
        newGlyph.xoffset = glyph.xoffset;
        newGlyph.yoffset = glyph.yoffset;
        newGlyph.xadvance = glyph.xadvance;
        if (glyph.kerning != null) {
            newGlyph.kerning = new byte[glyph.kerning.length][];
            for (int i = 0; i < glyph.kerning.length; i++) {
                byte[] innerArray = glyph.kerning[i];
                if (innerArray != null) {
                    int innerSize = innerArray.length;
                    newGlyph.kerning[i] = new byte[innerSize];
                    System.arraycopy(innerArray, 0, newGlyph.kerning[i], 0, innerSize);
                }
            }
        }
        newGlyph.fixedWidth = glyph.fixedWidth;
        return newGlyph;
    }

    private static class CharacterTypes {
        private static final int DEFAULT = 0;
        private static final int ABOVE = 1;
        private static final int ABOVE_LEFT = 2;
        private static final int TOP = 3;
        private static final int TOP_LEFT = 4;
        private static final int NO_LOWER_CURVES = 5;

        private CharacterTypes() {
        }
    }
}
