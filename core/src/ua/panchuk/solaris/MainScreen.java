package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainScreen implements Screen {
    private Stage stage = new Stage();
    private Table table = new Table();

    private SpriteBatch spriteBatch = new SpriteBatch();

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), new TextureAtlas(Gdx.files.internal("pack.pack")));

    private TextButton buttonPlay = new TextButton("Play", skin),
            buttonExit = new TextButton("Exit", skin),
            buttonRecords = new TextButton("Records", skin);
    private Label musicLabel = new Label("Music", skin);
    private CheckBox musicCheckBox = new CheckBox("", skin);
    private Label soundsLabel = new Label("Sounds", skin);
    private CheckBox soundsCheckBox = new CheckBox("", skin);

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(142f/255f, 68f/255f, 173f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                TextureRegion bg = skin.getRegion("bg");
                spriteBatch.draw(bg, i*1000, j*1000);
            }
        }
        spriteBatch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Settings.load();
        musicCheckBox.setChecked(Settings.isMusic());
        soundsCheckBox.setChecked(Settings.isSounds());
        toggleMusic();

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                // or System.exit(0);
            }
        });

        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        buttonRecords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LeaderBoardScreen());
            }
        });

        musicCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleMusic();
                Settings.setMusic(musicCheckBox.isChecked());
                Settings.save();
            }
        });

        soundsCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.setSounds(soundsCheckBox.isChecked());
                Settings.save();
            }
        });


        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        table.add(new Image(skin.getDrawable("logo"))).colspan(2).size(w*0.3f, w*0.3f/2f).row();
        table.add(buttonPlay).colspan(2).padBottom(20).row();
        table.add(buttonRecords).colspan(2).padBottom(20).row();
        table.add(musicLabel).width(table.getWidth()/2).padBottom(20);
        table.add(musicCheckBox).padBottom(20).row();
        table.add(soundsLabel).width(table.getWidth()/2).padBottom(20);
        table.add(soundsCheckBox).padBottom(20).row();
        table.add(buttonExit).colspan(2).center().padBottom(20).row();
        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void toggleMusic() {
        if (musicCheckBox.isChecked()) {
            ((Solaris) Gdx.app.getApplicationListener()).getMusic().play();
        } else {
            ((Solaris) Gdx.app.getApplicationListener()).getMusic().pause();
        }
    }
}