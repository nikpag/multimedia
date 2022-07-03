package src.classes;

/**
 * This class holds the 5 most recent games info, so they can be displayed in
 * the "Rounds" pop-up window.
 */
public class PastGamesInfo {
    /**
     * Creates a new PastGamesInfo instance.
     */
    public PastGamesInfo() {
        chosenWordArray = new String[5];
        numberOfChoicesArray = new String[5];
        winnerArray = new String[5];

        /* Initially, all 5 games will have "---" as their (empty) info. */
        for (int i = 0; i < 5; i++) {
            chosenWordArray[i] = "---";
            numberOfChoicesArray[i] = "---";
            winnerArray[i] = "---";
        }
    }

    /**
     * Adds the info for a newly completed game.
     * 
     * @param chosenWord      the chosen word of the newly completed game.
     * @param numberOfChoices the number of choices for the newly completed game.
     * @param winner          the winner of the newly completed game.
     */
    public void addGameInfo(String chosenWord, String numberOfChoices, String winner) {
        shiftAllGamesOnePosition(); /* Make room for the new game */

        /* The most recent game is saved at index 0. */
        chosenWordArray[0] = chosenWord;
        numberOfChoicesArray[0] = numberOfChoices;
        winnerArray[0] = winner;
    }

    /**
     * Gets the chosen word of the i-th game (Game 0 being the most recent).
     * 
     * @param i the desired game index.
     * @return the chosen word of the i-th game.
     */
    public String getChosenWord(int i) {
        return chosenWordArray[i];
    }

    /**
     * Gets the number of choices for the i-th game (Game 0 being the most recent).
     * Note that the number of choices is formatted into a string.
     * 
     * @param i the desired game index.
     * @return the number of choices for the i-th game.
     */
    public String getNumberOfChoices(int i) {
        return numberOfChoicesArray[i];
    }

    /**
     * Gets the winner of the i-th game (Game 0 being the most recent).
     * 
     * @param i the desired game index.
     * @return the winner of the i-th game.
     */
    public String getWinner(int i) {
        return winnerArray[i];
    }

    /**
     * Shifts all games in the "Rounds" table one position down, to make room for
     * the newly completed game.
     */
    private void shiftAllGamesOnePosition() {
        for (int i = 4; i >= 1; i--) {
            chosenWordArray[i] = chosenWordArray[i - 1];
            numberOfChoicesArray[i] = numberOfChoicesArray[i - 1];
            winnerArray[i] = winnerArray[i - 1];
        }
    }

    private String[] chosenWordArray; /* An array that holds the chosen words for the 5 most recent games. */
    private String[] numberOfChoicesArray; /* An array that holds the corresponding number of choices. */
    private String[] winnerArray; /* An array that holds the corresponding winner (You/Computer). */
}
