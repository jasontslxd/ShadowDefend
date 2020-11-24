import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * An apex slicer
 */
public class ApexSlicer extends Enemy {
    /* Constants */
    private static final String imageSource = "res/images/apexslicer.png";
    private static final double SPEED = 0.5 * MegaSlicer.SPEED;
    private static final int HEALTH = 25 * Slicer.HEALTH;
    private static final int PENALTY = 4 * MegaSlicer.PENALTY;
    private static final int REWARD = 150;

    /**
     * Creates a new apex slicer
     * @param polyline the path the apex slicer travels
     */
    public ApexSlicer(List<Point> polyline){
        super(polyline, imageSource, SPEED, HEALTH, PENALTY, REWARD);
    }

    /**
     * Spawns the children of an apex slicer. Children spawns around the parents last location.
     * @param level the level to spawn its children
     * @return a list of enemies to spawn
     */
    @Override
    public List<Enemy> spawnChild(Level level) {
        List<Enemy> toSpawn = new ArrayList<>();
        toSpawn.add(new MegaSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x - CHILDREN_OFFSET, getCentre().y)));
        toSpawn.add(new MegaSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x + CHILDREN_OFFSET, getCentre().y)));
        toSpawn.add(new MegaSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x, getCentre().y - CHILDREN_OFFSET)));
        toSpawn.add(new MegaSlicer(getPolyline(), getCurrPoint(), new Point(getCentre().x, getCentre().y + CHILDREN_OFFSET)));
        return toSpawn;
    }
}