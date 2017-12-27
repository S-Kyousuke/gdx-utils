package com.github.skyousuke.gdxutils.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.skyousuke.gdxutils.AnimationViewer;

public class DesktopLauncher  {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 576;
        config.title = "AnimationViewer 1.1";
        config.resizable = false;
        new LwjglApplication(new AnimationViewer(), config);
    }
}
