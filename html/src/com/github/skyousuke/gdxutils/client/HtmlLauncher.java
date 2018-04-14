package com.github.skyousuke.gdxutils.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.skyousuke.gdxutils.RegionPosApp;
import com.github.skyousuke.gdxutils.batchmonitor.BatchMonitor;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1024, 576);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new BatchMonitor(new RegionPosApp());
        }
}