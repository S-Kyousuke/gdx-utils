package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class Assets {

    public static final Assets instance = new Assets();

    public TextureAtlas uiAtlas;
    public BitmapFont font;
    public TextureRegionDrawable white;

    private Assets() {}

    public void init() {
        uiAtlas = new TextureAtlas("batchmonitor/batchmonitor-ui.atlas");
        font = new BitmapFont(Gdx.files.internal("batchmonitor/arial_12.fnt"), uiAtlas.findRegion("arial_12"));
        white = new TextureRegionDrawable(uiAtlas.findRegion("white"));
    }

    public void dispose() {
        uiAtlas.dispose();
        font.dispose();
    }

}
