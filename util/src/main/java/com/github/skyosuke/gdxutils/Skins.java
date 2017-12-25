package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.skyosuke.gdxutils.thaifont.ThaiFont;
import com.github.skyosuke.gdxutils.thaifont.ThaiFontLoader;

public enum Skins {
    DEFAULT,
    DEFAULT_THAI;

    public Skin createSkin() {
        Skin skin = new Skin(getFile());
        configSkin(skin);
        return skin;
    }

    public void loadTo(AssetManager manager) {
        AssetDescriptor<Skin> descriptor = new AssetDescriptor<Skin>(getFile(), Skin.class);
        manager.load(descriptor);
    }

    public Skin getFrom(AssetManager manager) {
        AssetDescriptor<Skin> descriptor = new AssetDescriptor<Skin>(getFile(), Skin.class);
        Skin skin = manager.get(descriptor);
        configSkin(skin);
        return skin;
    }

    private void configSkin(Skin skin) {
        if (this == DEFAULT_THAI) {
            BitmapFont thaiFont = new ThaiFont(Gdx.files.internal("skin/default/default-thai.fnt"),
                    new ThaiFontLoader.ThaiFontParameter(-3, 5, 4, 6));
            SkinUtils.replaceFont(skin, "default", thaiFont);
        }
    }

    private FileHandle getFile() {
        switch (this) {
            case DEFAULT:
            case DEFAULT_THAI:
                return Gdx.files.internal("skin/default/default.json");
            default:
                throw new IllegalStateException("Execution flow must not reach here!");
        }
    }

}
