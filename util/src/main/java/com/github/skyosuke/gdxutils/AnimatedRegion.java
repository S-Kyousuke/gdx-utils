package com.github.skyosuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.*;
import com.google.common.base.CaseFormat;

public class AnimatedRegion {

    private AnimatedRegionData data;
    private float time;
    private Animation<Object> animation;
    private String animationName;

    private float defaultFrameDuration;
    private float playbackScale = 1.0f;
    private boolean playing = true;

    public AnimatedRegion(String animationFileInternalPath, TextureAtlas atlas, String firstAnimation) {
        data = new AnimatedRegionData(Gdx.files.internal(animationFileInternalPath), atlas);
        animationName = firstAnimation;
        updateAnimation();
    }

    public void update(float deltaTime) {
        if (!playing)
            return;

        time += deltaTime;
    }

    public AtlasRegion getFrame() {
        return (AtlasRegion) animation.getKeyFrame(time);
    }

    public String getName() {
        return animationName;
    }

    public void setAnimation(String name) {
        setAnimation(name, true);
    }

    public void setAnimation(String name, boolean resetTime) {
        animation.setFrameDuration(defaultFrameDuration);

        animationName = name;
        updateAnimation();

        if (resetTime)
            time = 0;
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

    public void stop() {
        playing = false;
        time = 0;
    }

    public void pause() {
        playing = false;
    }

    public void play() {
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(time);
    }

    public boolean isAnimationEquals(String name) {
        return name.equals(animationName);
    }

    @Override
    public String toString() {
        return animationName + " " + getFrame().name + " " + time + " playing=" + playing;
    }

    private static class AnimatedRegionData implements Json.Serializable {

        private ArrayMap<String, Animation<Object>> animations;
        private TextureAtlas animationAtlas;

        public AnimatedRegionData(FileHandle animationJson, TextureAtlas atlas) {
            animations = new ArrayMap<String, Animation<Object>>();
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
                final Array<AtlasRegion> regionNames = getRegions(animationAtlas, eachJson.get("regions").asStringArray());
                Animation<Object> animation = new Animation<Object>(frameDuration, regionNames, playMode);
                animations.put(animationName, animation);
            }
            Pools.free(jsonReader);
        }

        @Override
        public void write(Json json) {
            for (int i = 0; i < animations.size; i++) {
                final String animationName = animations.getKeyAt(i);
                final Animation<Object> animation = animations.getValueAt(i);

                json.writeObjectStart(animationName);
                json.writeValue("playMode", getPlayModeAsString(animation.getPlayMode()));
                json.writeValue("frameDuration", animation.getFrameDuration());
                json.writeArrayStart("regions");
                for (int j = 0; j < animation.getKeyFrames().length; j++) {
                    json.writeValue(((AtlasRegion) animation.getKeyFrames()[j]).name);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            throw new UnsupportedOperationException();
        }

        private static Array<AtlasRegion> getRegions(TextureAtlas atlas, String[] regionNames) {
            Array<AtlasRegion> regions = new Array<AtlasRegion>();
            for (String regionName : regionNames) {
                regions.add(atlas.findRegion(regionName));
            }
            return regions;
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
}
