package src.classes;

import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.lang.Character;

/**
 * This class represents a game/round. That means everytime the user clicks on
 * "Start", a new Game instance is created. This class implements the entire
 * logic of the game, but does not implement visuals. Thus, it is conformant
 * with the MVC design pattern (as encouraged by the JavaFXonFXML philosophy).
 */
public class Game {
    /**
     * Creates a new Game instance. A new Game instance is created every time the
     * "Start" button is pressed.
     * 
     * @param dict the active dictionary to be used for the game.
     */
    public Game(Dictionary dict) {
        /* Set the active dictionary and pick a random word from it */
        activeDictionary = dict;
        chosenWord = getRandomWordFromActiveDictionary();

        /* Initialize every letter as hidden (i.e. "_") */
        filledLetters = new String[chosenWord.length()];
        for (int i = 0; i < filledLetters.length; i++) {
            filledLetters[i] = "_";
        }

        /* Put all words in the candidate set... */
        candidates = new HashSet<>(activeDictionary.getListOfWords());

        /* ...then filter based on length */
        removeBasedOnLength();

        /* Build the lists of available letters, one for each position */
        listOfAvailableLetters = new ArrayList<>();
        for (int i = 0; i < chosenWord.length(); i++) {
            listOfAvailableLetters.add(new ArrayList<Tuple>());
        }

        /* Initialize every array with (character, probability) tuples. Here, are probabilities are 0... */
        for (ArrayList<Tuple> tupleList : listOfAvailableLetters) {
            for (Character charLetter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                tupleList.add(new Tuple(charLetter, 0f));
            }
        }

        /* ...until they get calculated correctly */
        calculateProbabilities();

        /* Finally, sort every available letter list based on probability descending */
        for (ArrayList<Tuple> tupleList : listOfAvailableLetters) {
            Collections.sort(tupleList, new TupleComparator());
        }
    }

    /**
     * Submits the user's choice, checks to see if it is right or wrong, and acts
     * accordingly. It also decided if the game has been won, lost, or not decided
     * yet.
     * 
     * @param letter    the letter chosen by the player.
     * @param charIndex the position chosen by the player.
     * @return -2 if the game has been lost;
     *         <p>
     *         -1 if the choice was wrong but the game hasn't been lost yet;
     *         <p>
     *         0 if nothing happened (this could happen if the player chose a
     *         position already unveiled, for example);
     *         <p>
     *         1 if the choice was correct but the game hasn't been won yet;
     *         <p>
     *         2 if the game has been won;
     */
    public int makeChoice(char letter, int charIndex) {
        /*
         * If the game has been won/lost, return early (this could happen if the player
         * continued to choose letters after winning/losing, for example).
         */
        if (gameWon)
            return 2;
        if (gameLost)
            return -2;

        /* Assume nothing has happened and the game has not been decided yet */
        int returnValue = 0;
        gameNotDecidedYet = false;

        /* Check if the choice is correct or wrong (if the letter has been unveiled nothing happens here) */
        if (letter == chosenWord.charAt(charIndex) && filledLetters[charIndex] == "_") {
            correctChoice(letter, charIndex);
            returnValue = 1;
        }
        else if (filledLetters[charIndex] == "_") { /* Wrong choice */
            wrongChoice(letter, charIndex);
            returnValue = -1;
        }

        /* Recalculate probabilities and resort available letters based on their probability descending */
        calculateProbabilities();
        for (ArrayList<Tuple> tupleList : listOfAvailableLetters)
            Collections.sort(tupleList, new TupleComparator());

        if (wrongChoices == 6) {
            /* The game has been lost */
            gameLost = true;
            return -2;
        }

        /* Check if hidden letters still exist */
        for (String _letter : filledLetters) {
            if (_letter == "_") {
                gameNotDecidedYet = true; /* A hidden letter was found */
                break;
            }
        }
        if (gameNotDecidedYet) {
            return returnValue; /* {0, -1, 1}, depending on event flow */
        }
        else {
            gameWon = true;
            return 2; /* The game has been won */
        }
    }

    /**
     * Get the listOfAvailableLetters.
     * 
     * @return the listOfAvailableLetters.
     */
    public ArrayList<ArrayList<Tuple>> getListOfAvailableLetters() {
        return listOfAvailableLetters;
    }

    /**
     * See if the game has been won.
     * 
     * @return the gameWon value.
     */
    public boolean getGameWon() {
        return gameWon;
    }

    /**
     * See if the game has been lost.
     * 
     * @return the gameLost value.
     */
    public boolean getGameLost() {
        return gameLost;
    }

    /**
     * Set the gameLost value
     * 
     * @param gl the value to set gameLost to.
     */
    public void setGameLost(boolean gl) {
        gameLost = gl;
    }

    /**
     * See how many correct choices have been made.
     * 
     * @return the number of correct choices made.
     */
    public int getCorrectChoices() {
        return correctChoices;
    }

    /**
     * See how many wrong choices have been made.
     * 
     * @return the number of wrong choice made.
     */
    public int getWrongChoices() {
        return wrongChoices;
    }

    /**
     * Set the number of wrong choices. This function is not used during normal
     * gameplay (the number of wrong choices is auto-incremented everytime a wrong
     * choice is made), but it is used to simulate a loss when the player selects
     * the "Details/Solution" menu item.
     * 
     * @param wc
     */
    public void setWrongChoices(int wc) {
        wrongChoices = wc;
    }

    /**
     * Get the number of points the player has scored.
     * 
     * @return the number of points scored.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Get the chosen word for the current game.
     * 
     * @return the chosen word.
     */
    public String getChosenWord() {
        return chosenWord;
    }

    /**
     * Get the letters currently unveiled by the player.
     * 
     * @return the filledLetters array.
     */
    public String[] getFilledLetters() {
        return filledLetters;
    }

    /**
     * Called if the choice made by the player was correct. It updates the candidate
     * set and the player's points.
     * 
     * @param letter   the letter chosen by the player.
     * @param position the position chosen by the player.
     */
    private void correctChoice(char letter, int position) {
        /* Mark the letter as unveiled */
        filledLetters[position] = String.valueOf(letter);

        /* Refilter the candidate set */
        removeBasedOnLetter(letter, position, true);

        float prob = getProbabilityOfLetterInPosition(letter, position);
        points += calculatePoints(prob, true);
        correctChoices++;
    }

    /**
     * Called if the choice made by the player was wrong. It updates the candidate
     * set and the player's points.
     * 
     * @param letter   the letter chosen by the player.
     * @param position the position chosen by the player.
     */
    private void wrongChoice(char letter, int position) {
        /* Refilter the candidate set */
        removeBasedOnLetter(letter, position, false);

        Float prob = null; /* We don't care about probability when the choice is wrong */
        points -= calculatePoints(prob, false);
        if (points < 0)
            points = 0; /* Points can never be negative */
        wrongChoices++;
    }

    /**
     * Calculates the probabilities for all letters of all positions. The
     * probabilities are calculated based on how many words of the candidate set
     * have the corresponding character in the corresponding position.
     */
    private void calculateProbabilities() {
        /* For all tuples in the list of lists: */
        int pos = 0;
        for (ArrayList<Tuple> tupleList : listOfAvailableLetters) {
            for (Tuple tuple : tupleList) {
                /* Count how many candidate words have the specified character at the specified position */
                int counter = 0;
                for (String word : candidates) {
                    if (tuple.getCharacter() == word.charAt(pos)) {
                        counter++;
                    }
                }
                /* Calculate and set the desired probability */
                tuple.setProbability((float) counter / candidates.size());
            }
            pos++;
        }
    }

    /**
     * Calculates how many points correspond to the current choice. Note that the
     * points are always positive, and it is the responsibility of the outside
     * caller to subtract them in case of a wrong choice.
     * 
     * @param prob  the probability associated with the chosen letter
     * @param right true if the choice made was right, else false.
     * @return
     */
    private int calculatePoints(Float prob, boolean right) {
        if (!right)
            return 15; /* You LOSE 15 */
        if (prob >= 0.6)
            return 5; /* 0.6+ */
        if (prob >= 0.4)
            return 10; /* 0.4-0.6 */
        if (prob >= 0.25)
            return 15; /* 0.25-0.4 */

        return 20; /* 0.0-0.25 */
    }

    /**
     * Removes words from the candidate set that don't have the same length as the
     * chosen word.
     */
    private void removeBasedOnLength() {
        Set<String> candidatesClone = new HashSet<>(candidates); /* Create a clone we can traverse safely */
        for (String item : candidatesClone) {
            if (item.length() != chosenWord.length()) {
                candidates.remove(item);
            }
        }
    }

    /**
     * Remove words from the candidate set based on the player's choice.
     * <p>
     * If the choice made was right, remove all words NOT having the chosen
     * character at the chosen position.
     * <p>
     * If the choice made was wrong, remove all words having the chosen character at
     * the chosen position.
     * 
     * @param letter   the letter picked by the user.
     * @param position the position picked by the user.
     * @param right    true if the choice made was right, else false.
     */
    private void removeBasedOnLetter(Character letter, int position, boolean right) {
        Set<String> candidatesClone = new HashSet<>(candidates); /* Create a clone we can traverse safely */
        for (String item : candidatesClone) {
            /* 
             * If the choice is right, remove all words NOT having the chosen character at the chosen position.
             * If the choice is wrong, remove all words having the chosen character at the chosen position.
             */
            boolean mustRemove = right ? letter != item.charAt(position) : letter == item.charAt(position);

            if (mustRemove) {
                candidates.remove(item);
            }
        }
    }

    /**
     * Fetches a random word from the active dictionary.
     * 
     * @return a random word from the active dictionary.
     */
    private String getRandomWordFromActiveDictionary() {
        Random random = new Random();
        int randomIndex = random.nextInt(activeDictionary.getListOfWords().size());
        return activeDictionary.getListOfWords().get(randomIndex);
    }

    /**
     * Calculates the probability of a certain letter placed in a specific position.
     * 
     * @param letter   the desired letter.
     * @param position the desired position.
     * @return the probability corresponding to
     */
    private float getProbabilityOfLetterInPosition(char letter, int position) {
        int indexOf = -1; /* Holds the position of the letter in the list */

        /* Linear search */
        int i = 0;
        for (Tuple tuple : listOfAvailableLetters.get(position)) {
            if (tuple.getCharacter() == letter) {
                indexOf = i; /* Found it! */
                break;
            }
            i++;
        }

        float prob = listOfAvailableLetters.get(position).get(indexOf).getProbability();

        return prob;
    }

    /*
     * A list of lists of Tuples. Every position of the hidden word has its
     * corresponding list of Tuples. Those Tuples represent the available letters
     * for each position. Each Tuple is of the form (character, probability).
     */
    private ArrayList<ArrayList<Tuple>> listOfAvailableLetters;

    /*
     * Holds all the words in the dictionary that match the chosen word in every
     * unveiled letter, and also have the same length. For example, if the unveiled
     * letters are H _ L _ then candidate words could be HILL, HALL, HOLE etc.
     */
    private Set<String> candidates;

    /* Set to true if the game has been won. */
    private boolean gameWon = false;

    /* Set to true if the game has been lost. */
    private boolean gameLost = false;

    /* Auxilliary variable used to determine if the game has been decided or not. */
    private boolean gameNotDecidedYet = false;

    /* The active dictionary of the game. */
    private Dictionary activeDictionary;

    /* The current number of correct choices. */
    private int correctChoices = 0;

    /* The current number of wrong choices. */
    private int wrongChoices = 0;

    /* The total number of current points. Shouldn't be less than zero. */
    private int points = 0;

    /* The word chosen for the current game. */
    private String chosenWord;

    /* 
     * An array holding the currently unveiled letters. 
     * Not-yet-found letters are represented as "_". 
     */
    private String[] filledLetters;

}
