package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.Texture;

class BatchRecord {

    private long frameId;
    private BatchMeter.BatchState state;
    private Texture bindingTexture;

    BatchRecord() {
    }

    public void setData(long frameId, BatchMeter.BatchState state, Texture bindingTexture) {
        this.frameId = frameId;
        this.state = state;
        this.bindingTexture = bindingTexture;
    }

    public long getFrameId() {
        return frameId;
    }

    public BatchMeter.BatchState getState() {
        return state;
    }

    public Texture getBindingTexture() {
        return bindingTexture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchRecord record = (BatchRecord) o;

        if (state != record.state) return false;
        return bindingTexture != null ? bindingTexture.equals(record.bindingTexture) : record.bindingTexture == null;
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + (bindingTexture != null ? bindingTexture.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "frameId=" + frameId +
                ", state=" + state +
                ", bindingTexture=" + bindingTexture +
                '}';
    }
}
