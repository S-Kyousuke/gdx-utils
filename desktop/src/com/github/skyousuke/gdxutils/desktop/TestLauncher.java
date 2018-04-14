package com.github.skyousuke.gdxutils.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.skyousuke.gdxutils.SimpleGame;

public class TestLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 600;
        new LwjglApplication(new SimpleGame(), config);
    }
}
