import bagel.util.Point;
import bagel.util.Vector2;

/**
 * An air support which is a passive defence tower
 */
public class AirSupport extends Defence{
    /* Constants */
    private static final String IMAGE_SOURCE = "res/images/airsupport.png";
    private static final String PROJECTILE_SOURCE = "res/images/explosive.png";
    private static final int COST = 500;
    private static final double SPEED = 3;
    private static final int COOLDOWN = -1;
    private static final int DAMAGE = 500;
    private static final int RADIUS = 200;
    private static final int DROP_WINDOW = 60;
    private static final int MIN_DROP_TIME = 60;

    /* Attributes */
    private static boolean nextIsHorizontal = true;
    private final boolean horizontal;
    private int dropTime;

    /**
     * Creates a new air support at a given point
     * @param point the point to create the air support
     */
    public AirSupport(Point point){
        super(point, IMAGE_SOURCE, PROJECTILE_SOURCE, COST, COOLDOWN, DAMAGE, RADIUS);
        resetLastAttack();
        horizontal = nextIsHorizontal;
        dropTime = generateDropTime();
    }

    /**
     * Controls the movement and orientation of the air support.
     */
    @Override
    public void move() {
        if (horizontal) {
            super.move(Vector2.right.mul(SPEED * ShadowDefend.getTimescale()));
            setAngle(Math.PI/2);
        }
        else {
            super.move(Vector2.down.mul(SPEED * ShadowDefend.getTimescale()));
            setAngle(Math.PI);
        }
    }

    /**
     * The air support attacks by dropping an explosive at a random time interval.
     * @param game the instance of the game to add a projectile into
     */
    @Override
    public void attack(Level game) {
        if (getLastAttack() >= dropTime && withinMap()){
            game.addExplosive(getCentre(), PROJECTILE_SOURCE, DAMAGE);
            dropTime = generateDropTime();
            resetLastAttack();
        }
    }

    /**
     * The air support does not need to find a target when attacking, hence empty method.
     * @param game the game instance
     */
    @Override
    public void findTarget(Level game) {
        // dont find target
    }

    /**
     * Generates a random drop time given the interval
     * @return an integer representing the drop time
     */
    private int generateDropTime(){
        return (int) (MIN_DROP_TIME + Math.random() * DROP_WINDOW);
    }

    /**
     * Returns a boolean for whether the plane should have horizontal or vertical movement.
     * @return true if the plane should have horizontal movement
     */
    public static boolean isNextIsHorizontal() {
        return nextIsHorizontal;
    }

    /**
     * Alternates the boolean value for plane movement directions.
     */
    public static void setNextIsHorizontal(){
        nextIsHorizontal = !nextIsHorizontal;
    }
}
