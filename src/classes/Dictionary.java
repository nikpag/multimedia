package src.classes;

import java.util.List;

public class Dictionary {
    /**
     * Creates a new dictionary.
     * 
     * @param list the list of words to be set in the newly created dictionary.
     */
    public Dictionary(List<String> list) {
        listOfWords = list;
        countWordsOfVariousLengths();
    }

    /**
     * Gets the number of 6-letter words.
     * 
     * @return the number of 6-letter words.
     */
    public int getNumberOfLength6() {
        return numberOfLength6;
    }

    /**
     * Gets the number of 7-to-9-letter words.
     * 
     * @return the number of 7-to-9-letter words.
     */
    public int getNumberOfLength7To9() {
        return numberOfLength7To9;
    }

    /**
     * Gets the number of 10-or-more-letter words.
     * 
     * @return the number of 10-or-more-letter words.
     */
    public int getNumberOfLength10Plus() {
        return numberOfLength10Plus;
    }

    /**
     * Gets the number of all available words.
     * 
     * @return the number of all available words.
     */
    public int getNumberOfAvailableWords() {
        return numberOfAvailableWords;
    }

    /**
     * Gets the list of words in the dictionary.
     * 
     * @return the list of words in the dictionary.
     */
    public List<String> getListOfWords() {
        return listOfWords;
    }

    /**
     * Counts the number of words having 6, 7-9, or 10+ letters, and changes the
     * dictionary's fields accordingly.
     */
    private void countWordsOfVariousLengths() {
        for (String s : listOfWords) {
            if (s.length() == 6)
                numberOfLength6++;
            else if (s.length() >= 7 && s.length() <= 9)
                numberOfLength7To9++;
            else if (s.length() >= 10)
                numberOfLength10Plus++;

            numberOfAvailableWords++;
        }
    }

    private int numberOfLength6 = 0; /* Number of 6-letter words */
    private int numberOfLength7To9 = 0; /* Number of 7-to-9-letter words */
    private int numberOfLength10Plus = 0; /* Number of 10-or-more-letter words */
    private int numberOfAvailableWords = 0; /* Number of all available words */
    private List<String> listOfWords; /* List of all the words in the dictionary */
}
