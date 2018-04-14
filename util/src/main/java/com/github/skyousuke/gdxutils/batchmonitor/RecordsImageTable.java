package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

class RecordsImageTable extends Table {

    RecordsImageTable() {
        NinePatchDrawable drawable = new NinePatchDrawable(Assets.instance.uiAtlas.createPatch("round_rect"));
        setBackground(drawable);
        pad(6, 10, 6, 10);
    }

    public void init(Array<BatchRecord> records) {
        clear();
        row();
        for (int i = 0; i < records.size; i++) {
            add(RecordImage.obtain(records.get(i)));
            if (i != records.size - 1) {
                add(Pools.obtain(ArrowImage.class)).padLeft(2).padRight(2);
            }
        }
    }

    public static class ArrowImage extends Image {
        private static final TextureRegion arrowRegion = Assets.instance.uiAtlas.findRegion("arrow");

        private ArrowImage() {
            setDrawable(new TextureRegionDrawable(arrowRegion));
        }

        @Override
        public boolean remove() {
            if (super.remove()) {
                Pools.free(this);
                return true;
            }
            return false;
        }
    }
}
