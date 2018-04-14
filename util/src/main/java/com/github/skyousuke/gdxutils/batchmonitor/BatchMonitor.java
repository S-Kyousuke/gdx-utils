package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.nio.IntBuffer;

public class BatchMonitor implements ApplicationListener {

    private ApplicationListener application;
    private int[] applicationViewportAttribute = new int[4];

    private Stage stage;
    private MonitorTable monitorTable;
    private InputMultiplexer multiplexer = new InputMultiplexer();

    private int screenWidth;
    private int screenHeight;
    private boolean screenSizeChanged = true;
    private float timeSinceUpdateData;
    private float timeSinceFindBatch;

    private Array<BatchMeter> batchMeters = new Array<BatchMeter>();
    private Array<BatchRecordContainer> allRecords = new Array<BatchRecordContainer>();
    private Array<FieldObject> outBatchObjects = new Array<FieldObject>();
    private Array<SpriteBatch> alreadyDisposedBatch = new Array<SpriteBatch>();

    public BatchMonitor(ApplicationListener application) {
        this.application = application;
    }

    @Override
    public void create() {
        application.create();
        Assets.instance.init();

        stage = new Stage(new ScreenViewport());
        monitorTable = new MonitorTable();
        stage.addActor(monitorTable);
    }

    @Override
    public void resize(int width, int height) {
        application.resize(width, height);
        if (screenWidth != width || screenHeight != height) {
            screenWidth = width;
            screenHeight = height;
            screenSizeChanged = true;
        }
    }

    @Override
    public void render() {
        application.render();

        InputProcessor inputProcessor = Gdx.input.getInputProcessor();
        if (multiplexer != inputProcessor) {
            multiplexer.clear();
            multiplexer.addProcessor(stage);
            if (inputProcessor != null) {
                multiplexer.addProcessor(inputProcessor);
            }
            Gdx.input.setInputProcessor(multiplexer);
        }

        if (timeSinceFindBatch >= 1f) {
            BatchFinder.findBatchObjects(application, outBatchObjects);
            if (outBatchObjects.size > 0) {
                alreadyDisposedBatch.clear();
                for (Array<FieldObject> batchFieldObjects : groupBatch()) {
                    batchMeters.add(replaceBatchWithMeter(batchFieldObjects));
                }
                outBatchObjects.clear();
            }
            timeSinceFindBatch = 0;
        }

        if (timeSinceUpdateData >= 0.2f) {
            updateData();
            timeSinceUpdateData = 0;
        }

        IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
        Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, intBuffer);
        intBuffer.get(applicationViewportAttribute, 0, 4);

        stage.getViewport().update(screenWidth, screenHeight);
        if (screenSizeChanged) {
            monitorTable.onStageSizeChanged(screenWidth, screenHeight);
            screenSizeChanged = false;
        }

        stage.act();
        stage.draw();

        HdpiUtils.glViewport(applicationViewportAttribute[0], applicationViewportAttribute[1],
                applicationViewportAttribute[2], applicationViewportAttribute[3]);

        final float delta = Gdx.graphics.getRawDeltaTime();
        timeSinceUpdateData += delta;
        timeSinceFindBatch += delta;
    }

    private ObjectMap.Values<Array<FieldObject>> groupBatch() {
        final ObjectMap<SpriteBatch, Array<FieldObject>> batchFieldsObjectMap
                = new ObjectMap<SpriteBatch, Array<FieldObject>>();

        for (FieldObject batchObject : outBatchObjects) {
            final SpriteBatch batch = (SpriteBatch) batchObject.getFieldValue();
            Array<FieldObject> fieldObjects = batchFieldsObjectMap.get(batch);
            if (fieldObjects == null) {
                fieldObjects = new Array<FieldObject>();
                batchFieldsObjectMap.put(batch, fieldObjects);
            }
            fieldObjects.add(batchObject);
        }
        return batchFieldsObjectMap.values();
    }

    @Override
    public void pause() {
        application.pause();
    }

    @Override
    public void resume() {
        application.resume();
    }

    @Override
    public void dispose() {
        application.dispose();
        stage.dispose();
        Assets.instance.dispose();
    }

    private BatchMeter replaceBatchWithMeter(final Array<FieldObject> batchFieldObjects) {
        try {
            SpriteBatch originalBatch = (SpriteBatch) batchFieldObjects.first().getFieldValue();

            Field verticesField = ClassReflection.getDeclaredField(SpriteBatch.class, "vertices");
            verticesField.setAccessible(true);
            final float[] vertices = (float[]) verticesField.get(originalBatch);
            final int SPRITE_SIZE = 20;

            StringBuilder sb = new StringBuilder();
            sb.append(batchFieldObjects.first());
            for (int i = 1; i < batchFieldObjects.size; i++) {
                sb.append("\n\n").append(batchFieldObjects.get(i));
            }
            BatchMeter newBatch = new BatchMeter(vertices.length / SPRITE_SIZE, batchMeters, sb.toString());

            for (FieldObject batchFieldObject : batchFieldObjects) {
                batchFieldObject.setFieldValue(newBatch);
            }
            if (!alreadyDisposedBatch.contains(originalBatch, true)) {
                originalBatch.dispose();
                alreadyDisposedBatch.add(originalBatch);
            }
            return newBatch;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void updateData() {
        allRecords.clear();
        int totalRenderCalls = 0;
        for (BatchMeter batchMeter : batchMeters) {
            batchMeter.validateRecords(Gdx.graphics.getFrameId());
            BatchRecordContainer batchRecordContainer = batchMeter.getCompletedRecords();
            allRecords.add(batchRecordContainer);
            totalRenderCalls += batchRecordContainer.getRenderCalls();
        }
        monitorTable.updateData(totalRenderCalls, allRecords);
    }
}
