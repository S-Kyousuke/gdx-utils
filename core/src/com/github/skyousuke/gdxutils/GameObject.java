package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameObject {
    Vector2 position = new Vector2(0, 0);
    Vector2 origin = new Vector2(0, 0);
    Vector2 dimension = new Vector2(0, 0);
    Vector2 scale = new Vector2(1, 1);
    Vector2 pivot = new Vector2(0, 0);
    float rotation = 0;

    public GameObject(float x, float y, float originX, float originY, float width, float height, float pivotX, float pivotY) {
        position.set(x, y);
        origin.set(originX, originY);
        dimension.set(width, height);
        pivot.set(pivotX, pivotY);
    }

    public void draw(ShapeRenderer renderer, Color pivotColor) {
        renderer.setColor(Color.WHITE);
        final float x = position.x - pivot.x;
        final float y = position.y - pivot.y;

        renderer.rect(
                x, y,
                origin.x, origin.y,
                dimension.x, dimension.y,
                scale.x, scale.y,
                rotation);

        renderer.setColor(Color.RED);
        RenderUtils.drawCrosshair(renderer, position.x, position.y, 8);

        renderer.setColor(pivotColor);
        RenderUtils.drawCrosshair(renderer, x + pivot.x, y + pivot.y, 8);
    }
}
