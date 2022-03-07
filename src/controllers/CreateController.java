package src.controllers;

import java.io.FileNotFoundException;
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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import src.exceptions.UnbalancedException;
import src.exceptions.UndersizeException;

public class CreateController implements Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label openLibraryIDLabel;
    @FXML
    private Label dictionaryIDLabel;
    @FXML
    private TextField openLibraryIDTextField;
    @FXML
    private TextField dictionaryIDTextField;
    @FXML
    private Button createButton;
    @FXML
    private Label errorMessageLabel; /* should be something like messageLabel */

    public void setMainController(Controller controller) {
    }

    private URL getURLFromOpenLibraryID(String openLibraryID) throws MalformedURLException {
        String urlString = String.format("https://openlibrary.org/works/%s.json", openLibraryID);
        URL url = new URL(urlString);
        return url;
    }

    private JsonObject getJSONObjectFromURL(URL url) throws IOException {
        InputStream is = url.openStream();
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        return obj;
    }

    private String getDescriptionFromJSONObject(JsonObject obj) {
        String description;
        try {
            description = obj.getJsonObject("description").getString("value");
        }
        /* Exception could be a bit more specific here */
        catch (Exception e) {
            System.out.println(e); /* find out what descriptions are being thrown here */
            /* description is a string instead of an object */
            description = obj.getString("description");
        }
        return description;
    }

    private String replaceNonLettersWithWhitespace(String str) {
        return str.replaceAll("[^a-zA-Z]", " ");
    }

    private void fillListWithValidWordsFromTokenizer(ArrayList<String> list, StringTokenizer tokenizer) {
        /* Filter out short words (<5 letters) and convert to UPPERCASE */
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            if (nextToken.length() >= 6) {
                list.add(nextToken.toUpperCase());
            }
        }
    }

    private int countLongWords(Set<String> wordSet) {
        int longWordCount = 0;
        for (String string : wordSet) {
            if (string.length() >= 9) {
                longWordCount++;
            }
        }
        return longWordCount;
    }

    private int countTotalWords(Set<String> wordSet) {
        return wordSet.size();
    }

    private void writeWordSetToFile(Set<String> wordSet, String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        for (String str : wordSet) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

    public void checkValidity(int totalWordCount, int longWordCount) throws UndersizeException, UnbalancedException {
        /* here we should check for undersize and unbalanced exceptions */
        if (totalWordCount < 20) {
            throw new UndersizeException("UndersizeException");
        }

        /* UnbalancedException */
        if ((float) longWordCount / totalWordCount < 0.2) {
            throw new UnbalancedException("UnbalancedException");
        }
    }

    @FXML
    public void createDictionary(ActionEvent event) throws IOException {

        String openLibraryID = openLibraryIDTextField.getText();
        String dictionaryID = dictionaryIDTextField.getText();

        URL url = getURLFromOpenLibraryID(openLibraryID);

        try {
            JsonObject obj = getJSONObjectFromURL(url);
            /* Get description from JSON Object */

            String description = getDescriptionFromJSONObject(obj);

            /* Replace punctuation with whitespace and create tokens */
            String words = replaceNonLettersWithWhitespace(description);
            StringTokenizer tokenizer = new StringTokenizer(words);

            /* long words: length >= 6 */
            ArrayList<String> validWordList = new ArrayList<>();

            fillListWithValidWordsFromTokenizer(validWordList, tokenizer);

            /* Filter out duplicates */
            Set<String> validWordSet = new HashSet<>(validWordList);

            /* Count number of total words and long words (>=9 letters) */
            int totalWordCount = countTotalWords(validWordSet);
            int longWordCount = countLongWords(validWordSet);

            /* UndersizeException */
            try {
                checkValidity(totalWordCount, longWordCount);
            }
            catch (UndersizeException e) {
                errorMessageLabel.setText("The dictionary must have 20+ words");
                errorMessageLabel.setStyle("-fx-text-fill: red");
            }
            catch (UnbalancedException e) {
                errorMessageLabel.setText("At least 20% of the words should have 9+ letters");
                errorMessageLabel.setStyle("-fx-text-fill: red");
            }

            writeWordSetToFile(validWordSet, String.format("medialab/hangman_%s.txt", dictionaryID));
            errorMessageLabel.setText(
                    String.format("The dictionary with ID \"%s\" has been successfully created", dictionaryID));
            errorMessageLabel.setStyle("-fx-text-fill: green");
        }
        catch (FileNotFoundException e) {
            errorMessageLabel.setText("There is no book that matches the specified Open Library ID");
        }
    }
}
