import bagel.Window;
import bagel.util.Point;

/**
 * The defences of the game
 */
public abstract class Defence extends Sprite implements Moveable{
    /* Attributes */
    private final String projectileSource;
    private final int cost;
    private final int cooldown;
    private final int damage;
    private final int radius;
    private int lastAttack;
    private Enemy currentEnemy;

    /**
     * Creates a new defence tower
     * @param point the location to place it
     * @param imageSource its image file source
     * @param projectileSource its projectile file source
     * @param cost its cost
     * @param cooldown its attacking cooldown
     * @param damage its projectile damage
     * @param radius its effective radius
     */
    public Defence(Point point, String imageSource, String projectileSource, int cost, int cooldown, int damage, int radius){
        super(point, imageSource);
        this.cost = cost;
        this.projectileSource = projectileSource;
        this.cooldown = cooldown;
        this.damage = damage;
        this.radius = radius;
        lastAttack = Integer.MAX_VALUE;
        currentEnemy = null;
    }

    /**
     * Movement of the defence. Default action is to have no movement.
     */
    public void move(){
        // Empty method
    }

    /**
     * Attacks an enemy in the level
     * @param game the instance of the game to add a projectile into
     */
    public void attack(Level game){
        if (currentEnemy != null && !currentEnemy.getCompleted() && lastAttack > cooldown){
            if (currentEnemy.getCentre().asVector().sub(getCentre().asVector()).length() <= radius){
                game.addProjectile(getCentre(), projectileSource, currentEnemy, damage);
                lastAttack = 0;
            }
        }
    }

    /**
     * Finds a target in the game instance within its radius. Each defence targets an un-targeted enemy that is
     * furthest along the path.
     * @param game the game instance
     */
    public void findTarget(Level game){
        if (currentEnemy == null || currentEnemy.getCompleted()){
            for (Enemy enemy : game.getLevelEnemy()){
                Point enemyPos = enemy.getCentre();
                if ((getCentre().asVector().sub(enemyPos.asVector())).length() <= radius && !enemy.isTargeted()){
                    currentEnemy = enemy;
                    enemy.setTargeted(true);
                    break;
                }
            }
        }
        else{
            double dx = currentEnemy.getCentre().x - getCentre().x;
            double dy = currentEnemy.getCentre().y - getCentre().y;
            setAngle(Math.atan2(dy, dx) + Math.PI/2);
            if (currentEnemy.getCentre().asVector().sub(getCentre().asVector()).length() > radius){
                currentEnemy.setTargeted(false);
                currentEnemy = null;
            }
        }
    }

    /**
     * Increases the timer for the last attack based on the timescale
     */
    public void increaseAttackCounter(){
        lastAttack += ShadowDefend.getTimescale();
    }

    /**
     * Checks if the defence tower is within the map
     * @return true if the defence tower is within the map
     */
    public boolean withinMap(){
        return getCentre().x >= 0 && getCentre().x <= Window.getWidth() &&
                getCentre().y >= 0 && getCentre().y <= Window.getHeight();
    }

    /**
     * Checks if the defence tower (airplane) has completed the path by going off the map
     * @return true if it is off the map
     */
    public boolean completedPath(){
        return getCentre().x <= Window.getWidth() || getCentre().y <= Window.getHeight();
    }

    /* Getters and setters */

    /**
     * Returns an integer representing the frames since the last attack
     * @return the number of elapsed frames
     */
    public int getLastAttack(){
        return lastAttack;
    }

    /**
     * Sets the frames since last attack to be 0
     */
    public void resetLastAttack(){
        lastAttack = 0;
    }

    /**
     * Returns an integer representing the cost of the defence
     * @return the cost of the defence
     */
    public int getCost() {
        return cost;
    }
}
