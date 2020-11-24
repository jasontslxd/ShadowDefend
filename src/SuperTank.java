import bagel.util.Point;

/**
 * A super tank
 */
public class SuperTank extends Defence{
    /* Constants */
    private static final String IMAGE_SOURCE = "res/images/supertank.png";
    private static final String PROJECTILE_SOURCE = "res/images/supertank_projectile.png";
    private static final int COST = 600;
    private static final int COOLDOWN = 30;
    private static final int DAMAGE = 3;
    private static final int RADIUS = 150;

    /**
     * Creates a new super tank
     * @param point the location to spawn the super tank
     */
    public SuperTank(Point point){
        super(point, IMAGE_SOURCE, PROJECTILE_SOURCE, COST, COOLDOWN, DAMAGE, RADIUS);
    }
}
