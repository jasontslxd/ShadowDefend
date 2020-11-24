import bagel.util.Point;

/**
 * A tank which is an active defence tower
 */
public class Tank extends Defence{
    /* Constants */
    private static final String IMAGE_SOURCE = "res/images/tank.png";
    private static final String PROJECTILE_SOURCE = "res/images/tank_projectile.png";
    private static final int COST = 250;
    private static final int COOLDOWN = 60;
    private static final int DAMAGE = 1;
    private static final int RADIUS = 100;

    /**
     * Creates a new tank at the given point
     * @param point the point to place the tank
     */
    public Tank(Point point){
        super(point, IMAGE_SOURCE, PROJECTILE_SOURCE, COST, COOLDOWN, DAMAGE, RADIUS);
    }
}
