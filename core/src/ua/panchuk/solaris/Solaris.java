package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Solaris extends Game {

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
