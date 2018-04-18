package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.skyousuke.gdxutils.font.ThaiFonts;

import java.util.Date;
import java.util.Locale;

public class RegionPosApp extends GdxApp {

    SpriteBatch batch;
    ShapeRenderer renderer;
    OrthographicCamera camera;
    BitmapFont font;

    GameObject object1 = new GameObject(100, 100, 0, 0, 100, 100, 20, 20);
    GameObject object2 = new GameObject(100, 100, 0, 0, 150, 120, 20, 40);

    @Override
    public void create() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        font = ThaiFonts.CHIANGSAEN_64.createFont();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        Locale.setDefault(LocaleUtils.toLocale("en_US"));
        font.draw(batch, DateTimeFormat.format(new Date(TimeUtils.millis()), "สวัสดีHH:mm:ss M y"), -200, 200);
        Locale.setDefault(LocaleUtils.toLocale("th_TH"));
        font.draw(batch, DateTimeFormat.format(new Date(TimeUtils.millis()), "HH:mm:ss MM yy", 7 * 60), -200, 100);
        Locale.setDefault(LocaleUtils.toLocale("en_US"));
        font.draw(batch, DateTimeFormat.format(new Date(TimeUtils.millis()), "HH:mm:ss MMM yyy"), -200, 0);
        Locale.setDefault(LocaleUtils.toLocale("th_TH"));
        font.draw(batch, DateTimeFormat.format(new Date(TimeUtils.millis()), "HH:mm:ss MMMM yyyy"), -200, -100);
        Locale.setDefault(LocaleUtils.toLocale("en_US"));
        font.draw(batch, DateTimeFormat.format(new Date(TimeUtils.millis()), "HH:mm:ss MMMM yyyy"), -200, -200);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        object1.draw(renderer, Color.CYAN);
        object2.draw(renderer, Color.YELLOW);
        renderer.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(0, 0, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        renderer.dispose();
    }


}