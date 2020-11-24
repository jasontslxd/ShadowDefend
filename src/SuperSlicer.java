import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A super slicer
 */
public class SuperSlicer extends Enemy {
    /* Constants */
    private static final String imageSource = "res/images/superslicer.png";
    public static final double SPEED = 0.75 * Slicer.SPEED;
    public static final int HEALTH = Slicer.HEALTH;
    public static final int PENALTY = 2 * Slicer.PENALTY;
    private static final int REWARD = 15;

    /**
     * Creates a new super slicer
     * @param polyline the path the super slicer travels
     */
    public SuperSlicer(List<Point> polyline){
        super(polyline, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Creates a new super slicer at a given point
     * @param polyline the path it travels
     * @param currPoint the point on the polyline to spawn it
     * @param point the location to spawn it
     */
    public SuperSlicer(List<Point> polyline, int currPoint, Point point){
        super(polyline, currPoint, point, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Spawns the children of a super slicer. Children spawns around the parents last location.
     * @param level the level to spawn the children
     * @return a list of children to spawn
     */
    @Override
    public List<Enemy> spawnChild(Level level) {
        List<Enemy> toSpawn = new ArrayList<>();
        toSpawn.add(new Slicer(getPolyline(), getCurrPoint(), new Point(getCentre().x - CHILDREN_OFFSET, getCentre().y)));
        toSpawn.add(new Slicer(getPolyline(), getCurrPoint(), new Point(getCentre().x + CHILDREN_OFFSET, getCentre().y)));
        return toSpawn;
    }
}