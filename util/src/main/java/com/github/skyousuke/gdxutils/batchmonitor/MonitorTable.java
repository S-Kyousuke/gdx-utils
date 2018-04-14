package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

import java.util.Comparator;

class MonitorTable extends Table {
    private static final Comparator<BatchRecordContainer> recordGroupComparator = new Comparator<BatchRecordContainer>() {
        @Override
        public int compare(BatchRecordContainer group1, BatchRecordContainer group2) {
            return group2.records.size - group1.records.size;
        }
    };

    private final Cell scrollPaneCell;
    private final Table scrollTable;
    private boolean recordShowing;
    private Vector2 newPosition = new Vector2();

    private int screenHeight;

    public MonitorTable() {
        NinePatchDrawable whiteNinePatchDrawable = new NinePatchDrawable(
                new NinePatch(Assets.instance.uiAtlas.findRegion("white")));

        Drawable vScrollKnob = whiteNinePatchDrawable.tint(new Color(0.5f, 0.5f, 0.5f, 1f));
        Drawable hScrollKnob = whiteNinePatchDrawable.tint(new Color(0.5f, 0.5f, 0.5f, 1f));

        hScrollKnob.setMinHeight(6);
        vScrollKnob.setMinWidth(6);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.hScrollKnob = hScrollKnob;
        scrollPaneStyle.vScrollKnob = vScrollKnob;
        scrollPaneStyle.background = Assets.instance.white.tint(new Color(0, 0, 0, 0.5f));

        final ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = scrollPaneStyle.background;
        imageButtonStyle.over = Assets.instance.white.tint(new Color(0, 0, 0, 0.4f));
        imageButtonStyle.down = scrollPaneStyle.background;

        TextureRegion arrowRegion = Assets.instance.uiAtlas.findRegion("arrow");
        Sprite upArrowSprite = new Sprite(arrowRegion);
        final Sprite downArrowSprite = new Sprite(arrowRegion);
        upArrowSprite.rotate(90);
        downArrowSprite.rotate(-90);

        final Drawable downArrow = new SpriteDrawableWithRotation(downArrowSprite);
        final Drawable upArrow = new SpriteDrawableWithRotation(upArrowSprite);

        imageButtonStyle.imageUp = upArrow;

        scrollTable = new Table();
        scrollTable.left();

        ScrollPane scrollPane = new ScrollPane(scrollTable, scrollPaneStyle);
        scrollPane.setupFadeScrollBars(1f, 0.25f);

        ImageButton toggleButton = new ImageButton(imageButtonStyle);
        toggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (recordShowing) {
                    addAction(Actions.moveBy(0, -getHeight() + 10, getHeight() * 0.005f));
                    imageButtonStyle.imageUp = upArrow;
                } else {
                    addAction(Actions.moveBy(0, getHeight() - 10, getHeight() * 0.005f));
                    imageButtonStyle.imageUp = downArrow;
                }
                recordShowing = !recordShowing;
            }
        });
        row();
        add(toggleButton).fillX().center().height(10);
        row();
        scrollPaneCell = add(scrollPane).left();
    }

    public void updateData(int totalRenderCalls, Array<BatchRecordContainer> recordGroups) {
        if (getActions().size > 0)
            return;

        recordGroups.sort(recordGroupComparator);
        scrollTable.clearChildren();
        for (int i = 0; i < recordGroups.size; i++) {
            scrollTable.row().left();
            RecordsTable recordsTable = Pools.obtain(RecordsTable.class);
            recordsTable.init(i+1, totalRenderCalls, recordGroups.get(i));
            scrollTable.add(recordsTable).padRight(20).spaceBottom(5);
        }
        final float prefHeight = getPrefHeight();
        if (getHeight() != prefHeight) {
            setHeight(prefHeight);
            updatePosition(screenHeight);
        }
    }

    public void onStageSizeChanged(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        scrollPaneCell.width(screenWidth);
        scrollPaneCell.maxHeight(screenHeight);
        invalidate();
        setSize(getPrefWidth(), getPrefHeight());
        updatePosition(screenHeight);
    }

    private void updatePosition(int screenHeight) {
        if (recordShowing) {
            getStage().screenToStageCoordinates(newPosition.set(0, screenHeight - 1f));
        } else {
            getStage().screenToStageCoordinates(newPosition.set(0, screenHeight - 1f + getHeight() - 10));
        }
        setPosition(newPosition.x, newPosition.y);
    }

    private static class SpriteDrawableWithRotation extends SpriteDrawable {
        private SpriteDrawableWithRotation(Sprite sprite) {
            super(sprite);
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            Color spriteColor = getSprite().getColor();
            float batchColor = batch.getPackedColor();
            getSprite().setColor(batch.getColor().mul(spriteColor));
            getSprite().setScale(1, 1);
            getSprite().setBounds(x, y, width, height);
            getSprite().draw(batch);

            getSprite().setColor(spriteColor);
            batch.setColor(batchColor);
        }
    }
}
