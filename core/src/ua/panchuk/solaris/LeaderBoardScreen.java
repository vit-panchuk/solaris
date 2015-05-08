package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoardScreen implements Screen {
    private ArrayList<Record> records = new ArrayList();
    private SpriteBatch spriteBatch = new SpriteBatch();
    private Stage stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
    private Table table = new Table();
    private Skin skin = new Skin(Gdx.files.internal("skin.json"), new TextureAtlas(Gdx.files.internal("pack.pack")));
    private Label noRecords = new Label("No records yet", skin);
    private ScrollPane scroll = new ScrollPane(table);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    public LeaderBoardScreen(){
        if (Gdx.files.local("records.dat").exists()) {
            FileHandle file = Gdx.files.local("records.dat");
            try {
                records = (ArrayList<Record>) deserialize(file.readBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public LeaderBoardScreen(Record record) {
        if (Gdx.files.local("records.dat").exists()) {
            FileHandle file = Gdx.files.local("records.dat");
            try {
                records = (ArrayList<Record>) deserialize(file.readBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            records.add(record);
            Collections.sort(records);
            try {
                file.writeBytes(serialize(records), false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            FileHandle file = Gdx.files.local("records.dat");
            records.add(record);
            try {
                file.writeBytes(serialize(records), false);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void show() {
        scroll.layout();
        table.top();
        Table container = new Table();
        container.setFillParent(true);
        container.pad(h*0.01f, w*0.01f, h*0.01f, w*0.01f);
        container.top();
        if (records.size() > 0) {
            Label num = new Label("#", skin);
            num.setFontScale(w*0.002f);
            Label name = new Label("Name", skin);
            name.setFontScale(w*0.002f);
            Label score = new Label("Score", skin);
            score.setFontScale(w*0.002f);
            table.add(num).expandX();
            table.add(name).expandX();
            table.add(score).expandX();
            table.row();
            Record r;
            for (int i = 0, len = records.size(); i < len; i++) {
                r = records.get(i);
                Label currNum = new Label(String.valueOf(i+1), skin);
                currNum.setFontScale(w*0.002f);
                Label currName = new Label(r.getAuthor(), skin);
                currName.setFontScale(w*0.002f);
                Label currScore = new Label(String.format("%.0f", r.getScore()), skin);
                currScore.setFontScale(w*0.002f);
                table.add(currNum).expandX();
                table.add(currName).expandX();
                table.add(currScore).expandX();
                table.row();
            }
        }
        else {
            noRecords.setFontScale(w*0.002f);
            table.add(noRecords);
        }
        container.add(scroll).colspan(3).width(w);
        stage.addActor(container);
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
        Gdx.gl.glClearColor(211f/255f, 84f/255f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
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

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
}
