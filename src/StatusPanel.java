import bagel.DrawOptions;
import bagel.Font;
import bagel.Window;
import bagel.util.Colour;

/**
 * The status panel
 */
public class StatusPanel extends Panel {
    /* Constants */
    private static final int FONT_SIZE = 18;
    private static final int WAVE_X = 5;
    private static final int TIMESCALE_X = 200;
    private static final int Y_POS = Window.getHeight() - 8;
    private static final int X_OFFSET = 100;

    /* Attributes */
    private final Font font;

    /**
     * Creates a new status panel
     */
    public StatusPanel(){
        super("res/images/statuspanel.png");
        font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    }

    /**
     * Draws the status panel
     */
    @Override
    public void drawPanel(ShadowDefend gameInstance){
        getBackground().draw(Window.getWidth()/2, Window.getHeight() - getBackground().getHeight()/2);
        font.drawString("Wave: " + gameInstance.getGameLevel().getLevelWave().getWaveNumber(), WAVE_X, Y_POS);
        if(ShadowDefend.getTimescale() > 1){
            DrawOptions green = new DrawOptions();
            font.drawString("Timescale: " + ShadowDefend.getTimescale(), TIMESCALE_X, Y_POS, green.setBlendColour(Colour.GREEN) );
        }
        else{
            font.drawString("Timescale: " + ShadowDefend.getTimescale(), TIMESCALE_X, Y_POS);
        }
        font.drawString("Status: " + gameInstance.getGameStatus(), Window.getWidth() / 2 - X_OFFSET, Y_POS);
        font.drawString("Lives: " + gameInstance.getLives(), Window.getWidth() - X_OFFSET, Y_POS);
    }

}
