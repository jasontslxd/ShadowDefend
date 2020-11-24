import bagel.Image;

/**
 * A panel to display game information
 */
public abstract class Panel{
    /* Attributes */
    private final Image background;

    /**
     * Creates a new background
     * @param backgroundPath path to the background image
     */
    public Panel(String backgroundPath){
        background = new Image(backgroundPath);
    }

    /**
     * Draws a panel
     */
    public abstract void drawPanel(ShadowDefend gameInstance);

    /* Getters and setters */

    /**
     * Returns an image of the background of the panel
     * @return the background image
     */
    public Image getBackground() {
        return background;
    }

}
