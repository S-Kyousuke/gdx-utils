package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.*;
import com.google.common.base.CaseFormat;

public class AnimatedRegion {

    private final AnimatedRegionData data;
    private final float pixelPerWorldUnit;

    private float time;
    private Animation<AnimatedRegionData.RegionData> animation;
    private String animationName;

    private float defaultFrameDuration;
    private float playbackScale = 1.0f;
    private boolean playing = true;

    private boolean flipX;
    private boolean flipY;

    public AnimatedRegion(String animationFilePath, TextureAtlas atlas, String firstAnimation, float pixelPerWorldUnit) {
        data = new AnimatedRegionData(Gdx.files.internal(animationFilePath), atlas);
        this.pixelPerWorldUnit = pixelPerWorldUnit;
        animationName = firstAnimation;
        updateAnimation();
    }

    public void update(float deltaTime) {
        if (!playing)
            return;

        time += deltaTime;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        AnimatedRegionData.RegionData regionData = animation.getKeyFrame(time);

        final float scale = data.regionScales.get(animationName, 1) / pixelPerWorldUnit;
        final float pivotX = regionData.pivotX * scale;
        final float pivotY = regionData.pivotY * scale;
        final float scaleOffsetX = regionData.originX * scale - regionData.originX;
        final float scaleOffsetY = regionData.originY * scale - regionData.originY;
        final float flipOffsetX = flipX ?  scale * (2 *  regionData.originX - regionData.region.getRegionWidth()) : 0;
        final float flipOffsetY = flipY ?  scale * (2 *  regionData.originY - regionData.region.getRegionHeight()) : 0;
        final float adjustedX = x + pivotX + scaleOffsetX + flipOffsetX;
        final float adjustedY = y + pivotY + scaleOffsetY + flipOffsetY;

        boolean needToFlipX = false;
        boolean needToFlipY = false;

        if (regionData.region.isFlipX() != flipX)
            needToFlipX = true;

        if (regionData.region.isFlipY() != flipY)
            needToFlipY = true;

        regionData.region.flip(needToFlipX, needToFlipY);

        batch.draw(regionData.region,
                adjustedX, adjustedY,
                regionData.originX, regionData.originY,
                regionData.region.getRegionWidth(), regionData.region.getRegionHeight(),
                scale, scale,
                0);
    }

    public void drawDebug(ShapeRenderer renderer, float x, float y) {
        AnimatedRegionData.RegionData regionData = animation.getKeyFrame(time);

        final float scale = data.regionScales.get(animationName, 1) / pixelPerWorldUnit;

        final float pivotX = regionData.pivotX * scale;
        final float pivotY = regionData.pivotY * scale;
        final float scaleOffsetX = regionData.originX * scale - regionData.originX;
        final float scaleOffsetY = regionData.originY * scale - regionData.originY;
        final float flipOffsetX = flipX ?  scale * (2 *  regionData.originX - regionData.region.getRegionWidth()) : 0;
        final float flipOffsetY = flipY ?  scale * (2 *  regionData.originY - regionData.region.getRegionHeight()) : 0;
        final float adjustedX = x + pivotX + scaleOffsetX + flipOffsetX;
        final float adjustedY = y + pivotY + scaleOffsetY + flipOffsetY;

        // bounding box
        renderer.setColor(Color.RED);
        renderer.rect(adjustedX, adjustedY,
                regionData.originX, regionData.originY,
                regionData.region.getRegionWidth(), regionData.region.getRegionHeight(),
                scale, scale,
                0);

        // world pivot
        renderer.setColor(Color.CYAN);
        renderer.line(x + pivotX - 5 / pixelPerWorldUnit, y + pivotY, x + pivotX + 4 / pixelPerWorldUnit, y + pivotY);
        renderer.line(x + pivotX, y + pivotY - 4 / pixelPerWorldUnit, x + pivotX, y + pivotY + 5 / pixelPerWorldUnit);

        // world origin
        renderer.setColor(Color.GREEN);
        final float worldOriginX = x + pivotX + regionData.originX * scale;
        final float worldOriginY = y + pivotY + regionData.originY * scale;
        renderer.line(worldOriginX - 5 / pixelPerWorldUnit, worldOriginY, worldOriginX + 4 / pixelPerWorldUnit, worldOriginY);
        renderer.line(worldOriginX, worldOriginY - 4 / pixelPerWorldUnit, worldOriginX, worldOriginY + 5 / pixelPerWorldUnit);

        // x, y
        renderer.setColor(Color.WHITE);
        renderer.line(x - 5 / pixelPerWorldUnit, y, x + 4 / pixelPerWorldUnit, y);
        renderer.line(x, y - 4 / pixelPerWorldUnit, x, y + 5 / pixelPerWorldUnit);
    }

    public void setAnimation(String name) {
        setAnimation(name, 0);
    }

    public void setAnimation(String name, float time) {
        animation.setFrameDuration(defaultFrameDuration);

        animationName = name;
        updateAnimation();

        this.time = time;
    }

    private void updateAnimation() {
        animation = data.animations.get(animationName);

        if (animation == null)
            throw new IllegalArgumentException("Animation not found!: " + animationName);

        defaultFrameDuration = animation.getFrameDuration();
        animation.setFrameDuration(defaultFrameDuration / playbackScale);
    }

    public void setPlaybackScale(float scale) {
        playbackScale = scale;
        animation.setFrameDuration(defaultFrameDuration / playbackScale);
    }

    public float getPlaybackScale() {
        return playbackScale;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(time);
    }

    public String getName() {
        return animationName;
    }

    public boolean isAnimationName(String name) {
        return name.equals(animationName);
    }

    public void reset() {
        time = 0;
    }

    public float getTime() {
        return time;
    }

    @Override
    public String toString() {
        return animationName + " " + animation.getKeyFrame(time).region.name
                + " " + time + " playing=" + playing;
    }

    private static class AnimatedRegionData {

        private final ArrayMap<String, Animation<AnimatedRegionData.RegionData>> animations;
        private final ObjectFloatMap<String> regionScales;
        private final TextureAtlas animationAtlas;

        AnimatedRegionData(FileHandle animationJson, TextureAtlas atlas) {
            animations = new ArrayMap<String, Animation<AnimatedRegionData.RegionData>>();
            regionScales = new ObjectFloatMap<String>();
            this.animationAtlas = atlas;
            readJsonData(animationJson.readString("utf-8"));
        }

        private void readJsonData(String jsonData) {
            JsonReader jsonReader = Pools.obtain(JsonReader.class);
            JsonValue json = jsonReader.parse(jsonData);
            for (JsonValue eachJson : json) {
                final String animationName = eachJson.name;
                final Animation.PlayMode playMode = getPlayMode(eachJson.get("playMode").asString());
                final float frameDuration = eachJson.get("frameDuration").asFloat();
                final float regionScale = eachJson.get("regionScale").asFloat();
                final RegionData[] allRegionData = getAllRegionData(animationAtlas, eachJson.get("regions"));
                final Animation<AnimatedRegionData.RegionData> animation
                        = new Animation<AnimatedRegionData.RegionData>(frameDuration, allRegionData);
                animation.setPlayMode(playMode);
                regionScales.put(animationName, regionScale);
                animations.put(animationName, animation);
            }
            Pools.free(jsonReader);
        }

        private static RegionData[] getAllRegionData(TextureAtlas atlas, JsonValue regionsJson) {
            RegionData[] allRegionData = new RegionData[regionsJson.size];
            for (int i = 0; i < regionsJson.size; i++) {
                JsonValue regionJson = regionsJson.get(i);
                RegionData regionData = new RegionData();
                regionData.region = atlas.findRegion(regionJson.name);
                float[] pivot = regionJson.get("pivot").asFloatArray();
                regionData.pivotX = pivot[0];
                regionData.pivotY = pivot[1];
                allRegionData[i] = regionData;

                JsonValue originJson = regionJson.get("origin");
                if (originJson != null) {
                    float[] origin = originJson.asFloatArray();
                    regionData.originX = origin[0];
                    regionData.originY = origin[1];
                } else {
                    regionData.originX = regionData.region.getRegionWidth() * 0.5f;
                    regionData.originY = regionData.region.getRegionHeight() * 0.5f;
                }
            }
            return allRegionData;
        }

        private static String getPlayModeAsString(Animation.PlayMode playMode) {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, playMode.name());
        }

        private static Animation.PlayMode getPlayMode(String playMode) {
            for (Animation.PlayMode mode : Animation.PlayMode.values()) {
                if (playMode.equals(getPlayModeAsString(mode)))
                    return mode;
            }
            throw new IllegalArgumentException("Unsupported play mode: " + playMode);
        }

        private static class RegionData {
            private TextureAtlas.AtlasRegion region;
            private float originX;
            private float originY;
            private float pivotX;
            private float pivotY;
        }
    }
}
