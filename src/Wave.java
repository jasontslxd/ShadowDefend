import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The wave of a level
 */
public class Wave {
    /* Attributes */
    private final List<String> waves = new ArrayList<>();
    private final List<String[]> waveInProgress = new ArrayList<>();
    private int waveNumber;
    private int numberToSpawn;
    private int actionDelay;
    private String action;
    private String enemyType;
    private boolean currentWaveFinished;

    /**
     * Creates a new wave by reading a text file of wave instructions
     * @param filePath filepath of the wave
     * @throws IOException when the wave file is not found
     */
    public Wave(String filePath) throws IOException {
        FileReader input = new FileReader(filePath);
        BufferedReader waveRead = new BufferedReader(input);
        String line;
        while ((line = waveRead.readLine()) != null){
            waves.add(line);
        }
        currentWaveFinished = false;
        // Parse the first wave
        parseWave();
    }


    /**
     * Parse the actions in the wave file with matching wave numbers and processes the first action
     */
    public void parseWave(){
        if (!waves.isEmpty()){
            String[] currentWave = waves.get(0).split(",");
            waveInProgress.add(currentWave);
            waves.remove(0);

            boolean same = true;
            while (same){
                if(!waves.isEmpty()){
                    String[] nextWave = waves.get(0).split(",");
                    if (currentWave[0].equals(nextWave[0])){
                        waveInProgress.add(nextWave);
                        waves.remove(0);
                    }
                    else{
                        same = false;
                    }
                }
                else{
                    same = false;
                }
            }
        }
        // Process the first wave event
        processWave();
    }

    /**
     * Sets the variables of the wave given the current action
     */
    public void processWave(){
        if (!waveInProgress.isEmpty()){
            currentWaveFinished = false;
            String[] currentWave = waveInProgress.get(0);
            if (currentWave[1].equals("delay")){
                waveNumber = Integer.parseInt(currentWave[0]);
                action = currentWave[1];
                numberToSpawn = -1;
                actionDelay = Integer.parseInt(currentWave[2]);
            }
            if (currentWave[1].equals("spawn")){
                waveNumber = Integer.parseInt(currentWave[0]);
                action = currentWave[1];
                numberToSpawn = Integer.parseInt(currentWave[2]);
                enemyType = currentWave[3];
                actionDelay = Integer.parseInt(currentWave[4]);
            }
            waveInProgress.remove(0);
        }
        else{
            numberToSpawn = 0;
            currentWaveFinished = true;
        }
    }

    /**
     * Checks if there are more waves to be processed
     * @return true if no more waves to be processed
     */
    public boolean allWavesFinished(){
        return waves.isEmpty() && currentWaveFinished;
    }

    /* Getters and setters */

    /**
     * Returns a string representing the type of enemy to spawn
     * @return the type of enemy to spawn
     */
    public String getEnemyType() {
        return enemyType;
    }

    /**
     * Returns an integer representing the number of enemies to spawn
     * @return the number of enemies to spawn
     */
    public int getNumberToSpawn() {
        return numberToSpawn;
    }

    /**
     * Returns an integer representing the current wave number
     * @return the current wave number
     */
    public int getWaveNumber() {
        return waveNumber;
    }

    /**
     * Returns an integer representing the number of frames to delay the action
     * @return the delay
     */
    public int getActionDelay() {
        return actionDelay;
    }

    /**
     * Returns a list of string arrays representing the current wave in progress and its wave events
     * @return the current wave in progress and its wave events
     */
    public List<String[]> getWaveInProgress() {
        return waveInProgress;
    }

    /**
     * Returns a string indicating the current wave action
     * @return the wave action
     */
    public String getAction() {
        return action;
    }
}
