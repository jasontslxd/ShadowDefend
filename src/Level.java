import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The levels of the game
 */
public class Level{
    /* Constants */
    private static final double SPEED_OFFSET = 1; // Added to facilitate enemy spawning timings
    private static final String FIRST_LEVEL = "res/levels/1.tmx";
    private static final String SECOND_LEVEL = "res/levels/2.tmx";
    private static final String WAVES = "res/levels/waves.txt";

    /* Attributes */
    private final List<Enemy> levelEnemy = new ArrayList<>();
    private final List<Defence> levelDefence = new ArrayList<>();
    private final List<Projectile> levelProjectiles = new ArrayList<>();
    private List<Point> polyline;
    private Wave levelWave;
    private TiledMap map;
    private int spawnFrameCounter;
    private int spawnedEnemies;
    private int phaseFrameCounter;
    private boolean loadedNextLevel;

    /**
     * Loads the map and the wave information into the game
     * @throws IOException when file not found
     */
    public Level() throws IOException {
        map = new TiledMap(FIRST_LEVEL);
        polyline = map.getAllPolylines().get(0);
        levelWave = new Wave(WAVES);
        loadedNextLevel = false;
        resetLevel();
    }

    /**
     * Keeps count of the elapsed frames since last enemy spawn and wave phase
     */
    public void increaseFrame(){
        spawnFrameCounter += ShadowDefend.getTimescale();
        phaseFrameCounter += ShadowDefend.getTimescale();
        for (Defence object : levelDefence){
            object.increaseAttackCounter();
        }
    }

    /**
     * Moves all enemies in the level
     */
    public void moveEnemies(){
        for (Enemy enemy : levelEnemy) {
            enemy.move();
        }
    }

    /**
     * Moves all defences in the level
     */
    public void moveDefences(){
        for (Defence structure : levelDefence){
            structure.move();
        }
        for (Projectile projectile : levelProjectiles){
            projectile.move(this);
        }
    }

    /**
     * Spawns the wave of enemies with the given delay specified in the wave file
     */
    public void spawnWave(){
        String currEnemyType = levelWave.getEnemyType();
        // Spawns an enemy if current wave action is spawn and it is time to spawn next enemy
        if (levelWave.getAction().equals("spawn") && spawnFrameCounter >= (levelWave.getActionDelay() * SPEED_OFFSET)
                && spawnedEnemies < levelWave.getNumberToSpawn()){
            switch (currEnemyType){
                case "slicer":
                    levelEnemy.add(new Slicer(polyline));
                    break;
                case "megaslicer":
                    levelEnemy.add(new MegaSlicer(polyline));
                    break;
                case "superslicer":
                    levelEnemy.add(new SuperSlicer(polyline));
                    break;
                case "apexslicer":
                    levelEnemy.add(new ApexSlicer(polyline));
                    break;
            }
            spawnedEnemies++;
            spawnFrameCounter = 0;

        }

        // Resets the current level and gets the next wave action if available when current wave action has ended
        if (spawnedEnemies == levelWave.getNumberToSpawn() && spawnFrameCounter >= levelWave.getActionDelay()){
            levelWave.processWave();
            resetLevel();
        }

        // Resets the wave if the current wave action is delay and the delay is over
        if (levelWave.getAction().equals("delay") && phaseFrameCounter >= levelWave.getActionDelay()){
            levelWave.processWave();
            resetLevel();
        }

    }

    /**
     * Renders all active sprites in the level
     */
    public void renderEnemies(){
        for (Enemy enemy : levelEnemy){
            enemy.render();
        }
    }

    /**
     * Renders all active defences and projectiles in the level
     */
    public void renderDefences(){
        for (Defence item : levelDefence){
            item.render();
        }
        for (Projectile projectile : levelProjectiles){
            projectile.render();
        }
    }

    /**
     * Checks if there are no more wave actions left and all enemies have been spawned and completed
     * @return true if the wave has ended
     */
    public boolean checkWaveEnded(){
        return spawnedEnemies == levelWave.getNumberToSpawn() && levelEnemy.isEmpty()
                && levelWave.getWaveInProgress().isEmpty();
    }

    /**
     * Removes completed sprites, spawns child slicers if an enemy has died
     */
    public void removeCompletedSprites(ShadowDefend gameInstance){
        List<Enemy> toRemove = new ArrayList<>();
        List<Enemy> toSpawn = new ArrayList<>();
        for (Enemy enemy : levelEnemy){
            if (enemy.getCompletedMap()){
                gameInstance.deductLives(enemy.getPenalty());
                toRemove.add(enemy);
            }
            if (enemy.isKilled()){
                gameInstance.addMoney(enemy.getReward());
                toSpawn.addAll(enemy.spawnChild(this));
                toRemove.add(enemy);
            }
        }
        levelEnemy.removeAll(toRemove);
        levelEnemy.addAll(toSpawn);
        levelProjectiles.removeIf(Projectile::completed);
        List<Defence> toBeRemoved = new ArrayList<>();
        for(Defence tower : levelDefence){
            if (!tower.completedPath()){
                toBeRemoved.add(tower);
            }
        }
        levelDefence.removeAll(toBeRemoved);
    }

    /**
     * Loads the next wave into the level and resets the level
     */
    public void getNextWave(){
        levelWave.parseWave();
        resetLevel();
    }

    /**
     * Sets all variables to their default value
     */
    public void resetLevel(){
        spawnFrameCounter = Integer.MAX_VALUE;
        phaseFrameCounter = 0;
        spawnedEnemies = 0;
    }

    /**
     * Places a defence in the level
     * @param defence the defence to be placed
     * @param instance the instance of the game to deduct money
     */
    public void placeDefence(Defence defence, ShadowDefend instance){
        if (instance.getMoney() >= defence.getCost()){
            levelDefence.add(defence);
            instance.deductMoney(defence.getCost());
        }
    }

    /**
     * Tells defences of the game to find a target and attack the target
     */
    public void defenceAttack(){
        for (Defence tower : levelDefence){
            tower.findTarget(this);
            tower.attack(this);
        }
    }

    /**
     * Adds a new projectile to the level
     * @param point the point to add the projectile
     * @param projectileSource its image source
     * @param target its target
     * @param damage its damage
     */
    public void addProjectile(Point point, String projectileSource, Enemy target, int damage){
        levelProjectiles.add(new Projectile(point, projectileSource, target, damage));
    }

    /**
     * Adds a new explosive to the level
     * @param point the point to add the explosive
     * @param projectileSource its image source
     * @param damage its damage
     */
    public void addExplosive(Point point, String projectileSource, int damage){
        levelProjectiles.add(new Explosive(point, projectileSource, damage));
    }

    /**
     * Loads the next level of the game
     * @throws IOException when the file for the map of the next level is not find
     */
    public void loadNextLevel() throws IOException {
        map = new TiledMap(SECOND_LEVEL);
        polyline = map.getAllPolylines().get(0);
        levelWave = new Wave(WAVES);
        levelDefence.clear();
        levelEnemy.clear();
        levelProjectiles.clear();
        loadedNextLevel = true;
        resetLevel();
    }

    /**
     * Clears all projectiles in the game when the wave has ended
     */
    public void resetLevelProjectiles(){
        levelProjectiles.clear();
    }

    /* Getters and setters */

    /**
     * Returns the map of the current level
     * @return the map
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Returns the current and future waves of the level
     * @return the waves of the level
     */
    public Wave getLevelWave() {
        return levelWave;
    }

    /**
     * Returns a list of enemies active in the level
     * @return the list of enemies active
     */
    public List<Enemy> getLevelEnemy() {
        return levelEnemy;
    }

    /**
     * Returns a boolean value indicating whether the next level of the game has been loaded
     * @return true if the next level has been loaded
     */
    public boolean hasLoadedNextLevel() {
        return loadedNextLevel;
    }

    /**
     * Returns a list of defences being placed in the level
     * @return the list of defences
     */
    public List<Defence> getLevelDefence() {
        return levelDefence;
    }
}
