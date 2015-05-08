package ua.panchuk.solaris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.Comparator;


public class GameScreen implements Screen {

    private final static float COMPLEXITY_MODIFIER = 0.022f;

    private SpriteBatch spriteBatch = new SpriteBatch();

    private Stage stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
    private Stage uiStage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), spriteBatch);
    private Group group = new Group();

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), new TextureAtlas(Gdx.files.internal("pack.pack")));
    private Hero hero = new Hero(skin.getRegion("hero"), 100, 100);

    private OrthographicCamera cam;

    private Table table = new Table();
    private Label heroMass = new Label("Mass: ", skin);
    private Label heroMaxMass = new Label("Score: ", skin);

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    private float maxMass = hero.getMass();
    private int matterCount = 0;

    @Override
    public void show() {
        cam = new OrthographicCamera(w * hero.getWidth() / 100f, h * hero.getHeight() / 100f);
        cam.position.set(hero.getX(), hero.getY(), 0);
        stage.getViewport().setCamera(cam);

        setMatter(50);

        group.addActor(hero);

        stage.addActor(group);

        heroMass.setText("Mass: " + String.format("%.0f", hero.getMass()));
        heroMass.setFontScale(w*0.002f);
        heroMass.setZIndex(10000);
        heroMass.setText("Score: " + String.format("%.0f", maxMass - 100));
        heroMaxMass.setFontScale(w*0.002f);
        heroMaxMass.setZIndex(10000);
        table.setFillParent(true);
        table.top();
        table.pad(h*0.01f, w*0.01f, h*0.01f, w*0.01f);
        table.add(heroMass).expandX().left();
        table.add(heroMaxMass).expandX().right();
        uiStage.addActor(table);

        stage.addListener(new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                group.addActor(hero.move(new Vector2(x - hero.getX(Align.center), y - hero.getY(Align.center)), skin.getRegion("hero")));
                return true;
            }

        });

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
        Gdx.gl.glClearColor(40f/255f, 40f/255f, 40f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setColor(1,1,1,1);

        spriteBatch.begin();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                TextureRegion bg = skin.getRegion("bg");
                spriteBatch.draw(bg, i * 1000, j * 1000);
            }
        }
        spriteBatch.end();

        Array<Actor> actors = group.getChildren();

        actors.sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor lhs, Actor rhs) {
                return (int) ((((Matter) rhs).getMass() - ((Matter) lhs).getMass()) * 1000);
            }

            @Override
            public boolean equals(Object object) {
                return false;
            }
        });
        int len = actors.size;
        for (int i = 0; i < len; i++) {
            if (actors.get(i) instanceof Matter) {
                for (int j = i; j < len; j++) {
                    if ((actors.get(j) instanceof Matter) && (i != j)) {
                        if (((Matter) actors.get(i)).getMass() > ((Matter) actors.get(j)).getMass() && ((Matter) actors.get(i)).isCollision((Matter) actors.get(j))) {
                            ((Matter) actors.get(i)).eat((Matter) actors.get(j));
                        }
                    }
                }
            }
        }

        for (Actor actor : actors) {
            if ((((Matter) actor).getMass() <= 0) && (((Matter) actor).getMass() <= hero.getMass() * 0.01) && (Vector2.dst(actor.getX(Align.center), actor.getY(Align.center), hero.getX(Align.center), hero.getY(Align.center)) >= hero.getMass() * 50)) {
                actors.removeValue(actor, true);
                hero.ateMatterNumber+=10;
                matterCount--;
                if (matterCount <= 48 && hero.getMass() > 10) {
                    setMatter(50-matterCount);
                }
            }
        }

        if (hero.getMass() < 10) {
            ((Game)Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(maxMass-100));
        }

        stage.act(delta);
        uiStage.act(delta);

        heroMass.setText("Mass: " + String.format("%.0f", hero.getMass()));
        heroMaxMass.setText("Score: " + String.format("%.0f", maxMass-100));
        if (hero.getMass() > maxMass) {
            maxMass = hero.getMass();
        }

        cam.position.set(hero.getX(Align.center), hero.getY(Align.center), 0);
        cam.viewportWidth = w * hero.getHeight() / 100f;
        cam.viewportHeight = h * hero.getWidth() / 100f;
        cam.update();

        stage.draw();
        uiStage.draw();
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
        stage.dispose();
        uiStage.dispose();
        skin.dispose();

    }

    private float getRandom(float coord, float mass, float ateMatterNumber) {
        float max = coord + (20*COMPLEXITY_MODIFIER*ateMatterNumber) * mass;
        float min = coord - (20*COMPLEXITY_MODIFIER*ateMatterNumber) * mass;
        float range = (max - min) + 1;

        return (float) (Math.random() * range) + min;
    }

    private Matter addEnemy(float minMass, float maxMass) {
        boolean overlap;
        Array<Actor> actors = group.getChildren();
        int len = actors.size;
        Matter matter;
        int count = 0;
        do {
            count++;
            float range = (maxMass* hero.getMass() - minMass * hero.getMass()) + 1;
            matter = new Matter(skin.getRegion("hero"), (float) (Math.random() * range + hero.getMass()* minMass), new Vector2(0, 0), getRandom(hero.getX(Align.center), hero.getMass(), hero.getAteMatterNumber()), getRandom(hero.getY(Align.center), hero.getMass(), hero.getAteMatterNumber()));
            overlap = false;
            if (!matter.isCollision(hero) && Vector2.dst(hero.getX(Align.center), hero.getY(Align.center), matter.getX(Align.center), matter.getY(Align.center)) > 2*hero.getMass()) {
                for (int i = 0; i < len; i++) {
                    if (((Matter) actors.get(i)).isCollision(matter)) {
                        overlap = true;
                        break;
                    }
                }
            } else {
                overlap = true;
            }
        } while(overlap || count < 20);
        return matter;
    }

    private void setMatter(int count) {
        matterCount += count;
        for (int i = 0; i < count/2; i++) {
            group.addActor(addEnemy((COMPLEXITY_MODIFIER*hero.ateMatterNumber),(COMPLEXITY_MODIFIER*hero.ateMatterNumber)/0.05f));
        }
        for (int i = 0; i < count/2; i++) {
            group.addActor(addEnemy((COMPLEXITY_MODIFIER*hero.ateMatterNumber)/1.2f,(COMPLEXITY_MODIFIER*hero.ateMatterNumber)));
        }
    }
}
