package com.github.skyosuke.gdxutils.thaifont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


public class ThaiFont extends BitmapFont {

    private final ThaiFontLoader.ThaiFontParameter parameter;

    public ThaiFont(String fontFileInternalPath, ThaiFontLoader.ThaiFontParameter parameter) {
        this(Gdx.files.internal(fontFileInternalPath), parameter);
    }

    public ThaiFont(FileHandle fontFile, ThaiFontLoader.ThaiFontParameter parameter) {
        super(fontFile);
        this.parameter = parameter;
        getThaiFontCache().config(parameter);
    }

    /**
     * The {@link #dispose()} method will not dispose the bitmap font in this case! */
    public ThaiFont(BitmapFont bitmapFont, ThaiFontLoader.ThaiFontParameter parameter) {
        this(bitmapFont.getData(), bitmapFont.getRegions(), parameter);
    }

    /**
     * The {@link #dispose()} method will not dispose the region's texture in this case! */
    public ThaiFont(BitmapFontData data, Array<TextureRegion> regions, ThaiFontLoader.ThaiFontParameter parameter) {
        super(data, regions, true);
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



}
