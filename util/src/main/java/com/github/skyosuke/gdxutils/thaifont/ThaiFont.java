package com.github.skyosuke.gdxutils.thaifont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;

public class ThaiFont extends BitmapFont {

    private final ThaiFontParameter parameter;

    public ThaiFont(String fontFileInternalPath, ThaiFontParameter parameter) {
        super(Gdx.files.internal(fontFileInternalPath));
        this.parameter = parameter;
        getThaiFontCache().config(parameter);
    }

    public ThaiFontCache getThaiFontCache() {
        return ((ThaiFontCache) getCache());
    }

    @Override
    public BitmapFontCache newFontCache() {
        return new ThaiFontCache(this, parameter);
    }

    public static class ThaiFontParameter {
        public int horizontalOffset;
        public int verticalOffset;
        public int yoYingTrim;
        public int thoThanTrim;
    }

}
