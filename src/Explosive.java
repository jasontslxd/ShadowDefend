import bagel.util.Point;

/**
 * An explosive dropped by an air support
 */
public class Explosive extends Projectile {
    /* Constants */
    private static final int DELAY = 120;
    private static final int RADIUS = 200;

    /* Attributes */
    private int timer;

    /**
     * Creates a new explosive
     * @param point the location to create it
     * @param imageSource its image source
     * @param damage the damage it does
     */
    public Explosive(Point point, String imageSource, int damage) {
        super(point, imageSource, null, damage);
        timer = 0;
    }

    /**
     * Controls how the explosive moves in time and when it explodes
     * @param game the level of the game
     */
    @Override
    public void move(Level game) {
        timer += ShadowDefend.getTimescale();
        if (timer >= DELAY){
            for (Enemy enemy : game.getLevelEnemy()){
                if (enemy.getCentre().asVector().sub(getCentre().asVector()).length() <= RADIUS){
                    enemy.deductHealth(getDamage());
                }
            }
            setHit();
        }
    }
}
