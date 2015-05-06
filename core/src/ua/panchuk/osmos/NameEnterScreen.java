package ua.panchuk.osmos;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class NameEnterScreen implements Screen {
    private Record record;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private Stage stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
    private Table table = new Table();
    private Skin skin = new Skin(Gdx.files.internal("skin.json"), new TextureAtlas(Gdx.files.internal("pack.pack")));
    private Label nameLabel = new Label("Enter your name", skin);
    private TextField nameField = new TextField("", skin);
    private TextButton recordButton = new TextButton("Save", skin);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    public NameEnterScreen(float score) {
        this.record = new Record("",score);
    }

    @Override
    public void show() {
        table.setFillParent(true);

        nameLabel.setFontScale(w*0.002f);
        table.add(nameLabel).padBottom(h*0.05f).row();
        table.add(nameField).padBottom(h*0.05f).row();

        recordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                record.setAuthor(nameField.getText());
                Gdx.app.log("record", record.getAuthor());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LeaderBoardScreen(record));
            }
        });

        table.add(recordButton);
        stage.addActor(table);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ){
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(record.getScore()));
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(46f/255f, 204f/255f, 113f/255f, 1);
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
