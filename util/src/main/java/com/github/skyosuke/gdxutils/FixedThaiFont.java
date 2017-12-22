package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pools;

public class FixedThaiFont extends BitmapFont {

    private static final char YO_YING = 0x0E0D;
    private static final char THO_THAN = 0x0E10;

    private static final char MAI_HAN_AKAT = 0x0E31;
    private static final char SARA_I = 0x0E34;
    private static final char SARA_II = 0x0E35;
    private static final char SARA_UE = 0x0E36;
    private static final char SARA_UEE = 0x0E37;
    private static final char NIKHAHIT = 0x0E4D;

    private static final char SARA_AA = 0x0E32;
    private static final char SARA_AM = 0x0E33;

    private static final char SARA_U = 0x0E38;
    private static final char SARA_UU = 0x0E39;

    private static final char MAI_EK = 0x0E48;
    private static final char MAI_THO = 0x0E49;
    private static final char MAI_TRI = 0x0E4A;
    private static final char MAI_CHATTAWA = 0x0E4B;
    private static final char THANTHAKHAT = 0x0E4C;

    private static final char PO_PLA = 0x0E1B;
    private static final char FO_FA = 0x0E1D;
    private static final char FO_FAN = 0x0E1F;

    private final IntMap<IntMap<Glyph>> fixedGlyphs;
    private static char[] tempCharArray = new char[0];

    public FixedThaiFont(String fontFileInternalPath, FixedThaiFontParameter parameter) {
        super(Gdx.files.internal(fontFileInternalPath));
        fixedGlyphs = new IntMap<IntMap<Glyph>>(16);
        config(parameter);
    }

    public void config(FixedThaiFontParameter parameter) {
        configHorizontalOffset(parameter.horizontalOffset);
        configVerticalOffset(parameter.verticalOffset);
        configYoYing(parameter.yoYingTrim);
        configThoThan(parameter.thoThanTrim);
    }

    public void configHorizontalOffset(int horizontalOffset) {
        setGlyphHorizontalOffset(MAI_HAN_AKAT, horizontalOffset);
        setGlyphHorizontalOffset(SARA_I, horizontalOffset);
        setGlyphHorizontalOffset(SARA_II, horizontalOffset);
        setGlyphHorizontalOffset(SARA_UE, horizontalOffset);
        setGlyphHorizontalOffset(SARA_UEE, horizontalOffset);
        setGlyphHorizontalOffset(NIKHAHIT, horizontalOffset);

        setGlyphHorizontalOffset(MAI_EK, horizontalOffset);
        setGlyphHorizontalOffset(MAI_THO, horizontalOffset);
        setGlyphHorizontalOffset(MAI_TRI, horizontalOffset);
        setGlyphHorizontalOffset(MAI_CHATTAWA, horizontalOffset);
        setGlyphHorizontalOffset(THANTHAKHAT, horizontalOffset);
    }

    public void configVerticalOffset(int verticalOffset) {
        setGlyphVerticalOffset(MAI_EK, verticalOffset);
        setGlyphVerticalOffset(MAI_THO, verticalOffset);
        setGlyphVerticalOffset(MAI_TRI, verticalOffset);
        setGlyphVerticalOffset(MAI_CHATTAWA, verticalOffset);
        setGlyphVerticalOffset(THANTHAKHAT, verticalOffset);
    }

    public void configYoYing(int yoYingTrim) {
        Glyph fixedGlyph = getGlyph(YO_YING, CharacterTypes.NO_LOWER_CURVES);
        fixedGlyph.yoffset = getDefaultGlyph(YO_YING).yoffset + yoYingTrim;
        fixedGlyph.height = getDefaultGlyph(YO_YING).height - yoYingTrim;
        getData().setGlyphRegion(fixedGlyph, getRegion());
    }

    public void configThoThan(int thoThanTrim) {
        Glyph fixedGlyph = getGlyph(THO_THAN, CharacterTypes.NO_LOWER_CURVES);
        fixedGlyph.yoffset = getDefaultGlyph(THO_THAN).yoffset + thoThanTrim;
        fixedGlyph.height = getDefaultGlyph(THO_THAN).height - thoThanTrim;
        getData().setGlyphRegion(fixedGlyph, getRegion());
    }

    @Override
    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y) {
        getCache().clear();
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(this, replaceSaraAm(str));
        fixLayout(layout);
        getCache().addText(layout, x, y);
        getCache().draw(batch);
        return layout;
    }

    @Override
    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
        getCache().clear();
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(this, replaceSaraAm(str), getColor(), targetWidth, halign, wrap);
        fixLayout(layout);
        getCache().addText(layout, x, y);
        getCache().draw(batch);
        return layout;
    }

    @Override
    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap) {
        getCache().clear();
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(this, replaceSaraAm(str), start, end, getColor(), targetWidth, halign, wrap, null);
        fixLayout(layout);
        getCache().addText(layout, x, y);
        getCache().draw(batch);
        return layout;
    }

    @Override
    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap, String truncate) {
        getCache().clear();
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(this, replaceSaraAm(str), start, end, getColor(), targetWidth, halign, wrap, truncate);
        fixLayout(layout);
        getCache().addText(layout, x, y);
        getCache().draw(batch);
        return layout;
    }

    @Override
    public void draw(Batch batch, GlyphLayout layout, float x, float y) {
        getCache().clear();
        fixLayout(layout);
        getCache().addText(layout, x, y);
        getCache().draw(batch);
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

    private Glyph getGlyph(int codePoint, int characterType) {
        if (characterType == CharacterTypes.DEFAULT)
            return getDefaultGlyph(codePoint);

        IntMap<Glyph> glyphs = fixedGlyphs.get(codePoint);
        if (glyphs == null) {
            glyphs = new IntMap<Glyph>(4);
            fixedGlyphs.put(codePoint, glyphs);
        }
        Glyph fixedGlyph = glyphs.get(characterType);
        if (fixedGlyph == null) {
            fixedGlyph = cloneGlyph(getDefaultGlyph(codePoint));
            glyphs.put(characterType, fixedGlyph);
        }
        return fixedGlyph;
    }

    private Glyph getDefaultGlyph(int codePoint) {
        final Glyph glyph = getData().getGlyph((char) codePoint);
        return glyph != null ? glyph : getData().missingGlyph;
    }

    private int getGlyphId(GlyphLayout.GlyphRun run, int runIndex) {
        if (runIndex < 0 || runIndex > run.glyphs.size - 1)
            return -1;

        final Glyph glyph = run.glyphs.get(runIndex);
        if (glyph != null)
            return glyph.id;
        else
            return -1;
    }

    private void fixUpper(GlyphLayout.GlyphRun run, int runIndex) {
        final int id = getGlyphId(run, runIndex);
        final int previousId = getGlyphId(run, runIndex - 1);
        final int penultimateId = getGlyphId(run, runIndex - 2);

        final int characterType = findCharacterType(penultimateId, previousId, id);
        run.glyphs.set(runIndex, getGlyph(id, characterType));
    }

    private void fixLower(GlyphLayout.GlyphRun run, int runIndex) {
        final int id = getGlyphId(run, runIndex);
        if (!isBottomCharacter(id) || runIndex == 0)
            return;

        final int previousId = getGlyphId(run, runIndex - 1);
        if (previousId == YO_YING || previousId == THO_THAN)
            run.glyphs.set(runIndex - 1, getGlyph(previousId, CharacterTypes.NO_LOWER_CURVES));
    }

    private static int findCharacterType(int penultimateId, int previousId, int id) {
        if (isAboveCharacter(id)) {
            return findAboveCharacterType(previousId);
        }
        if (isTopCharacter(id)) {
            return findTopCharacterType(penultimateId, previousId);
        }
        return CharacterTypes.DEFAULT;
    }

    private static int findAboveCharacterType(int previousId) {
        if (isTallCharacter(previousId))
            return CharacterTypes.ABOVE_LEFT;
        else
            return CharacterTypes.ABOVE;
    }

    private static int findTopCharacterType(int penultimateId, int previousId) {
        if (isAboveCharacter(previousId)) {
            if (isTallCharacter(penultimateId))
                return CharacterTypes.TOP_LEFT;
            else
                return CharacterTypes.TOP;
        }
        return findAboveCharacterType(previousId);
    }

    private static boolean isTopCharacter(int id) {
        return id == MAI_EK
                || id == MAI_THO
                || id == MAI_TRI
                || id == MAI_CHATTAWA
                || id == THANTHAKHAT;
    }

    private static boolean isAboveCharacter(int id) {
        return id == MAI_HAN_AKAT
                || id == NIKHAHIT
                || id == SARA_I
                || id == SARA_II
                || id == SARA_UE
                || id == SARA_UEE;
    }

    private static boolean isBottomCharacter(int id) {
        return id == SARA_U
                || id == SARA_UU;
    }

    private static boolean isTallCharacter(int id) {
        return id == PO_PLA
                || id == FO_FA
                || id == FO_FAN;
    }

    private static BitmapFont.Glyph cloneGlyph(Glyph glyph) {
        Glyph newGlyph = new Glyph();
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

    private static String replaceSaraAm(CharSequence str) {
        final int strLength = str.length();
        if (tempCharArray.length < strLength << 1) {
            tempCharArray = new char[strLength << 1];
        }

        int i = 0;
        int sbIndex = 0;
        while (i < strLength) {
            final char character = str.charAt(i);
            final char nextCharacter;
            if (i < strLength - 1)
                nextCharacter = str.charAt(i + 1);
            else
                nextCharacter = '\u0000';
            i += 2;

            final boolean firstCharIsTopChar = character == MAI_EK
                    || character == MAI_THO
                    || character == MAI_TRI
                    || character == MAI_CHATTAWA
                    || character == THANTHAKHAT;

            if (nextCharacter == SARA_AM && firstCharIsTopChar) {
                tempCharArray[sbIndex++] = NIKHAHIT;
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = SARA_AA;
            } else if (nextCharacter == SARA_AM && character == SARA_AM) {
                tempCharArray[sbIndex++] = NIKHAHIT;
                tempCharArray[sbIndex++] = SARA_AA;
                tempCharArray[sbIndex++] = NIKHAHIT;
                tempCharArray[sbIndex++] = SARA_AA;
            } else if (nextCharacter == SARA_AM) {
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = NIKHAHIT;
                tempCharArray[sbIndex++] = SARA_AA;
            } else if (character == SARA_AM) {
                tempCharArray[sbIndex++] = NIKHAHIT;
                tempCharArray[sbIndex++] = SARA_AA;
                tempCharArray[sbIndex++] = nextCharacter;
            } else {
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = nextCharacter;
            }
        }
        if (tempCharArray[sbIndex - 1] == '\u0000')
            --sbIndex;

        return new String(tempCharArray, 0, sbIndex);
    }

    public static class FixedThaiFontParameter {
        public int horizontalOffset;
        public int verticalOffset;
        public int yoYingTrim;
        public int thoThanTrim;
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
