import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A mega slicer
 */
public class MegaSlicer extends Enemy {
    /* Constants */
    private static final String imageSource = "res/images/megaslicer.png";
    public static final double SPEED = SuperSlicer.SPEED;
    public static final int PENALTY = 2 * SuperSlicer.PENALTY;
    private static final int HEALTH = 2 * SuperSlicer.HEALTH;
    private static final int REWARD = 10;

    /**
     * Creates a new mega slicer
     * @param polyline the path the mega slicer travels
     */
    public MegaSlicer(List<Point> polyline){
        super(polyline, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Creates a new mega slicer at a given point
     * @param polyline the path it walks
     * @param currPoint the point on the polyline to spawn it at
     * @param point the location to spawn it
     */
    public MegaSlicer(List<Point> polyline, int currPoint, Point point){
        super(polyline, currPoint, point, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Spawns the children of a mega slicer. Children spawns around the parents last location.
     * @param level the level to spawn its children
     * @return a list of enemies to spawn
     */
    @Override
    public List<Enemy> spawnChild(Level level) {
        List<Enemy> toSpawn = new ArrayList<>();
        toSpawn.add(new SuperSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x - CHILDREN_OFFSET, getCentre().y)));
        toSpawn.add(new SuperSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x + CHILDREN_OFFSET, getCentre().y)));
        return toSpawn;
    }
}