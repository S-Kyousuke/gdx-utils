package com.github.skyousuke.gdxutils.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum ThaiFonts {
    CHIANGSAEN_16,
    CHIANGSAEN_20,
    CHIANGSAEN_64;

    public BitmapFont createFont() {
        return new ThaiFont(getFile(), getParameter());
    }

    public FileHandle getFile() {
        switch (this) {
            case CHIANGSAEN_16:
                return Gdx.files.internal("com/github/skyousuke/gdxutils/font/chiangsaen-16.fnt");
            case CHIANGSAEN_20:
                return Gdx.files.internal("com/github/skyousuke/gdxutils/font/chiangsaen-20.fnt");
            case CHIANGSAEN_64:
                return Gdx.files.internal("com/github/skyousuke/gdxutils/font/chiangsaen-64.fnt");
            default:
                throw new IllegalStateException("the execution flow must not reach here!");
        }
    }

    public ThaiFontLoader.ThaiFontParameter getParameter() {
        ThaiFontLoader.ThaiFontParameter parameter = new ThaiFontLoader.ThaiFontParameter();
        switch (this) {
            case CHIANGSAEN_16:
                parameter.horizontalOffset = -2;
                parameter.verticalOffset = 4;
                parameter.yoYingTrim = 3;
                parameter.thoThanTrim = 4;
                return parameter;
            case CHIANGSAEN_20:
                parameter.horizontalOffset = -3;
                parameter.verticalOffset = 5;
                parameter.yoYingTrim = 4;
                parameter.thoThanTrim = 6;
                return parameter;
            case CHIANGSAEN_64:
                parameter.horizontalOffset = -6;
                parameter.verticalOffset = 12;
                parameter.yoYingTrim = 10;
                parameter.thoThanTrim = 13;
                return parameter;
            default:
                throw new IllegalStateException("Execution flow must not reach here!");
        }
    }

    public void loadTo(AssetManager manager) {
        AssetDescriptor<ThaiFont> descriptor = new AssetDescriptor<ThaiFont>(getFile(), ThaiFont.class,  getParameter());
        manager.load(descriptor);
    }

    public BitmapFont getFrom(AssetManager manager) {
        AssetDescriptor<ThaiFont> descriptor = new AssetDescriptor<ThaiFont>(getFile(), ThaiFont.class);
        return manager.get(descriptor);
    }
}
