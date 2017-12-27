package com.github.skyousuke.gdxutils.font;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class ThaiFontLoader extends AsynchronousAssetLoader<ThaiFont, ThaiFontLoader.ThaiFontParameter> {

    public ThaiFontLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    private BitmapFont.BitmapFontData data;

    @Override
    @SuppressWarnings("squid:S1186")
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, ThaiFontParameter parameter) {}

    @Override
    public ThaiFont loadSync(AssetManager manager, String fileName, FileHandle file, ThaiFontParameter parameter) {
        int imageCount = data.getImagePaths().length;
        Array<TextureRegion> regions = new Array<TextureRegion>(imageCount);
        for (int i = 0; i < imageCount; i++) {
            regions.add(new TextureRegion(manager.get(data.getImagePath(i), Texture.class)));
        }
        return new ThaiFont(data, regions, parameter);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ThaiFontParameter parameter) {
        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
        data = new BitmapFont.BitmapFontData(file, false);
        for (int i = 0; i < data.getImagePaths().length; i++) {
            String path = data.getImagePath(i);
            AssetDescriptor descriptor = new AssetDescriptor<Texture>(resolve(path), Texture.class);
            dependencies.add(descriptor);
        }
        return dependencies;
    }

    public static class ThaiFontParameter extends AssetLoaderParameters<ThaiFont> {
        public int horizontalOffset;
        public int verticalOffset;
        public int yoYingTrim;
        public int thoThanTrim;

        public ThaiFontParameter() {}

        public ThaiFontParameter(int horizontalOffset, int verticalOffset, int yoYingTrim, int thoThanTrim) {
            this.horizontalOffset = horizontalOffset;
            this.verticalOffset = verticalOffset;
            this.yoYingTrim = yoYingTrim;
            this.thoThanTrim = thoThanTrim;
        }
    }
}
