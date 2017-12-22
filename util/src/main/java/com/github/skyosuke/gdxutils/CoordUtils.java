package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CoordUtils {

    private CoordUtils() {
    }

    private static final Vector3 tempCoords3 = new Vector3();

    public static Vector2 worldToStage(float worldX, float worldY, Camera worldCamera, Viewport worldViewport, Stage stage) {
        Vector2 screenPos = worldToScreen(worldX, worldY, worldCamera, worldViewport);
        Vector2 touchPos = screenToTouch(screenPos.x, screenPos.y);

        Vector2 stageCoord = touchToStage(touchPos.x, touchPos.y, stage);
        Pools.free(screenPos);
        Pools.free(touchPos);
        return stageCoord;
    }

    public static Vector2 worldToScreen(float worldX, float worldY, Camera worldCamera, Viewport worldViewport) {
        tempCoords3.set(worldX, worldY, 0);

        worldCamera.project(tempCoords3, worldViewport.getScreenX(), worldViewport.getScreenY(),
                worldViewport.getScreenWidth(), worldViewport.getScreenHeight());

        return Pools.obtain(Vector2.class).set(tempCoords3.x, tempCoords3.y) ;
    }

    public static Vector2 touchToWorld(float touchX, float touchY,
                                       Camera worldCamera, Viewport worldViewport) {
        tempCoords3.set(touchX, touchY, 0);

        worldCamera.unproject(tempCoords3, worldViewport.getScreenX(), worldViewport.getScreenY(),
                worldViewport.getScreenWidth(), worldViewport.getScreenHeight());

        return Pools.obtain(Vector2.class).set(tempCoords3.x, tempCoords3.y);
    }

    public static Vector2 touchToStage(float touchX, float touchY, Stage stage) {
        tempCoords3.set(touchX, touchY, 0);

        stage.getCamera().unproject(tempCoords3, stage.getViewport().getScreenX(), stage.getViewport().getScreenY(),
                stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());

        return Pools.obtain(Vector2.class).set(tempCoords3.x, tempCoords3.y);
    }

    public static Vector2 touchToScreen(float touchX, float touchY) {
        return Pools.obtain(Vector2.class).set(touchX, Gdx.graphics.getHeight() - 1 - touchY);
    }

    public static Vector2 screenToTouch(float screenX, float screenY) {
        return Pools.obtain(Vector2.class).set(screenX, Gdx.graphics.getHeight() - 1 - screenY);
    }

}
