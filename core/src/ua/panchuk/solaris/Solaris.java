package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Solaris extends Game {

    public static final String TITLE = "Solaris";
    public static final int WIDTH = 480,HEIGHT = 320; // used later to set window size

    public Music getMusic() {
        return music;
    }

    private Music music;
    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        music = Gdx.audio.newMusic(Gdx.files.internal("music/Dream about space.mp3"));
        setScreen(new MainScreen());
    }
}
