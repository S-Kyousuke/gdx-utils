package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

        Gdx.app.setLogLevel(Application.LOG_INFO);

        Gdx.app.log("", (NumberFormat.formatDecimal(777.575f, 2)));
        Gdx.app.log("", (NumberFormat.formatDecimal(777.575, 2)));
        Gdx.app.log("", (NumberFormat.formatDecimal(777.578905f, 5)));
        Gdx.app.log("", (NumberFormat.formatDecimal(777.57890501, 5)));

        font = ThaiFonts.CHIANGSAEN_64.createFont();
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(1));
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(3));
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(5));
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(6));
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(51));
        Gdx.app.log("", "" + MathUtils.nextPowerOfTwo(33));

//        else
//            Gdx.app.log("", (NumberFormat.formatFloat(1234.57890, 2)));

//
//
//        Gdx.app.setLogLevel(Application.LOG_INFO);
//
//        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
//            com.google.gwt.i18n.client.NumberFormat nfm =
//                    com.google.gwt.i18n.client.NumberFormat .getFormat("#,###.0000");
//            Gdx.app.log("", nfm.format(123457810.1254823));
//        } else {
//            DecimalFormat dfm = new DecimalFormat("#,###.0000");
//            Gdx.app.log("", dfm.format(123457810.1254823));
//        }
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