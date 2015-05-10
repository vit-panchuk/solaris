package ua.panchuk.solaris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Hero extends Matter {

    public int ateMatterNumber = 25;

    private Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/bubble.mp3"));


    public Hero(TextureRegion textureRegion, float x, float y) {
        super(textureRegion, 100, new Vector2(0, 0), x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public Matter move(Vector2 direction, TextureRegion textureRegion) {
        float m = mass*0.05f;
        float v = mass*0.005f*mass;
        mass -= m;
        Vector2 vec = direction.cpy();
        velocity.add(vec.scl(-1).setLength(v));
        Vector2 radius = direction.cpy().setLength((mass+m)/2f*1.1f);
        v *= 0.05f;
        if (Settings.isSounds()) {
            sound.play();
        }
        return new Matter(textureRegion, m, direction.setLength(v), getX(Align.center)+radius.x-m, getY(Align.center)+radius.y-m);
    }

    public int getAteMatterNumber() {
        return ateMatterNumber;
    }

    public void setAteMatterNumber(int ateMatterNumber) {
        this.ateMatterNumber = ateMatterNumber;
    }
}
