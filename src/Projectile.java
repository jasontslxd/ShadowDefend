import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * A projectile
 */
public class Projectile extends Sprite {
    /* Constants */
    private static final double SPEED = 10;

    /* Attributes */
    private final Enemy target;
    private final Font damageIndicator;
    private final int damage;
    private boolean hit;

    /**
     * Creates a new projectile
     * @param point the location it fires from
     * @param imageSource its image source
     * @param target its target
     * @param damage its damage
     */
    public Projectile(Point point, String imageSource, Enemy target, int damage) {
        super(point, imageSource);
        this.target = target;
        this.damage = damage;
        damageIndicator = new Font("res/fonts/DejaVuSans-Bold.ttf", 14);
        hit = false;
    }

    /**
     * Controls how the projectile moves
     * @param game the level which contains the projectile
     */
    public void move(Level game) {
        if (!hit){
            double dx = target.getCentre().x - getCentre().x;
            double dy = target.getCentre().y - getCentre().y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            double totalSpeed = SPEED * ShadowDefend.getTimescale();
            double vx = totalSpeed * dx / (distance);
            double vy = totalSpeed * dy / (distance);

            // Changed hit logic to when the centre of the projectile intersects the enemy to make it look better
            if(target.getRect().intersects(getCentre())){
                hit = true;
                target.deductHealth(damage);

            }
            // Added rudimentary damage indicators
            damageIndicator.drawString("-" + damage, target.getCentre().x, target.getCentre().y,
                    new DrawOptions().setBlendColour(Colour.BLACK));
            super.move(new Vector2(vx, vy));
        }
        if (target.getCompleted()){
            hit = true;
        }
    }

    /* Getters and setters */

    /**
     * Returns a boolean value indicating whether the projectile has completed its job
     * @return true if it has hit the target or the enemy has died
     */
    public boolean completed(){
        return hit;
    }

    /**
     * Returns an integer representing the damage of the projectile
     * @return the damage of the projectile
     */
    public int getDamage(){
        return damage;
    }

    /**
     * Sets the projectile to have completed its job
     */
    public void setHit(){
        hit = true;
    }
}
