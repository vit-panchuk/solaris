package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameOverScreen implements Screen {

    private SpriteBatch spriteBatch = new SpriteBatch();
    private Stage stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
    private Skin skin = new Skin(Gdx.files.internal("skin.json"), new TextureAtlas(Gdx.files.internal("pack.pack")));
    private Label gameOverLabel = new Label("Game Over :'(", skin);
    private Label scoreLabel;
    private TextButton restartButton = new TextButton("Restart", skin);
    private TextButton leaderBoardButton = new TextButton("Records", skin);
    private Table table = new Table();
    private float score;

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    public GameOverScreen(float score) {
        this.score = score;
        scoreLabel = new Label("Your score: " + String.format("%.0f", score), skin);
    }

    @Override
    public void show() {
        gameOverLabel.setFontScale(w*0.004f);
        table.setFillParent(true);
        table.add(gameOverLabel).colspan(2).padBottom(h*0.05f).row();
        scoreLabel.setFontScale(w * 0.002f);
        table.add(scoreLabel).colspan(2).padBottom(h*0.05f).row();
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });
        leaderBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NameEnterScreen(score));
            }
        });
        table.add(restartButton);
        table.add(leaderBoardButton);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ){
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new MainScreen());
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(41f/255f, 128f/255f, 185f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        dispose();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
