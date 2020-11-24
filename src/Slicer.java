import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A slicer
 */
public class Slicer extends Enemy {
    /* Constants */
    private static final String imageSource = "res/images/slicer.png";
    public static final double SPEED = 2;
    public static final int HEALTH = 1;
    public static final int PENALTY = 1;
    private static final int REWARD = 2;

    /**
     * Creates a new slicer
     * @param polyline the path it travels
     */
    public Slicer(List<Point> polyline){
        super(polyline, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Creates a new slicer at a location
     * @param polyline the path it travels
     * @param currPoint the point on polyline to spawn it
     * @param point the location to spawn it
     */
    public Slicer(List<Point> polyline, int currPoint, Point point){
        super(polyline, currPoint, point, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Spawns no children since slicers are the most basic enemy
     * @param level the level to spawn the children
     * @return an empty list
     */
    @Override
    public List<Enemy> spawnChild(Level level) {
        return new ArrayList<>();
    }
}
