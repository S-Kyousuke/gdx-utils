package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.skyousuke.gdxutils.skin.Skins;

public class AnimationViewer extends GdxApp {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private TextureAtlas mouseAtlas;
    private TextureAtlas atlas;

    private Table topLeftPanel;

    private SelectBox<String> animationSelectBox;
    private SelectBox<String> frameSelectBox;

    private TextButton playButton;
    private Slider playbackScaleSlider;

    private CheckBox debugCheckbox;
    private CheckBox gridCheckbox;

    private TextField backgroundColorTextField;
    private TextField gridColorTextField;
    private TextField gridSizeTextField;
    private TextField animationFileTextField;
    private TextField atlasFileTextField;

    private TextButton toggleFlipXButton;
    private TextButton toggleFlipYButton;

    private AnimatedRegion animatedRegion;
    private ProgressBar animationTimeBar;

    private int screenWidth;
    private int screenHeight;

    private Label animationStatusLabel;
    private Label positionStatusLabel;
    private Label errorLabel;

    private Label.LabelStyle labelWithBackgroundStyle;
    private TextButton.TextButtonStyle toggleButtonStyle;

    private Color backgroundColor = new Color(0x111111ff);
    private Color gridColor = new Color(0x333333ff);
    private int gridSize = 20;

    private boolean unlockedCamera = true;

    private Vector2 startRightClickPos = new Vector2();
    private Vector2 startCameraPos = new Vector2();
    private boolean movingCamera = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.position.set(0, 0, 0);

        stage = new Stage();
        skin = Skins.DEFAULT_THAI.createSkin();

        mouseAtlas = new TextureAtlas("mouse.atlas");

        labelWithBackgroundStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        labelWithBackgroundStyle.background = ((TextureRegionDrawable) skin.getDrawable("white")).tint(Color.BLACK);

        toggleButtonStyle = skin.get("toggle", TextButton.TextButtonStyle.class);
        toggleButtonStyle.up = ((NinePatchDrawable) toggleButtonStyle.up).tint(Color.GRAY);

        errorLabel = new Label("", labelWithBackgroundStyle);
        errorLabel.setAlignment(Align.center);
        errorLabel.setColor(1, 0.2f, 0.2f, 1);

        animationStatusLabel = new Label("", skin);
        positionStatusLabel = new Label("", skin);
        positionStatusLabel.setAlignment(Align.right);
        topLeftPanel = buildTopLeftPanel();

        ProgressBar.ProgressBarStyle barStyle =
                new ProgressBar.ProgressBarStyle(skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
        TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        barStyle.knob = null;
        NinePatchDrawable barDrawable = ((NinePatchDrawable) buttonStyle.up).tint(Color.CYAN);
        barDrawable.getPatch().setLeftWidth(0);
        barDrawable.getPatch().setRightWidth(0);
        barDrawable.setMinHeight(8);
        barStyle.knobBefore = barDrawable;

        animationTimeBar = new ProgressBar(0.0f, 100.0f, 1f, false, barStyle);
        animationTimeBar.setWidth(210);
        animationTimeBar.setPosition(10, topLeftPanel.getY() - animationTimeBar.getHeight() - 5);

        stage.addActor(errorLabel);
        stage.addActor(topLeftPanel);
        stage.addActor(buildBottomLeftPanel());
        stage.addActor(buildBottomRightPanel());
        stage.addActor(buildLeftPanel());
        stage.addActor(buildRightPanel());
        stage.addActor(buildEditorPanel());
        stage.addActor(animationTimeBar);
        stage.addActor(animationStatusLabel);
        stage.addActor(positionStatusLabel);

        reloadFile();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final float deltaTime = Gdx.graphics.getDeltaTime();
        if (animatedRegion != null) {
            animatedRegion.update(deltaTime);
            updateAnimationGuiStatus();
        }
        updatePositionStatus();

        updateCamera();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (gridCheckbox.isChecked()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            drawGrid(shapeRenderer, camera, gridSize);
            shapeRenderer.end();
        }

        batch.begin();
        if (animatedRegion != null)
            animatedRegion.draw(batch, 0, 0);
        batch.end();

        if (debugCheckbox.isChecked() && animatedRegion != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            animatedRegion.drawDebug(shapeRenderer, 0, 0);
            shapeRenderer.end();
        }
        stage.act();
        stage.draw();
    }

    private void updateCamera() {
        if (!unlockedCamera && animatedRegion != null) {
            final float deltaX = -camera.position.x;
            final float deltaY = -camera.position.y;

            if (Math.abs(deltaX) >= 1) camera.position.x += deltaX * 0.1f;
            if (Math.abs(deltaY) >= 1) camera.position.y += deltaY * 0.1f;
        }
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        skin.dispose();
        mouseAtlas.dispose();
        if (atlas != null)
            atlas.dispose();
    }


    private Table buildBottomRightPanel() {
        Table table = new Table();
        table.setBackground(labelWithBackgroundStyle.background);
        table.pad(5);

        TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        buttonStyle.up = ((NinePatchDrawable) buttonStyle.up).tint(Color.GRAY);

        animationFileTextField = new TextField("animation.json", skin);
        atlasFileTextField = new TextField("animation.atlas", skin);
        TextButton reloadButton = new TextButton("โหลดใหม่", buttonStyle);
        reloadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reloadFile();
            }
        });

        TextButton.TextButtonStyle newButtonStyle = new TextButton.TextButtonStyle(buttonStyle);
        newButtonStyle.up = ((NinePatchDrawable) buttonStyle.up).tint(Color.TEAL);
        TextButton newButton = new TextButton("สร้าง", newButtonStyle);
        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: add create animation function!
            }
        });

        table.add(new Label("ไฟล์แอนิเมชั่น", skin)).padLeft(10).padRight(10).align(Align.center);
        table.add(animationFileTextField).size(120, 25);
        table.add(new Label("ไฟล์ Atlas", skin)).padLeft(10).padRight(10).align(Align.center);
        table.add(atlasFileTextField).size(120, 25).padRight(10);
        table.add(reloadButton).size(70, 25).padRight(10);
        table.add(newButton).size(50, 25).padRight(5);

        table.pack();
        table.setPosition(screenWidth - table.getWidth(), 0);
        return table;
    }

    private Table buildBottomLeftPanel() {
        Table table = new Table();
        table.setBackground(labelWithBackgroundStyle.background);

        backgroundColorTextField = new TextField("#111111", skin);
        backgroundColorTextField.setMaxLength(7);
        backgroundColorTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return "#0123456789aAbBcCdDeEfF".indexOf(c) != -1;
            }
        });
        backgroundColorTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String colorText = backgroundColorTextField.getText();
                if (colorText.length() == backgroundColorTextField.getMaxLength() && colorText.charAt(0) == '#') {
                    final String rgba8888 = colorText + "ff";
                    final long colorValue = Long.decode(rgba8888);
                    backgroundColor = new Color((int) colorValue);
                }
            }
        });

        gridColorTextField = new TextField("#333333", skin);
        gridColorTextField.setMaxLength(7);
        gridColorTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return "#0123456789aAbBcCdDeEfF".indexOf(c) != -1;
            }
        });
        gridColorTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String colorText = gridColorTextField.getText();
                if (colorText.length() == gridColorTextField.getMaxLength() && colorText.charAt(0) == '#') {
                    final String rgba8888 = colorText + "ff";
                    final long colorValue = Long.decode(rgba8888);
                    gridColor = new Color((int) colorValue);
                }
            }
        });

        gridSizeTextField = new TextField("20", skin);
        gridSizeTextField.setMaxLength(3);
        gridSizeTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return "0123456789".indexOf(c) != -1;
            }
        });
        gridSizeTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String gridSizeText = gridSizeTextField.getText();
                if (NumberUtils.isInteger(gridSizeText)) {
                    final int inputGridSize = Integer.parseInt(gridSizeText);
                    if (inputGridSize > 0)
                        gridSize = Integer.parseInt(gridSizeText);
                }
            }
        });

        table.row().pad(5);
        table.add(new Label("สีพื้นหลัง", skin)).padLeft(10).padRight(5).align(Align.center);
        table.add(backgroundColorTextField).size(75, 25).align(Align.center);
        table.add(new Label("สีตาราง", skin)).padLeft(5).padRight(5).align(Align.center);
        table.add(gridColorTextField).size(75, 25).align(Align.center);
        table.add(new Label("ขนาดตาราง", skin)).padLeft(5).padRight(5).align(Align.center);
        table.add(gridSizeTextField).size(35, 25).align(Align.center);

        table.pack();
        return table;
    }

    private Table buildTopLeftPanel() {
        Table table = new Table();
        table.pad(5);

        table.row().align(Align.top).expandY();
        table.setBackground(labelWithBackgroundStyle.background);

        TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        buttonStyle.up = ((NinePatchDrawable) buttonStyle.up).tint(Color.GRAY);



        playButton = new TextButton("เล่น", toggleButtonStyle);
        final TextButton stopButton = new TextButton("หยุด", toggleButtonStyle);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (animatedRegion != null) {
                    animatedRegion.setPlaying(playButton.isChecked());
                }
                stopButton.setChecked(false);
            }
        });

        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (animatedRegion != null)
                    animatedRegion.setPlaying(false);
                playButton.setChecked(false);
            }
        });

        TextButton resetButton = new TextButton("เริ่มใหม่", buttonStyle);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (animatedRegion != null)
                    animatedRegion.reset();
            }
        });

        animationSelectBox = new SelectBox<String>(skin);
        animationSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (animatedRegion != null) {
                    animatedRegion.setAnimation(animationSelectBox.getSelected());
                    final int totalFrame = animatedRegion.getFrameSize();
                    final Array<String> frames = new Array<String>(totalFrame);
                    for (int i = 0; i < totalFrame; i++) {
                        frames.add(String.valueOf(i + 1));
                    }
                    frameSelectBox.setItems(frames);
                    playButton.setChecked(true);
                    animatedRegion.setPlaying(true);
                    stopButton.setChecked(false);
                }
            }
        });

        frameSelectBox = new SelectBox<String>(skin);
        frameSelectBox.getScrollPane().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (animatedRegion != null) {
                    stopButton.setChecked(true);
                    animatedRegion.setPlaying(false);
                    AnimatedRegion.setKeyFrameIndex(animatedRegion, Integer.parseInt(frameSelectBox.getSelected()) - 1);
                }
                playButton.setChecked(false);
            }
        });

        playbackScaleSlider = new Slider(0.1f, 3f, 0.25f, false, skin);
        playbackScaleSlider.setValue(1.0f);
        final Label playbackScaleLabel = new Label("1.00 เท่า", skin);

        playbackScaleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playbackScaleLabel.setText(NumberFormat.formatDecimal(playbackScaleSlider.getValue(), 2) + " เท่า");
                if (animatedRegion != null)
                    animatedRegion.setPlaybackScale(playbackScaleSlider.getValue());
            }
        });

        debugCheckbox = new CheckBox("โหมดหาบัค", skin);
        debugCheckbox.setChecked(true);
        debugCheckbox.getImageCell().padRight(5);

        gridCheckbox = new CheckBox("แสดงตาราง", skin);
        gridCheckbox.setChecked(true);
        gridCheckbox.getImageCell().padRight(5);

        table.add(new Label("แอนิเมชั่น", skin)).padLeft(5).padRight(5).align(Align.center);
        table.add(animationSelectBox).size(150, 25).align(Align.center);
        table.add(new Label("เฟรม", skin)).padLeft(5).padRight(5).align(Align.center);
        table.add(frameSelectBox).size(50, 25).align(Align.center).padRight(10);
        table.add(playButton).size(70, 25);
        table.add(stopButton).size(70, 25);
        table.add(resetButton).size(70, 25);
        table.add(new Label("ความเร็ว", skin)).padLeft(10).padRight(10).align(Align.center);
        table.add(playbackScaleSlider).size(160, 25);
        table.add(playbackScaleLabel).size(50, 25).padLeft(5).padRight(5).align(Align.center);
        table.add(debugCheckbox).size(100, 25).padLeft(5);
        table.add(gridCheckbox).size(100, 25);

        table.pack();
        table.setPosition(0, screenHeight - table.getHeight());

        return table;
    }

    private void reloadFile() {
        animatedRegion = null;
        if (atlas != null) {
            atlas.dispose();
            atlas = null;
        }

        String lastAnimation = animationSelectBox.getSelected();

        setErrorText("");
        animationStatusLabel.setText("");
        frameSelectBox.clearItems();
        animationSelectBox.clearItems();
        playbackScaleSlider.setValue(1.0f);
        playButton.setChecked(false);
        toggleFlipXButton.setChecked(false);
        toggleFlipYButton.setChecked(false);

        toggleFlipXButton.setVisible(false);
        toggleFlipYButton.setVisible(false);
        animationTimeBar.setVisible(false);

        final String atlasFilePath = atlasFileTextField.getText();
        final String animationFilePath = animationFileTextField.getText();

        try {
            atlas = new TextureAtlas(Gdx.files.local(atlasFilePath));
        } catch (Exception e) {
            setErrorText("ไม่สามารถอ่านไฟล์ < " + atlasFilePath + " > ได้\nกรุณาตรวจสอบความถูกต้องของไฟล์");
            e.printStackTrace();
            return;
        }
        try {
            animatedRegion = new AnimatedRegion(animationFilePath, atlas, 1);
        } catch (Exception e) {
            setErrorText("ไม่สามารถอ่านไฟล์ < " + animationFilePath + " > ได้\nกรุณาตรวจสอบความถูกต้องของไฟล์");
            e.printStackTrace();
            return;
        }
        animationSelectBox.setItems(animatedRegion.getNames());
        if (lastAnimation != null && animatedRegion.getNames().contains(lastAnimation, false)) {
            animationSelectBox.setSelected(lastAnimation);
        }

        toggleFlipXButton.setVisible(true);
        toggleFlipYButton.setVisible(true);
        animationTimeBar.setVisible(true);
    }

    private void updateAnimationGuiStatus() {
        final float frameDuration = animatedRegion.getFrameDuration();
        final float time = animatedRegion.getTime();

        String statusText = "กำลังเล่น";

        final boolean playOnceMode = (animatedRegion.getPlayMode() == Animation.PlayMode.NORMAL
                || animatedRegion.getPlayMode() == Animation.PlayMode.REVERSED);

        if (!animatedRegion.isPlaying()) {
            statusText = "หยุดเล่นชั่วคราว";
        } else if (playOnceMode && animatedRegion.isAnimationFinished()) {
            statusText = "เล่นจบแล้ว";
        }

        final int frameNumber = animatedRegion.getKeyFrameIndex() + 1;

        final String text = ""
                + "\nสถานะ: " + statusText + " (โหมด " + animatedRegion.getPlayMode().name() + ')'
                + "\nความเร็ว: " + NumberFormat.formatDecimal(1 / frameDuration, 2) + " เฟรม/วินาที"
                + "\nเวลา: " + NumberFormat.formatDecimal(time, 3) + " วินาที"
                + "\nเฟรม: " + frameNumber + '/' + animatedRegion.getFrameSize();

        animationStatusLabel.setText(text);
        animationStatusLabel.pack();
        animationStatusLabel.setPosition(10, topLeftPanel.getY() - animationStatusLabel.getHeight());

        frameSelectBox.setSelected(String.valueOf(frameNumber));

        animationTimeBar.setValue(100f * AnimatedRegion.getKeyFramePercent(animatedRegion));
    }

    private void updatePositionStatus() {
        String text = "ตำแหน่งมุมกล้อง: (" + NumberFormat.formatDecimal(camera.position.x, 2)
                + ", " + NumberFormat.formatDecimal(camera.position.y, 2) + ')'
                + "\nระดับการซูม: " + NumberFormat.formatDecimal(1 / camera.zoom, 2) + " เท่า";

        positionStatusLabel.setText(text);
        positionStatusLabel.pack();
        positionStatusLabel.setPosition(screenWidth - positionStatusLabel.getWidth() - 10,
                topLeftPanel.getY() - positionStatusLabel.getHeight() - 10);
    }

    private void setErrorText(String text) {
        errorLabel.setText(text);
        errorLabel.pack();
        errorLabel.setPosition(
                (screenWidth - errorLabel.getWidth()) * 0.5f,
                120 - errorLabel.getHeight());
    }

    private void drawGrid(ShapeRenderer renderer, OrthographicCamera camera, int size) {
        final float cameraWidth = camera.viewportWidth * camera.zoom;
        final float cameraHeight = camera.viewportHeight * camera.zoom;
        final float leftEdge = camera.position.x - cameraWidth / 2;
        final float rightEdge = camera.position.x + cameraWidth / 2;
        final float topEdge = camera.position.y + cameraHeight / 2;
        final float bottomEdge = camera.position.y - cameraHeight / 2;

        final int startX = roundDownToNearestMultiple(leftEdge, size);
        final int endX = roundDownToNearestMultiple(rightEdge, size);
        final int startY = roundDownToNearestMultiple(bottomEdge, size);
        final int endY = roundDownToNearestMultiple(topEdge, size);

        renderer.setColor(gridColor);
        for (int i = startX; i <= endX; i += size) {
            renderer.line(i, bottomEdge, i, topEdge);
        }
        for (int i = startY; i <= endY; i += size) {
            renderer.line(leftEdge, i, rightEdge, i);
        }
    }

    private int roundDownToNearestMultiple(float valueToRound, int multiple) {
        if (valueToRound > 0)
            return (int) (Math.floor(valueToRound / multiple) * multiple);
        if (valueToRound < 0)
            return (int) (Math.ceil(valueToRound / multiple) * multiple);
        return 0;
    }

    private Actor buildLeftPanel() {
        Table table = new Table();
        table.pad(5f, 10f, 5f, 0f);


        toggleFlipXButton = new TextButton("กลับภาพแนวนอน", toggleButtonStyle);
        toggleFlipXButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (animatedRegion != null)
                    animatedRegion.setFlipX(!animatedRegion.isFlipX());
            }
        });
        toggleFlipYButton = new TextButton("กลับภาพแนวตั้ง", toggleButtonStyle);
        toggleFlipYButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (animatedRegion != null)
                    animatedRegion.setFlipY(!animatedRegion.isFlipY());
            }
        });
        table.row().padBottom(10f);
        table.add(toggleFlipXButton).size(120, 25);
        table.row();
        table.add(toggleFlipYButton).size(120, 25);

        table.setPosition(0, 345);
        table.pack();
        return table;
    }

    private Actor buildRightPanel() {
        Table table = new Table();
        table.pad(5f, 0, 5f, 10f);
        table.row().padBottom(20f);

        final TextButton lockCameraButton = new TextButton("ล็อคมุมกล้อง", toggleButtonStyle);
        lockCameraButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                unlockedCamera = !lockCameraButton.isChecked();
            }
        });
        table.add(lockCameraButton).size(120, 25).colspan(2);
        table.row();
        table.row().padBottom(10f);
        table.add(new Image(mouseAtlas.findRegion("mouse2"))).expandX();
        table.add(new Label("ปรับตำแหน่ง\nมุมกล้อง", skin)).padLeft(10).align(Align.left);
        table.row();
        table.add(new Image(mouseAtlas.findRegion("mouse3"))).expandX();
        table.add(new Label("ปรับระดับ\nการซูม", skin)).padLeft(10).align(Align.left);

        table.pack();
        table.setPosition(screenWidth - table.getWidth(), screenHeight - table.getHeight() - 100);
        return table;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom = MathUtils.clamp(camera.zoom + amount * 0.1f, 0.1f, 4.0f);
        return true;
    }

    @Override
    public boolean touchDragged(int touchX, int touchY, int pointer) {
        if (movingCamera) {
            Vector2 posDiff = new Vector2(startRightClickPos).sub(touchX, touchY);
            camera.position.x = startCameraPos.x + posDiff.x;
            camera.position.y = startCameraPos.y - posDiff.y;
        }
        return true;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button) {
        if (button == 1) { // right-click
            startRightClickPos.set(touchX, touchY);
            startCameraPos.set(camera.position.x, camera.position.y);
            movingCamera = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button) {
        if (button == 1) { // right-click
            movingCamera = false;
        }
        return true;
    }

    public Actor buildEditorPanel() {
        Table table = new Table();

        table.pack();
        return table;
    }
}
