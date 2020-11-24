import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.IOException;

/**
 * A tower defense game called ShadowDefend
 * Implementation of timescale functionality used from project 1 sample solution
 */
public class ShadowDefend extends AbstractGame {
    /* Constants */
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final int DEFAULT_TIMESCALE = 1;
    private static final int STARTING_MONEY = 500;
    private static final int STARTING_LIVES = 25;
    private static final int AIR_SUPPORT_OFFSET = 100;


    /* Attributes */
    private static double timescale = DEFAULT_TIMESCALE;
    private StatusPanel statusPanel;
    private BuyPanel buyPanel;
    private Level gameLevel;
    private String gameStatus;
    private int money;
    private int lives;
    private boolean waveStarted = false;
    private boolean holdingTank = false;
    private boolean holdingSuperTank = false;
    private boolean holdingAirSupport = false;
    private boolean gameOver = false;


    /**
     * Creates a new instance of the game
     * @throws IOException when wave is not found
     */
    public ShadowDefend() throws IOException {
        super(WIDTH, HEIGHT, "ShadowDefend");
        gameLevel = new Level();
        statusPanel = new StatusPanel();
        buyPanel = new BuyPanel();
        money = STARTING_MONEY;
        lives = STARTING_LIVES;
        gameStatus = "Awaiting Start";
    }

    /**
     * Entry point for the game
     * @param args optional command line arguments
     * @throws Exception when file is not found
     */
    public static void main(String[] args) throws Exception {
        new ShadowDefend().run();
    }

    /**
     * Updates the game state 60 frames per second, does all the necessary game logic
     * @param input the input the user entered
     */
    @Override
    protected void update(Input input) {
        gameLevel.getMap().draw(0, 0, 0, 0, Window.getWidth(), Window.getHeight());

        if (input.wasPressed(Keys.S) && !waveStarted) {
            waveStarted = true;
        }

        gameLevel.renderDefences();
        buyPanel.drawPanel(this);
        // Moved this line out here to allow planes to complete its flight even if the wave ends before it finishes
        gameLevel.moveDefences();
        gameLevel.renderEnemies();
        if (waveStarted && lives > 0) {
            try {
                doWaveLogic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Instead of closing the game, gives the player an option to restart the game on death
        if (lives <= 0){
            gameOver = true;
            gameStatus = "Oh No! You have died! Press Y to restart game.";
            // Uncomment this next line if you want the window to close
//            Window.close();
        }

        // Dont allow players to purchase towers after game ended
        if (!gameOver) {
            checkHoldingDefence(input);
        }

        statusPanel.drawPanel(this);

        if (gameOver && input.wasPressed(Keys.Y)){
            try {
                restartGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (input.wasPressed(Keys.L)) {
            if (timescale < 5){
                timescale++;
            }
        }

        if (input.wasPressed(Keys.K)) {
            if (timescale > 1){
                timescale--;
            }
        }
    }

    /**
     * Performs all the wave logic
     * @throws IOException when wave file is not found
     */
    private void doWaveLogic() throws IOException{
        gameStatus = "Wave In Progress";
        gameLevel.spawnWave();
        gameLevel.moveEnemies();
        gameLevel.defenceAttack();
        gameLevel.increaseFrame();
        gameLevel.removeCompletedSprites(this);

        // Wave has ended
        if (gameLevel.checkWaveEnded()){
            gameStatus = "Awaiting Start";
            waveStarted = false;
            gameLevel.getNextWave();
            money += 150 + gameLevel.getLevelWave().getWaveNumber() * 100;
            gameLevel.resetLevelProjectiles();

            // Level has ended
            if(gameLevel.getLevelWave().allWavesFinished()){
                if(gameLevel.hasLoadedNextLevel()){
                    gameStatus = "Winner! Press Y to restart game.";
                    gameOver = true;
                }
                else{
                    gameLevel.loadNextLevel();
                    resetLevel();
                }
            }
        }
    }

    /**
     * Checks if the player is holding a defence tower and draws the indicator. Does not allow towers to be stacked or
     * placed onto the enemy path and does not draw indicator if this is the case.
     * @param input the player input
     */
    private void checkHoldingDefence(Input input){
        // Holding a defence tower
        if (holdingTank || holdingSuperTank || holdingAirSupport){
            boolean allowedToPlace = true;
            gameStatus = "Placing";

            if (holdingTank || holdingSuperTank){
                for (Defence defence : gameLevel.getLevelDefence()){
                    if (defence.getRect().intersects(input.getMousePosition())){
                        allowedToPlace = false;
                    }
                }
                if (inputWithinWindow(input) && validLocation(input) && allowedToPlace){
                    if (holdingTank){
                        buyPanel.drawTankIndicator(input.getMousePosition());
                    }
                    if (holdingSuperTank){
                        buyPanel.drawSuperTankIndicator(input.getMousePosition());
                    }
                    if (input.wasPressed(MouseButtons.LEFT)){
                        if (holdingTank){
                            gameLevel.placeDefence(new Tank(input.getMousePosition()), this);
                            holdingTank = false;
                        }
                        if (holdingSuperTank) {
                            gameLevel.placeDefence(new SuperTank(input.getMousePosition()), this);
                            holdingSuperTank = false;
                        }
                        gameStatus = "Awaiting Start";
                    }
                }
            }

            if (holdingAirSupport){
                // Allow the plane to be placed on the polyline as it does not make sense if we cant place bombs
                // on top of the enemy path
                if(inputWithinWindow(input)){
                    buyPanel.drawAirSupportIndicator(input.getMousePosition());
                    if (input.wasPressed(MouseButtons.LEFT)){
                        if (AirSupport.isNextIsHorizontal()){
                            gameLevel.placeDefence(new AirSupport(new Point(-AIR_SUPPORT_OFFSET, input.getMouseY())), this);
                        }
                        else{
                            gameLevel.placeDefence(new AirSupport(new Point(input.getMouseX(), -AIR_SUPPORT_OFFSET)), this);
                        }
                        AirSupport.setNextIsHorizontal();
                        holdingAirSupport = false;
                        gameStatus = "Awaiting Start";
                    }
                }
            }

            // De-select defences
            if (input.wasPressed(MouseButtons.RIGHT)) {
                holdingTank = false;
                holdingAirSupport = false;
                holdingSuperTank = false;
                gameStatus = "Awaiting Start";
            }
        }

        // Select defences
        if (input.wasPressed(MouseButtons.LEFT)){
            if (money >= BuyPanel.TANK_PRICE && buyPanel.getTankBound().intersects(input.getMousePosition())
                    && !holdingSuperTank && !holdingAirSupport){
                holdingTank = true;
            }

            if (money >= BuyPanel.SUPER_TANK_PRICE && buyPanel.getSuperTankBound().intersects(input.getMousePosition())
                    && !holdingTank && !holdingAirSupport){
                holdingSuperTank = true;
            }

            if (money >= BuyPanel.AIR_SUPPORT_PRICE && buyPanel.getAirSupportBound().intersects(input.getMousePosition())
                    && !holdingSuperTank && !holdingTank){
                holdingAirSupport = true;
            }

        }
    }

    /**
     * Checks if the mouse input is within the game window
     * @param input the player input
     * @return true if the mouse is within the game window
     */
    private boolean inputWithinWindow(Input input){
        Rectangle gameWindow = new Rectangle(0, 0, WIDTH, HEIGHT);
        return gameWindow.intersects(input.getMousePosition()) &&
                !buyPanel.getBackground().getBoundingBox().intersects(input.getMousePosition()) &&
                !statusPanel.getBackground().getBoundingBox().intersects(input.getMousePosition());
    }

    /**
     * Checks if a defence can be placed on the location of the mouse input on the map
     * @param input the player input
     * @return true if it can be placed on the tile
     */
    private boolean validLocation(Input input){
        return !gameLevel.getMap().hasProperty((int)input.getMouseX(), (int)input.getMouseY(), "blocked");
    }

    /**
     * Restarts the game, resets the variables
     * @throws IOException when the files for the level cannot be found
     */
    private void restartGame() throws IOException {
        gameLevel = new Level();
        statusPanel = new StatusPanel();
        buyPanel = new BuyPanel();
        gameStatus = "Awaiting Start";
        waveStarted = false;
        gameOver = false;
        resetLevel();
    }

    /**
     * Resets the level to its starting state
     */
    private void resetLevel(){
        money = STARTING_MONEY;
        lives = STARTING_LIVES;
        timescale = 1;
        if(!AirSupport.isNextIsHorizontal()){
            AirSupport.setNextIsHorizontal();
        }
    }

    /* Getters and setters */

    /**
     * Returns the timescale of the game
     * @return the timescale of the game
     */
    public static double getTimescale() {
        return timescale;
    }

    /**
     * Deducts a given amount of money
     * @param amount the amount to deduct
     */
    public void deductMoney(int amount){
        money -= amount;
    }

    /**
     * Returns the current amount of money the player has
     * @return the amount of money of the player
     */
    public int getMoney() {
        return money;
    }

    /**
     * Returns the current game level
     * @return the game level
     */
    public Level getGameLevel() {
        return gameLevel;
    }

    /**
     * Returns a string representing the game status
     * @return the game status
     */
    public String getGameStatus() {
        return gameStatus;
    }

    /**
     * Returns an integer indicating the lives the player has
     * @return the amount of lives left
     */
    public int getLives() {
        return lives;
    }

    /**
     * Deducts the a given amount of lives from the player
     * @param amount the amount of lives to deduct
     */
    public void deductLives(int amount){
        lives -= amount;
    }

    /**
     * Adds a given amount of money to the player
     * @param amount the amount of money to add
     */
    public void addMoney(int amount){
        money += amount;
    }
}
