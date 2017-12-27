package com.github.skyousuke.gdxutils.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.skyousuke.gdxutils.RegionPosApp;

public class TestLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 576;
        new LwjglApplication(new RegionPosApp(), config);
    }
}
