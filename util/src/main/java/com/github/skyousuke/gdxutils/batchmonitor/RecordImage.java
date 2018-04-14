package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

class RecordImage extends Image {
    private static final TextureRegion circleRegion = Assets.instance.uiAtlas.findRegion("circle");

    private static final TextTooltip beginToolTip;
    private static final TextTooltip endToolTip;
    private static final ObjectMap<Texture, Tooltip<Image>> renderTooltips = new ObjectMap<Texture, Tooltip<Image>>();

    private static final BeginRecordImagePool beginRecordImagePool = new BeginRecordImagePool();
    private static final EndRecordImagePool endRecordImagePool = new EndRecordImagePool();
    private static final ObjectMap<Texture, RenderRecordImagePool> renderRecordImagePools
            = new ObjectMap<Texture, RenderRecordImagePool>();

    static {
        final TextTooltip.TextTooltipStyle tooltipStyle = new TextTooltip.TextTooltipStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Assets.instance.font;
        labelStyle.fontColor = Color.WHITE;

        tooltipStyle.label = labelStyle;
        tooltipStyle.background = Assets.instance.white.tint(new Color(0, 0, 0, 0.5f));

        beginToolTip = new TextTooltip("begin()", tooltipStyle);
        beginToolTip.setInstant(true);

        endToolTip = new TextTooltip("end()", tooltipStyle);
        endToolTip.setInstant(true);
    }

    private Pool<RecordImage> pool;

    private RecordImage() {
        super(circleRegion);
    }

    public static RecordImage obtain(BatchRecord record) {
        switch (record.getState()) {
            case RENDER:
                return getRenderRecordImagePool(record.getBindingTexture()).obtain();
            case BEGIN:
                return beginRecordImagePool.obtain();
            case END:
                return endRecordImagePool.obtain();
            default:
                throw new IllegalArgumentException("Wrong batch state: " + record.getState());
        }
    }

    private static RenderRecordImagePool getRenderRecordImagePool(Texture texture) {
        RenderRecordImagePool renderRecordImagePool = renderRecordImagePools.get(texture);
        if (renderRecordImagePool == null) {
            renderRecordImagePool = new RenderRecordImagePool(texture);
            renderRecordImagePools.put(texture, renderRecordImagePool);
        }
        return renderRecordImagePool;
    }


    private void setTooltip(Tooltip tooltip) {
        if (!getListeners().contains(tooltip, true)) {
            clearListeners();
            addListener(tooltip);
        }
    }

    @Override
    public boolean remove() {
        if (super.remove()) {
            pool.free(this);
            return true;
        }
        return false;
    }

    private static class RenderRecordImagePool extends Pool<RecordImage> {
        private Texture texture;

        private RenderRecordImagePool(Texture texture) {
            super(4);
            this.texture = texture;
        }

        @Override
        protected RecordImage newObject() {
            RecordImage recordImage = new RecordImage();
            recordImage.setColor(Color.valueOf("ff5555ff"));
            recordImage.setTooltip(getTextureToolTips(texture));
            recordImage.pool = this;
            return recordImage;
        }

        private static Tooltip<Image> getTextureToolTips(Texture texture) {
            Tooltip<Image> tooltip = renderTooltips.get(texture);
            if (tooltip != null) {
                return tooltip;
            }
            tooltip = new Tooltip<Image>(new Image(texture));
            tooltip.setInstant(true);
            renderTooltips.put(texture, tooltip);
            return tooltip;
        }
    }

    private static class BeginRecordImagePool extends Pool<RecordImage> {

        private BeginRecordImagePool() {
            super(4);
        }

        @Override
        protected RecordImage newObject() {
            RecordImage recordImage = new RecordImage();
            recordImage.setColor(Color.valueOf("ffdd55ff"));
            recordImage.setTooltip(beginToolTip);
            recordImage.pool = this;
            return recordImage;
        }
    }

    private static class EndRecordImagePool extends Pool<RecordImage> {

        private EndRecordImagePool() {
            super(4);
        }

        @Override
        protected RecordImage newObject() {
            RecordImage recordImage = new RecordImage();
            recordImage.setColor(Color.valueOf("37c871ff"));
            recordImage.setTooltip(endToolTip);
            recordImage.pool = this;
            return recordImage;
        }
    }
}
