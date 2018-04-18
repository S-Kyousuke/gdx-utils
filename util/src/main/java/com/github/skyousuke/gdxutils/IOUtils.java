package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.IntBuffer;
/**
 * Read-Write files to various format.
 * <p style="color:red">Not support WebGL Platform</p>
 */
public class IOUtils {
    private IOUtils() {
    }

    public static void writeScreenShot(FileHandle file) {
        final int backBufferWidth = Gdx.graphics.getBackBufferWidth();
        final int backBufferHeight = Gdx.graphics.getBackBufferHeight();
        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0,backBufferWidth, backBufferHeight, true);
        for (int i = 4; i < pixels.length; i += 4) {
            // set alpha component to 100%
            pixels[i - 1] = (byte) 255;
        }
        Pixmap pixmap = new Pixmap(backBufferWidth, backBufferHeight, Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        PixmapIO.writePNG(file, pixmap);
        pixmap.dispose();
    }

    public static void writeTexture(Texture texture, FileHandle file) {
        if (texture.getTextureData().getType() == TextureData.TextureDataType.Pixmap) {
            writePixmapTexture(texture, file);
        } else {
            writeCustomTexture(texture, file);
        }
    }

    private static void writePixmapTexture(Texture pixmapTexture, FileHandle file) {
        TextureData textureData = pixmapTexture.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }
        Pixmap pixmap = textureData.consumePixmap();
        PixmapIO.writePNG(file, pixmap);
        pixmap.dispose();
    }

    private static void writeCustomTexture(Texture customTexture, FileHandle file) {
        IntBuffer intBuffer = BufferUtils.newIntBuffer(16);
        Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, intBuffer);
        final int oldScreenX = intBuffer.get(0);
        final int oldScreenY = intBuffer.get(1);
        final int oldScreenWidth = intBuffer.get(2);
        final int oldScreenHeight = intBuffer.get(3);

        SpriteBatch batch = new SpriteBatch(1);
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        final int textureWidth = customTexture.getWidth();
        final int textureHeight = customTexture.getHeight();

        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, textureWidth, textureHeight, false);

        HdpiUtils.glViewport(0, 0, textureWidth, textureHeight);
        frameBuffer.bind();

        batch.getProjectionMatrix().setToOrtho2D(0, 0, textureWidth, textureHeight);
        batch.begin();
        batch.draw(customTexture, 0, textureHeight, textureWidth, -textureHeight);
        batch.end();

        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, customTexture.getWidth(), customTexture.getHeight());
        PixmapIO.writePNG(file, pixmap);

        FrameBuffer.unbind();
        pixmap.dispose();
        frameBuffer.dispose();
        batch.dispose();

        HdpiUtils.glViewport(oldScreenX, oldScreenY, oldScreenWidth, oldScreenHeight);
    }
}
