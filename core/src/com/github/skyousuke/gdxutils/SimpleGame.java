package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.Game;

public class SimpleGame extends Game {

    @Override
    public void create() {
        setScreen(new MySimpleScreen());
    }
}
