package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.skyousuke.gdxutils.font.ThaiFonts;
import com.github.skyousuke.gdxutils.skin.Skins;

public class FontInspector extends GdxApp {

    public static final String TEST_TEXT = "กตัญญู ป่านนี้ ไข่ดาว สาวสุก ปฎิบัติ ปี่พาทย์ สิทธิ์ ทิฏฐุชุกัมม์ กฺ ปุ่ม ปุ่ม";
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont font1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 0, 0);

        stage = new Stage();
        font = ThaiFonts.MAHANIYOM_BAO_NO_SHADOW.createFont();
        font1 = ThaiFonts.CHIANGSAEN_20_NO_SHADOW.createFont();

        skin = Skins.DEFAULT_THAI.createSkin();
        stage.addActor(new Label(TEST_TEXT, skin));


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        font.draw(batch, TEST_TEXT, -100, 0);
        font1.draw(batch, TEST_TEXT, -100, -100);

        shapeRenderer.end();
        batch.end();

        stage.act();
        stage.draw();
    }

    private void handleInput() {
        final float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            camera.position.y += 200 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            camera.position.y -= 200 * deltaTime;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            camera.position.x -= 200 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            camera.position.x += 200 * deltaTime;

        if (Gdx.input.isKeyPressed(Input.Keys.Z))
            camera.zoom += 1 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.X))
            camera.zoom -= 1 * deltaTime;
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        font1.dispose();
        skin.dispose();
    }
}
