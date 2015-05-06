package ua.panchuk.osmos;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Matter extends Actor {
    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        setWidth(mass);
        setHeight(mass);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    protected float mass;
    protected Vector2 velocity;
    protected Sprite sprite;

    public Matter(TextureRegion textureRegion, float mass, Vector2 velocity, float x, float y) {
        sprite = new Sprite(textureRegion);
        setColor((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
        setWidth(mass);
        setHeight(mass);
        setX(x);
        setY(y);
        this.mass = mass;
        this.velocity = velocity;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(sprite, getX(), getY(), mass, mass);
    }

    @Override
    public void act(float delta) {
        float x = getX(), y = getY();
        setWidth(mass);
        setHeight(mass);
        x -= getX();
        y -= getY();
        moveBy(velocity.x/mass-x, velocity.y/mass-y);
    }

    public boolean eat(Matter target) {
        if (mass > target.getMass()) {
            float diff = massDiff(target);
            float colorPart = diff/mass;
            blendColor(target.getColor(), colorPart);
            Vector2 v = velocity.cpy().setLength(diff/target.getMass());
            Vector2 v2 = target.getVelocity().cpy().setLength(diff/mass);
            target.getVelocity().add(v);
            velocity.add(v2);
            if (target.getMass() - diff > 0) {
                mass += diff;
                target.setMass(target.getMass() - diff);
            } else {
                mass += target.getMass();
                target.setMass(0);
                return true;
            }
        }
        return false;
    }

    public boolean isCollision(Matter target) {
        return Vector2.dst(getX(Align.center), getY(Align.center), target.getX(Align.center), target.getY(Align.center)) <= (mass+target.getMass())/2;
    }

    public float massDiff(Matter target) {
        return ((mass+target.getMass())/2 - Vector2.dst(getX(Align.center), getY(Align.center), target.getX(Align.center), target.getY(Align.center)))/2;
    }

    private float blendColorLevel(float level, float targetLevel, float persent) {
        return level * (1 - persent) + targetLevel * persent;
    }

    private void blendColor(Color c, float persent) {
        Color color = getColor();
        setColor(blendColorLevel(color.r, c.r, persent), blendColorLevel(color.g, c.g, persent), blendColorLevel(color.b, c.b, persent), 1);
    }
}
