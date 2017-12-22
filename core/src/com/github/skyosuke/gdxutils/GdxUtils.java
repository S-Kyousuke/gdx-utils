package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GdxUtils extends ApplicationAdapter {

    TextureAtlas playerAtlas;
    SpriteBatch batch;
    ShapeRenderer renderer;
    AnimatedRegion animatedRegion;
    Viewport viewport;

    float rotation;

    @Override
    public void create() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        viewport = new FitViewport(12.80f, 7.20f);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        animatedRegion = new AnimatedRegion("playerAnimation.json", playerAtlas, "stand", 100);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        animatedRegion.update(deltaTime);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            rotation += Gdx.graphics.getDeltaTime() * 100;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            animatedRegion.setFlipX(!animatedRegion.isFlipX());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            animatedRegion.setFlipY(!animatedRegion.isFlipY());
        }

        viewport.getCamera().update();
        batch.setProjectionMatrix(viewport.getCamera().projection);
        renderer.setProjectionMatrix(viewport.getCamera().projection);

        batch.begin();
        animatedRegion.draw(batch, -1, -1);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        animatedRegion.drawDebug(renderer, -1, -1);
        renderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerAtlas.dispose();
        renderer.dispose();
    }
}
