package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

class RenderCallsGraph extends Table {
    
    private static final Drawable lightRed;
    private static final Drawable darkRed;
    private static final Drawable lightYellow;
    private static final Drawable darkYellow;
    private static final Drawable lightGreen;
    private static final Drawable darkGreen;

    static {
        lightRed = Assets.instance.white.tint(Color.valueOf("ff5555ff"));
        darkRed = Assets.instance.white.tint(Color.valueOf("2b0000ff"));
        lightYellow = Assets.instance.white.tint(Color.valueOf("ffdd55ff"));
        darkYellow = Assets.instance.white.tint(Color.valueOf("2b2200ff"));
        lightGreen = Assets.instance.white.tint(Color.valueOf("5fd35fff"));
        darkGreen = Assets.instance.white.tint(Color.valueOf("0b280bff"));
    }

    private final Image image;
    private final Cell cell;

    RenderCallsGraph() {
        image = new Image();
        add(new Actor()).grow();
        row();
        bottom();
        cell = add(image);
    }
    
    public void init(int renderCalls, int totalRenderCall, int width, int height){
        final float percent = (float) renderCalls / totalRenderCall;
        if (percent > 0.66f) {
            setBackground(darkRed);
            image.setDrawable(lightRed);
        } else if (percent > 0.33f) {
            setBackground(darkYellow);
            image.setDrawable(lightYellow);
        } else {
            setBackground(darkGreen);
            image.setDrawable(lightGreen);
        }
        cell.size(width, height * percent);
        invalidate();
    }
}
