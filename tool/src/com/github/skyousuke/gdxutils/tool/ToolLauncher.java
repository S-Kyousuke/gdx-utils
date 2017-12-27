package com.github.skyousuke.gdxutils.tool;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.github.skyousuke.gdxutils.AnimatedRegion;
import com.github.skyousuke.gdxutils.font.ThaiFont;
import objectexplorer.MemoryMeasurer;
import org.mockito.Mockito;

public class ToolLauncher {

    private static final Object[] objectsToFindSize = new Object[]{
            Mockito.mock(AnimatedRegion.class),
            Mockito.mock(ThaiFont.class),
            Mockito.mock(BitmapFont.class)
    };

    public static void main(String[] args) {
        Application application = new HeadlessApplication(new ApplicationAdapter() {});
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
        for (Object object : objectsToFindSize) {
            System.out.println(object.getClass().getCanonicalName() + " size: "
                    + MemoryMeasurer.measureBytes(object) + " bytes");
        }
        application.exit();
    }

}