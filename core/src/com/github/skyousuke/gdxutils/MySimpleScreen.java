package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MySimpleScreen implements Screen {

    SpriteBatch batch;
    Texture texture;
    Viewport viewport;

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture("map.jpg");
        viewport = new ExtendViewport(1280, 600);

        viewport.getCamera().position.set(1280 / 2, 600 / 2, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        batch.dispose();
        texture.dispose();
    }

    @Override
    public void dispose() {

    }
}
