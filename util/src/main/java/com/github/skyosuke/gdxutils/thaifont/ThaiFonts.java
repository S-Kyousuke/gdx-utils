package com.github.skyosuke.gdxutils.thaifont;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum ThaiFonts {
    INSTANCE;

    public BitmapFont createFont(Font font) {
        if (font == null)
            throw new IllegalArgumentException("font cannot be null!");

        BitmapFont bitmapFont;
        ThaiFont.ThaiFontParameter parameter;

        switch (font) {
            case CHIANGSAEN_16:
                parameter = new ThaiFont.ThaiFontParameter();
                parameter.horizontalOffset = -2;
                parameter.verticalOffset = 4;
                parameter.yoYingTrim = 3;
                parameter.thoThanTrim = 4;
                bitmapFont = new ThaiFont("font/chiangsaen-16.fnt", parameter);
                return bitmapFont;
            case CHIANGSAEN_64:
                parameter = new ThaiFont.ThaiFontParameter();
                parameter.horizontalOffset = -6;
                parameter.verticalOffset = 12;
                parameter.yoYingTrim = 10;
                parameter.thoThanTrim = 13;
                bitmapFont = new ThaiFont("font/chiangsaen-64.fnt", parameter);
                return bitmapFont;
        }

        throw new IllegalStateException("Execution flow must not reach here!");
    }

    public enum Font {
        CHIANGSAEN_16,
        CHIANGSAEN_64
    }
}
