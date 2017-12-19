package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            animatedRegion.setAnimation("stand");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            animatedRegion.setAnimation("run");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            animatedRegion.setAnimation("jump");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            animatedRegion.setAnimation("hit");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            animatedRegion.setPlaybackScale(1.0f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            animatedRegion.setPlaybackScale(0.5f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F7)) {
            animatedRegion.setPlaybackScale(2.0f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            animatedRegion.pause();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            animatedRegion.play();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            animatedRegion.stop();
        }
        if (animatedRegion.isAnimationEquals("hit") && animatedRegion.isAnimationFinished()) {
            animatedRegion.setAnimation("run");
        }

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
