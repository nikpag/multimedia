package src.classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import src.exceptions.NoDescriptionFieldException;
import src.exceptions.UnbalancedException;
import src.exceptions.UndersizeException;

/**
 * This class is used to create .txt files that have one word per line (called
 * dictionaries), given an Open Library ID (for the API call), and a Dictionary
 * ID (for the filename).
 */
public class DictionaryCreator {
    /**
     * This function writes the contents of the "description" field of the book with
     * Open Library ID into a file named "hangman-<Dictionary ID>.txt" where
     * <Dictionary ID> is the dictionaryID provided by the caller.
     * 
     * @param openLibraryID the desired Open Library ID used to search the
     *                      corresponding book
     * @param dictionaryID  the desired Dictionary ID used to name the file
     * @throws UnbalancedException         if less than 20% of the dictionary's
     *                                     words have 9+ letters.
     * @throws UndersizeException          if the dictionary has 20 words or fewer.
     * @throws IOException
     * @throws NoDescriptionFieldException if the JSON returned by the Works API
     *                                     call doesn't have a description field.
     */
    public void create(String openLibraryID, String dictionaryID)
            throws UnbalancedException, UndersizeException, IOException, NoDescriptionFieldException {
        /* Get the "description" field of the corresponding book */
        URL url = getURLFromOpenLibraryID(openLibraryID);
        JsonObject jsonObject = getJSONObjectFromURL(url);
        String description = getDescriptionFromJSONObject(jsonObject);

        /* Remove non-letter characters */
        String words = removeNonLetterCharacters(description);

        /* Add only 6-or-more-letter words */
        StringTokenizer tokenizer = new StringTokenizer(words);
        ArrayList<String> validWordList = new ArrayList<>();
        fillListWithValidWordsFromTokenizer(validWordList, tokenizer);

        /* Remove duplicates */
        Set<String> validWordSet = new HashSet<>(validWordList);

        /* Count number of total words 9-or-more-letter words */
        int totalWordCount = countTotalWords(validWordSet);
        int longWordCount = count9PlusLetterWords(validWordSet);

        /* Throw Undersize/Unbalanced exceptions if needed */
        if (totalWordCount < 20) {
            throw new UndersizeException("UndersizeException");
        }
        if ((float) longWordCount / totalWordCount < 0.2) {
            throw new UnbalancedException("UnbalancedException");
        }

        /* If everything has been successful, write the contents to the corresponsing file */
        writeWordSetToFile(validWordSet, String.format("medialab/hangman_%s.txt", dictionaryID));
    }

    /**
     * This function turns an openLibraryID into a URL needed for the API call.
     * 
     * @param openLibraryID the Open Library ID provided by the user.
     * @return the corresponding URL
     * @throws MalformedURLException
     */
    private URL getURLFromOpenLibraryID(String openLibraryID) throws MalformedURLException {
        String urlString = String.format("https://openlibrary.org/works/%s.json", openLibraryID);
        return new URL(urlString);
    }

    /**
     * This function returns a JSON Object, by calling the API with the URL given.
     * 
     * @param url the URL to be used for the API call.
     * @return the resulting JSON Object from the API call.
     * @throws IOException
     */
    private JsonObject getJSONObjectFromURL(URL url) throws IOException {
        InputStream inputStream = url.openStream();
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject jsonObject = jsonReader.readObject();
        return jsonObject;
    }

    /**
     * This function fetches the description from a JSON object. If there is a
     * "description" object that has a "value" string, then the contents of the
     * "value" string are returned. Otherwise, if there is a "description" string
     * (not the same as a "description" object), then the contents of the
     * "description" string are returned instead.
     * 
     * @param jsonObject the JSON object containing the description.
     * @return the description of the JSON object as a string.
     * @throws NoDescriptionFieldException if the JSON object returned doesn't have
     *                                     a description field.
     */
    private String getDescriptionFromJSONObject(JsonObject jsonObject) throws NoDescriptionFieldException {
        String description;

        try {
            description = jsonObject.getJsonObject("description").getString("value");
        }
        catch (ClassCastException | NullPointerException e1) {
            /* Description is not an object, but it might be a string */
            try {
                description = jsonObject.getString("description");
            }
            catch (ClassCastException | NullPointerException e2) {
                /* Description is not an object nor a string */
                throw new NoDescriptionFieldException("NoDescriptionFieldException");
            }
        }
        /* Fetched description successfully */
        return description;
    }

    /**
     * This function replaces all non-letter characters with whitespace. This way,
     * all that is left is letters grouped into words.
     * 
     * @param string the string from which to remove non-letter characters.
     * @return the string after all non-letter characters have been replaced by
     *         whitespace.
     */
    private String removeNonLetterCharacters(String string) {
        return string.replaceAll("[^a-zA-Z]", " ");
    }

    /**
     * This function takes words from a tokenizer and adds them to a list. Only
     * 6-or-more-letter words are added to the list. All words get converted to
     * UPPERCASE.
     * 
     * @param list            the list to be modified.
     * @param stringTokenizer the tokenizer to take the words from.
     */
    private void fillListWithValidWordsFromTokenizer(ArrayList<String> list, StringTokenizer stringTokenizer) {
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            /* Only add words of 6+ letters to the list */
            if (nextToken.length() >= 6) {
                list.add(nextToken.toUpperCase());
            }
        }
    }

    /**
     * This function counts how many 9-or-more-letter words are in a set
     * 
     * @param wordSet the set under examination.
     * @return the number of 9-or-more-letter-words in the set.
     */
    private int count9PlusLetterWords(Set<String> wordSet) {
        int longWordCount = 0;
        for (String string : wordSet) {
            if (string.length() >= 9) {
                longWordCount++;
            }
        }
        return longWordCount;
    }

    /**
     * This function counts how many words a set has.
     * 
     * @param wordSet the set under examination.
     * @return the number of words in the set.
     */
    private int countTotalWords(Set<String> wordSet) {
        return wordSet.size();
    }

    /**
     * This function takes a set of words and writes them into a file (one word per
     * line).
     * 
     * @param wordSet the set to take words from.
     * @param path    the path of the file to write to.
     * @throws IOException
     */
    private void writeWordSetToFile(Set<String> wordSet, String path) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        for (String str : wordSet) {
            fileWriter.write(str + System.lineSeparator());
        }
        fileWriter.close();
    }

}
