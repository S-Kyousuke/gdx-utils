package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RenderUtils {

    private RenderUtils() {
    }

    public static void drawCrosshair(ShapeRenderer renderer, float x, float y, int size) {
        renderer.line(x - (size + 1), y, x + size, y);
        renderer.line(x, y - size, x, y + (size + 1));
    }

    public static void drawXAxis(ShapeRenderer renderer, float y, Camera camera) {
        final float leftEdge = camera.position.x - camera.viewportWidth * 0.5f;
        final float rightEdge = camera.position.x + camera.viewportWidth * 0.5f;
        renderer.line(leftEdge, y, rightEdge, y);
    }

    public static void drawYAxis(ShapeRenderer renderer, float x, Camera camera) {
        final float bottomEdge = camera.position.y - camera.viewportHeight * 0.5f;
        final float topEdge = camera.position.y + camera.viewportHeight * 0.5f;
        renderer.line(x, bottomEdge, x, topEdge);
    }
}
