package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GdxUtils extends ApplicationAdapter {

    TextureAtlas playerAtlas;
    SpriteBatch batch;
    AnimatedRegion animatedRegion;

    @Override
    public void create() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        batch = new SpriteBatch();
        animatedRegion = new AnimatedRegion("playerAnimation.json", playerAtlas, "run");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        animatedRegion.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.draw(animatedRegion.getFrame(), 0, 0,
                animatedRegion.getFrame().getRegionWidth() * 0.5f,
                animatedRegion.getFrame().getRegionHeight() * 0.5f);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerAtlas.dispose();
    }
}
