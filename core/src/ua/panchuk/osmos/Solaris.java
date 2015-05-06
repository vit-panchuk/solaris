package ua.panchuk.osmos;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Solaris extends Game {

    public static final String TITLE = "Solaris";
    public static final int WIDTH = 480,HEIGHT = 320; // used later to set window size
    public Music music;
    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        Music music = Gdx.audio.newMusic(Gdx.files.internal("music/Dream about space.mp3"));
        music.play();
        setScreen(new MainScreen());
    }
}
