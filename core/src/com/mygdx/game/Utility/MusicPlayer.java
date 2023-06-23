package com.mygdx.game.Utility;

import com.badlogic.gdx.audio.Music;

public class MusicPlayer {
    public Music muzika;

    private static MusicPlayer singleton = new MusicPlayer();

    private MusicPlayer() {
    }

    public static MusicPlayer vratInstanci() {
        return singleton;
    }
}
