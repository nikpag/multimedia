package src.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.classes.Dictionary;

/**
 * This controller is used by the "Load" pop-up window that is opened when the
 * user selects Application-->Load from the menu.
 */
public class LoadController implements Controller {
    /* Refer to the Controller interface for more details on this function. */
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }

    /**
     * This function is called when the user clicks the "Load" button. If the
     * dictionary file exists, then it is loaded as the active dictionary.
     * Otherwise, an error message shows up.
     */
    @FXML
    private void loadDictionary() {
        String dictionaryID = dictionaryIDTextField.getText();

        Path path = Paths.get(String.format("medialab/hangman_%s.txt",
                dictionaryID)); /* The path used to retrieve the dictionary file */

        try {
            /* Read all lines of the file and put them in a list */
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

            /* Create a new dictionary with this list */
            Dictionary dictionary = new Dictionary(lines);

            /* Now that the dictionary has been created, set it as active */
            mainController.setActiveDictionary(dictionary);

            /* Show message for successful load */
            messageLabel.setText("Dictionary loaded successfully");
            messageLabel.setStyle("-fx-text-fill: green"); /* Green for success */

            /* Update the number of available words in the active dictionary */
            mainController.getAvailableWordsNumberLabel()
                    .setText(String.valueOf(dictionary.getNumberOfAvailableWords()));
        }
        catch (NoSuchFileException e) {
            /* Show message that the dictionary doesn't exist */
            messageLabel.setText("The dictionary doesn't exist");
            messageLabel.setStyle("-fx-text-fill: red"); /* Red for failure */
        }
        catch (IOException e) {
            /* A more general IO error has happened */
            messageLabel.setText("There was an error while loading the dictionary");
            messageLabel.setStyle("-fx-text-fill: red"); /* Red for failure */
        }
    }

    @FXML
    private TextField dictionaryIDTextField; /* A text field where the user enters the desired Dictionary ID. */
    @FXML
    private Label messageLabel; /* A label where the corresponding success/failure message is displayed. */

    private MainController mainController; /* The controller of the main window (used for communication between windows) */
}
