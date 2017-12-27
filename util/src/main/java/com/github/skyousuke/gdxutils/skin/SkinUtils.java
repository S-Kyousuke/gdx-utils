package com.github.skyousuke.gdxutils.skin;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.skyousuke.gdxutils.font.ThaiFont;
import com.github.skyousuke.gdxutils.font.ThaiFontLoader;

public class SkinUtils {

    private SkinUtils() {
    }

    public static void configThaiFont(Skin skin, String fontName, ThaiFontLoader.ThaiFontParameter params) {
        final BitmapFont font = skin.getFont(fontName);
        if (font instanceof ThaiFont)
            ((ThaiFont) font).getThaiFontCache().config(params);
        else
            replaceFont(skin, fontName, new ThaiFont(font, params));
    }

    @SuppressWarnings("squid:S3776")
    public static void replaceFont(Skin skin, String fontName, BitmapFont newFont) {
        final BitmapFont font = skin.getFont(fontName);
        skin.add(fontName, newFont, BitmapFont.class);

        for (Label.LabelStyle style : getStyles(skin, Label.LabelStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (TextButton.TextButtonStyle style : getStyles(skin, TextButton.TextButtonStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (ImageTextButton.ImageTextButtonStyle style : getStyles(skin, ImageTextButton.ImageTextButtonStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (SelectBox.SelectBoxStyle style : getStyles(skin, SelectBox.SelectBoxStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (CheckBox.CheckBoxStyle style : getStyles(skin, CheckBox.CheckBoxStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (TextField.TextFieldStyle style : getStyles(skin, TextField.TextFieldStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (List.ListStyle style : getStyles(skin, List.ListStyle.class)) {
            if (style.font == font)
                style.font = newFont;
        }
        for (Window.WindowStyle style : getStyles(skin, Window.WindowStyle.class)) {
            if (style.titleFont == font)
                style.titleFont = newFont;
        }
        for (TextTooltip.TextTooltipStyle style : getStyles(skin, TextTooltip.TextTooltipStyle.class)) {
            if (style.label.font == font)
                style.label.font = newFont;
        }
    }

    private static <T> ObjectMap.Values<T> getStyles(Skin skin, Class<T> type) {
        ObjectMap<String, T> styles = skin.getAll(type);
        if (styles == null) styles = new ObjectMap<String, T>(0);
        return new ObjectMap.Values<T>(styles);
    }
}
