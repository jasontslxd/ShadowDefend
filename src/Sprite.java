import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * A Sprite (game entity)
 * Implementation of everything from project 1 sample solution
 */
public abstract class Sprite {
    /* Attributes */
    private final Image image;
    private final Rectangle rect;
    private double angle;

    /**
     * Creates a new sprite
     * @param point the starting location
     * @param imageSource the path to the image of the desired sprite
     */
    public Sprite(Point point, String imageSource){
        image = new Image(imageSource);
        rect = image.getBoundingBoxAt(point);
        angle = 0;
    }

    /**
     * Moves the sprite
     * @param dx the amount to move the sprite by
     */
    public void move(Vector2 dx){
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    /**
     * Renders the sprite at its location
     */
    public void render(){
        image.draw(getCentre().x, getCentre().y, new DrawOptions().setRotation(angle));
    }

    /* Getters and setters */

    /**
     * Returns a rectangle indicating the bounding box of the sprite
     * @return the bounding box of the sprite
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Returns a point indicating the centre of the sprite
     * @return the centre point
     */
    public Point getCentre(){
        return getRect().centre();
    }

    /**
     * Sets the angle that the sprite faces
     * @param angle the angle of the sprite
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

}
