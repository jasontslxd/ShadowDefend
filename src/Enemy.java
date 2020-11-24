import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;
import java.lang.Math;

/**
 * An enemy of the game
 * Two lines of the move method are from project 1 sample solution
 */
public abstract class Enemy extends Sprite implements Moveable{
    /* Constants */
    private static final int START_OF_POLYLINE = 0;
    private static final int ACCEPTABLE_DISTANCE = 1;
    public static final int CHILDREN_OFFSET = 20;
    private static final double SPEED_OFFSET = 1; // Added to facilitate slicer movement timings

    /* Attributes */
    private final List<Point> polyline;
    private final double speed;
    private final int penalty;
    private final int reward;
    private int health;
    private int currPoint;
    private boolean completedMap;
    private boolean targeted;

    /**
     * Creates a new enemy at the start of the path
     * @param polyline the path it walks
     * @param imageSource the source image
     * @param speed its speed
     * @param health its health
     * @param penalty its penalty
     * @param reward its reward
     */
    public Enemy(List<Point> polyline, String imageSource, double speed, int health, int penalty, int reward){
        super(polyline.get(0), imageSource);
        this.polyline = polyline;
        currPoint = START_OF_POLYLINE;
        completedMap = false;
        this.speed = speed;
        this.health = health;
        this.penalty = penalty;
        this.reward = reward;
        targeted = false;
    }

    /**
     * Creates a new enemy at the given point of the path
     * @param polyline the path it walks
     * @param currPoint the point that the enemy starts at
     * @param location the location that the enemy spawns at
     * @param imageSource the source image
     * @param speed its speed
     * @param health its health
     * @param penalty its penalty
     * @param reward its reward
     */
    public Enemy(List<Point> polyline, int currPoint, Point location, String imageSource, double speed, int health, int penalty, int reward){
        super(location, imageSource);
        this.polyline = polyline;
        this.currPoint = currPoint;
        completedMap = false;
        this.speed = speed;
        this.health = health;
        this.penalty = penalty;
        this.reward = reward;
    }

    /**
     * Calculates the amount the enemy has to move per frame to the next point and updates the location of the sprite.
     */
    @Override
    public void move(){
        int nextPoint = currPoint + 1;
        if (nextPoint < polyline.size()) {
            double dx = polyline.get(nextPoint).x - getCentre().x;
            double dy = polyline.get(nextPoint).y - getCentre().y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            double totalSpeed = SPEED_OFFSET * speed * ShadowDefend.getTimescale();
            double vx = totalSpeed * dx / (distance);
            double vy = totalSpeed * dy / (distance);

            // If the enemy is within an acceptable distance to the next point, it has reached the next point
            if (Math.abs((int)dx)/(totalSpeed) <= ACCEPTABLE_DISTANCE &&
                    Math.abs((int)dy)/(totalSpeed) <= ACCEPTABLE_DISTANCE) {
                currPoint++;
            }

            // Implementation of these two lines are from sample project 1 solution
            super.move(new Vector2(vx, vy));
            setAngle(Math.atan2(dy, dx));
        }

        else{
            completedMap = true;
        }
    }

    /**
     * Gets a list of children enemies an enemy spawns
     * @param level the level to spawn the children
     * @return the list of enemies
     */
    public abstract List<Enemy> spawnChild(Level level);

    /* Getters and setters */

    /**
     * Returns a boolean value to check if the entire polyline has been traversed
     * @return true if the enemy has traversed the entire polyline
     */
    public boolean getCompletedMap(){
        return completedMap;
    }

    /**
     * Returns a boolean value to check if the enemy has been killed
     * @return true if the enemy has died
     */
    public boolean isKilled(){
        return health <= 0;
    }

    /**
     * Deducts a given amount of health from the enemy
     * @param damage the damage taken
     */
    public void deductHealth(int damage){
        health -= damage;
    }

    /**
     * Returns an integer representing the reward the player gets
     * @return the reward amount
     */
    public int getReward() {
        return reward;
    }

    /**
     * Returns an integer representing the penalty the player takes
     * @return the penalty amount
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * Returns a boolean value indicating whether the enemy has either died or traversed the polyline.
     * @return true if it has completed its mission
     */
    public boolean getCompleted(){
        return completedMap || health <= 0;
    }

    /**
     * Returns a list of points representing the polyline
     * @return the polyline
     */
    public List<Point> getPolyline() {
        return polyline;
    }

    /**
     * Returns an integer representing where the point on the polyline the enemy got up to
     * @return the point on the polyline
     */
    public int getCurrPoint() {
        return currPoint;
    }

    /**
     * Returns a boolean value indicating whether the enemy is currently being targeted
     * @return
     */
    public boolean isTargeted() {
        return targeted;
    }

    /**
     * Sets the attribute indicating whether the enemy is being targeted
     * @param targeted whether the enemy is being currently targeted
     */
    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }
}
