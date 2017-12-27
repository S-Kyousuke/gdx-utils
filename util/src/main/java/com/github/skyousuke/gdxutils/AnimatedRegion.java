package com.github.skyousuke.gdxutils;

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
    private Animation<RegionData> animation;
    private String animationName;

    private float defaultFrameDuration;
    private float playbackScale = 1.0f;
    private boolean playing = true;

    private boolean flipX;
    private boolean flipY;

    private float drawPositionX;
    private float drawPositionY;

    public AnimatedRegion(String animationFilePath, TextureAtlas atlas, float pixelPerWorldUnit, String firstAnimation) {
        data = new AnimatedRegionData(Gdx.files.internal(animationFilePath), atlas);
        this.pixelPerWorldUnit = pixelPerWorldUnit;
        animationName = firstAnimation;
        updateAnimation();
    }

    public AnimatedRegion(String animationFilePath, TextureAtlas atlas, float pixelPerWorldUnit) {
        data = new AnimatedRegionData(Gdx.files.internal(animationFilePath), atlas);
        this.pixelPerWorldUnit = pixelPerWorldUnit;
        animationName = data.animations.getKeyAt(0);
        updateAnimation();
    }

    public void update(float deltaTime) {
        if (!playing)
            return;

        time += deltaTime;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        final RegionData regionData = animation.getKeyFrame(time);
        final float scale = data.regionScales.get(animationName, 1) / pixelPerWorldUnit;

        updateDrawPosition(regionData, x, y);

        boolean needToFlipX = false;
        boolean needToFlipY = false;

        if (regionData.region.isFlipX() != flipX)
            needToFlipX = true;

        if (regionData.region.isFlipY() != flipY)
            needToFlipY = true;

        regionData.region.flip(needToFlipX, needToFlipY);

        batch.draw(regionData.region,
                drawPositionX, drawPositionY,
                regionData.pivotX, regionData.pivotY,
                regionData.region.getRegionWidth(), regionData.region.getRegionHeight(),
                scale, scale,
                0);
    }

    public void drawDebug(ShapeRenderer renderer, float x, float y) {
        final RegionData regionData = animation.getKeyFrame(time);
        final float scale = data.regionScales.get(animationName, 1) / pixelPerWorldUnit;

        updateDrawPosition(regionData, x, y);

        // bounding box
        renderer.setColor(Color.RED);
        renderer.rect(drawPositionX, drawPositionY,
                regionData.pivotX, regionData.pivotY,
                regionData.region.getRegionWidth(), regionData.region.getRegionHeight(),
                scale, scale,
                0);

        // x y
        renderer.setColor(Color.CYAN);
        RenderUtils.drawCrosshair(renderer, x, y, 4);
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

    public void setPlaybackScale(float scale) {
        if (scale <= 0) {
            throw new IllegalArgumentException("playback scale can't lower than or equal to zero: " + scale);
        }
        time *= (playbackScale / scale);
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

    @SuppressWarnings("unchecked")
    public Array<String> getNames() {
        return new ArrayMap.Keys(data.animations).toArray();
    }

    public void reset() {
        time = 0;
    }

    public float getTime() {
        return time;
    }

    public Animation.PlayMode getPlayMode() {
        return animation.getPlayMode();
    }

    public float getFrameDuration() {
        return animation.getFrameDuration();
    }

    public int getFrameSize() {
        return animation.getKeyFrames().length;
    }

    public int getKeyFrameIndex() {
        return animation.getKeyFrameIndex(time);
    }

    public RegionData getKeyFrameData() {
        return animation.getKeyFrame(time);
    }

    public float getRegionScale() {
        return data.regionScales.get(animationName, 1);
    }

    @Override
    public String toString() {
        return animationName + " " + animation.getKeyFrame(time).region.name
                + " " + time + " playing=" + playing;
    }

    private void updateAnimation() {
        animation = data.animations.get(animationName);

        if (animation == null)
            throw new IllegalArgumentException("Animation not found!: " + animationName);

        defaultFrameDuration = animation.getFrameDuration();
        animation.setFrameDuration(defaultFrameDuration / playbackScale);
    }

    private float getScale() {
        return data.regionScales.get(animationName, 1) / pixelPerWorldUnit;
    }

    private void updateDrawPosition(RegionData regionData, float x, float y) {
        final float pivotX = regionData.pivotX * getScale();
        final float pivotY = regionData.pivotY * getScale();
        final float scaleOffsetX = regionData.pivotX * getScale() - regionData.pivotX;
        final float scaleOffsetY = regionData.pivotY * getScale() - regionData.pivotY;
        final float offsetX = flipX ? -regionData.offsetX * getScale() : regionData.offsetX * getScale();
        final float offsetY = flipY ? -regionData.offsetY * getScale() : regionData.offsetY * getScale();
        final float flipOffsetX = flipX ? (2 * regionData.pivotX - regionData.region.getRegionWidth()) * getScale() : 0;
        final float flipOffsetY = flipY ? (2 * regionData.pivotY - regionData.region.getRegionHeight()) * getScale() : 0;
        drawPositionX = x - pivotX + scaleOffsetX + offsetX + flipOffsetX;
        drawPositionY = y - pivotY + scaleOffsetY + offsetY + flipOffsetY;
    }


    /**
     * @return the key frame index percent (in decimal form) of total frame.
     */
    public static float getKeyFramePercent(AnimatedRegion animatedRegion) {
        final Animation animation = animatedRegion.animation;
        final float animationDuration = animation.getAnimationDuration();

        switch (animation.getPlayMode()) {
            case NORMAL:
                return Math.min(animatedRegion.time, animationDuration) / animationDuration;
            case LOOP:
                return (animatedRegion.time % animationDuration) / animationDuration;
            case LOOP_REVERSED:
                return (animationDuration - (animatedRegion.time % animationDuration)) / animationDuration;
            case REVERSED:
                return (animationDuration - Math.min(animatedRegion.time, animationDuration)) / animationDuration;
            case LOOP_PINGPONG:
                final float frameDuration = animation.getFrameDuration();
                final int totalFrame = animation.getKeyFrames().length;
                final float cycleDuration = frameDuration * ((totalFrame * 2) - 2);
                final float timeInCycle = animatedRegion.time % cycleDuration;
                final float time;
                if (timeInCycle >= animationDuration) {
                    time = animationDuration - (2 * frameDuration) - (timeInCycle - animationDuration);
                } else {
                    time = timeInCycle;
                }
                return time / animationDuration;
            case LOOP_RANDOM:
                return (float) animation.getKeyFrameIndex(animatedRegion.time) / animation.getKeyFrames().length;
            default:
                throw new IllegalStateException("the execution flow must not reach here!");
        }
    }

    public static void setKeyFrameIndex(AnimatedRegion animatedRegion, int frameIndex) {
        final int frameSize = animatedRegion.animation.getKeyFrames().length;
        if (frameIndex < 0 || frameIndex > frameSize) {
            throw new IllegalArgumentException("frame index out of range: " + frameIndex);
        }
        final Animation.PlayMode playMode = animatedRegion.animation.getPlayMode();
        final float frameDuration = animatedRegion.animation.getFrameDuration();
        switch (playMode) {
            case NORMAL:
            case LOOP_PINGPONG:
            case LOOP:
                animatedRegion.time = frameIndex * frameDuration;
                break;
            case LOOP_REVERSED:
            case REVERSED:
                final int reversedIndex = animatedRegion.animation.getKeyFrames().length - 1 - frameIndex;
                animatedRegion.time = reversedIndex * frameDuration;
                break;
            case LOOP_RANDOM:
                animatedRegion.animation.setPlayMode(Animation.PlayMode.NORMAL);
                animatedRegion.time = frameIndex * frameDuration;
                animatedRegion.animation.getKeyFrameIndex(animatedRegion.time);
                animatedRegion.animation.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
                break;
        }
    }

    private static class AnimatedRegionData {

        private final ArrayMap<String, Animation<RegionData>> animations;
        private final ObjectFloatMap<String> regionScales;
        private final TextureAtlas animationAtlas;

        AnimatedRegionData(FileHandle animationJson, TextureAtlas atlas) {
            animations = new ArrayMap<String, Animation<RegionData>>();
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
                final Animation<RegionData> animation = new Animation<RegionData>(frameDuration, allRegionData);
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

                JsonValue originJson = regionJson.get("offset");
                if (originJson != null) {
                    float[] offset = originJson.asFloatArray();
                    regionData.offsetX = offset[0];
                    regionData.offsetY = offset[1];
                } else {
                    regionData.offsetX = 0;
                    regionData.offsetY = 0;
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
    }

    public static class RegionData {
        public TextureAtlas.AtlasRegion region;
        public float pivotX;
        public float pivotY;
        public float offsetX;
        public float offsetY;
    }
}
