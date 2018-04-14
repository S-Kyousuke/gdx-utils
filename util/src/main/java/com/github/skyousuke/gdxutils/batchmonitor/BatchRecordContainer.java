package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

class BatchRecordContainer {

    public final Array<BatchRecord> records = new Array<BatchRecord>(BatchRecord.class);
    public final String label;
    private int renderCalls;

    public BatchRecordContainer() {
        this.label = "";
    }

    public BatchRecordContainer(String label) {
        this.label = label;
    }

    public void add(BatchRecord record) {
        records.add(record);
        if (record.getState() == BatchMeter.BatchState.RENDER) {
            renderCalls++;
        }
    }

    public void addAll(BatchRecordContainer batchRecordContainer) {
        records.addAll(batchRecordContainer.records);
        renderCalls = batchRecordContainer.renderCalls;
    }

    public void clear() {
        records.clear();
        renderCalls = 0;
    }

    public void free() {
        BatchRecord[] items = records.items;
        for (int i = 0, n = records.size; i < n; i++) {
            Pools.free(items[i]);
            items[i] = null;
        }
        records.size = 0;
        renderCalls = 0;
    }

    public int getRenderCalls() {
        return renderCalls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchRecordContainer that = (BatchRecordContainer) o;

        if (renderCalls != that.renderCalls) return false;
        return records.equals(that.records);
    }

    @Override
    public int hashCode() {
        int result = records.hashCode();
        result = 31 * result + renderCalls;
        return result;
    }
}
