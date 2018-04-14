package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

class BatchMeter extends SpriteBatch {

    public enum BatchState {
        BEGIN,
        END,
        RENDER,
    }

    private final BatchRecordContainer lastFrameRecords;
    private final BatchRecordContainer secondLastFrameRecords;
    private final BatchRecordContainer completedRecords;
    private final Array<BatchMeter> batchMeters;

    private long lastFrameId = -1;

    private Texture bindingTexture;

    BatchMeter(int size, Array<BatchMeter> batchMeters, String label) {
        super(size);
        lastFrameRecords = new BatchRecordContainer();
        secondLastFrameRecords = new BatchRecordContainer();
        completedRecords = new BatchRecordContainer(label);
        this.batchMeters = batchMeters;
    }

    @Override
    public void begin() {
        super.begin();
        record(Gdx.graphics.getFrameId(), BatchState.BEGIN, bindingTexture);
    }

    @Override
    public void end() {
        super.end();
        bindingTexture = null;
        record(Gdx.graphics.getFrameId(), BatchState.END, null);
        if (lastFrameRecords.records.size > 0 && lastFrameRecords.equals(secondLastFrameRecords)) {
            completedRecords.clear();
            completedRecords.addAll(lastFrameRecords);
        }
    }

    @Override
    protected void switchTexture(Texture texture) {
        super.switchTexture(texture);
        bindingTexture = texture;
    }

    @Override
    public void flush() {
        int beforeFlushRenderCalls = renderCalls;
        super.flush();
        if (beforeFlushRenderCalls != renderCalls) {
            record(Gdx.graphics.getFrameId(), BatchState.RENDER, bindingTexture);
        }
    }

    private void record(long frameId, BatchMeter.BatchState state, Texture bindingTexture) {
        BatchRecord record = Pools.obtain(BatchRecord.class);
        record.setData(frameId, state, bindingTexture);

        if (frameId != lastFrameId) {
            secondLastFrameRecords.free();
            secondLastFrameRecords.addAll(lastFrameRecords);
            lastFrameRecords.clear();
            lastFrameId = frameId;
        }
        lastFrameRecords.add(record);
    }

    public BatchRecordContainer getCompletedRecords() {
        return completedRecords;
    }

    public void validateRecords(long currentFrameId) {
        if (currentFrameId != lastFrameId)
            completedRecords.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
        batchMeters.removeValue(this, true);
    }
}
