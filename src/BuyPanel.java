import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The buy panel
 */
public class BuyPanel extends Panel{
    /* Constants */
    public static final int TANK_PRICE = 250;
    public static final int SUPER_TANK_PRICE = 600;
    public static final int AIR_SUPPORT_PRICE = 500;
    private static final int KEY_BIND_FONT_SIZE = 14;
    private static final int MONEY_FONT_SIZE = 50;
    private static final int PRICE_FONT_SIZE = 20;
    private static final int TANK_X = 64;
    private static final int SUPER_TANK_X = TANK_X + 120;
    private static final int AIR_SUPPORT_X = SUPER_TANK_X + 120;
    private static final int MONEY_X = Window.getWidth() - 200;
    private static final int MONEY_Y = 65;
    private static final int DEFENCE_MONEY_OFFSET = 10;
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";

    /* Attributes */
    private final int yPos = (int) getBackground().getHeight()/2 - 10;
    private final double textY = getBackground().getHeight() - 10;
    private final Font keyBindFont;
    private final Font moneyFont;
    private final Font priceFont;
    private final Image tank;
    private final Image superTank;
    private final Image airSupport;
    private final DrawOptions tankPriceColour;
    private final DrawOptions superTankPriceColour;
    private final DrawOptions airSupportPriceColour;

    /**
     * Creates a new buy panel
     */
    public BuyPanel(){
        super("res/images/buypanel.png");
        keyBindFont = new Font(FONT_FILE, KEY_BIND_FONT_SIZE);
        moneyFont = new Font(FONT_FILE, MONEY_FONT_SIZE);
        priceFont = new Font(FONT_FILE, PRICE_FONT_SIZE);
        tank = new Image("res/images/tank.png");
        superTank = new Image("res/images/supertank.png");
        airSupport = new Image("res/images/airsupport.png");
        tankPriceColour = new DrawOptions().setBlendColour(Colour.RED);
        superTankPriceColour = new DrawOptions().setBlendColour(Colour.RED);
        airSupportPriceColour = new DrawOptions().setBlendColour(Colour.RED);


    }

    /**
     * Draws the buy panel
     */
    @Override
    public void drawPanel(ShadowDefend gameInstance){
        getBackground().draw( Window.getWidth()/2, getBackground().getHeight()/2);
        drawKeyBindings();
        moneyFont.drawString("$" + gameInstance.getMoney(), MONEY_X, MONEY_Y);
        drawDefences(gameInstance.getMoney());
    }

    /**
     * Draws the key bindings
     */
    private void drawKeyBindings(){
        int leftAlign = (int) getBackground().getWidth()/2 - 50;
        keyBindFont.drawString("Key Binds:", leftAlign, getBackground().getHeight()*2/8);
        keyBindFont.drawString("S - Start Wave", leftAlign, getBackground().getHeight()*4/8);
        keyBindFont.drawString("L - Increase Timescale", leftAlign, getBackground().getHeight()*5/8);
        keyBindFont.drawString("K - Decrease Timescale", leftAlign, getBackground().getHeight()*6/8);
    }

    /**
     * Draws the defences icons and their money icons based on how much the player has
     */
    private void drawDefences(int money){
        if (money <= TANK_PRICE){
            tankPriceColour.setBlendColour(Colour.RED);
            superTankPriceColour.setBlendColour(Colour.RED);
            airSupportPriceColour.setBlendColour(Colour.RED);
        }
        if (money >= TANK_PRICE && money < AIR_SUPPORT_PRICE){
            tankPriceColour.setBlendColour(Colour.GREEN);
            superTankPriceColour.setBlendColour(Colour.RED);
            airSupportPriceColour.setBlendColour(Colour.RED);
        }
        if (money >= AIR_SUPPORT_PRICE && money < SUPER_TANK_PRICE){
            tankPriceColour.setBlendColour(Colour.GREEN);
            superTankPriceColour.setBlendColour(Colour.RED);
            airSupportPriceColour.setBlendColour(Colour.GREEN);
        }
        if (money >= SUPER_TANK_PRICE){
            tankPriceColour.setBlendColour(Colour.GREEN);
            superTankPriceColour.setBlendColour(Colour.GREEN);
            airSupportPriceColour.setBlendColour(Colour.GREEN);
        }
        tank.draw(TANK_X, yPos);
        priceFont.drawString(String.format("$%d", TANK_PRICE), TANK_X - tank.getWidth()/2 + DEFENCE_MONEY_OFFSET,
                textY, tankPriceColour);
        superTank.draw(SUPER_TANK_X, yPos);
        priceFont.drawString(String.format("$%d", SUPER_TANK_PRICE), SUPER_TANK_X - superTank.getWidth()/2 +
                DEFENCE_MONEY_OFFSET, textY, superTankPriceColour);
        airSupport.draw(AIR_SUPPORT_X, yPos);
        priceFont.drawString(String.format("$%d", AIR_SUPPORT_PRICE), AIR_SUPPORT_X - airSupport.getWidth()/2 +
                DEFENCE_MONEY_OFFSET, textY, airSupportPriceColour);
    }

    /**
     * Draws the tank indicator at the given point
     * @param point the given point
     */
    public void drawTankIndicator(Point point){
        tank.draw(point.x, point.y);
    }

    /**
     * Draws the super tank indicator at the given point
     * @param point the given point
     */
    public void drawSuperTankIndicator(Point point){
        superTank.draw(point.x, point.y);
    }

    /**
     * Draws the air support indicator at the given point
     * @param point the given point
     */
    public void drawAirSupportIndicator(Point point){
        if (AirSupport.isNextIsHorizontal()){
            airSupport.draw(point.x, point.y, new DrawOptions().setRotation(Math.PI/2));
        }
        else{
            airSupport.draw(point.x, point.y, new DrawOptions().setRotation(Math.PI));
        }
    }

    /* Getters and setters */

    /**
     * Returns a rectangle indicating the bound of the tank icon
     * @return the bound of the tank icon
     */
    public Rectangle getTankBound(){
        return tank.getBoundingBoxAt(new Point(TANK_X, yPos));
    }

    /**
     * Returns a rectangle indicating the bound of the super tank icon
     * @return the bound of the super tank icon
     */
    public Rectangle getSuperTankBound(){
        return superTank.getBoundingBoxAt(new Point(SUPER_TANK_X, yPos));
    }

    /**
     * Returns a rectangle indicating the bound of the air support icon
     * @return the bound of the air support icon
     */
    public Rectangle getAirSupportBound(){
        return airSupport.getBoundingBoxAt(new Point(AIR_SUPPORT_X, yPos));
    }
}
