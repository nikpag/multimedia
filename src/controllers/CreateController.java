package src.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.classes.DictionaryCreator;
import src.exceptions.NoDescriptionFieldException;
import src.exceptions.UnbalancedException;
import src.exceptions.UndersizeException;

/**
 * This controller is used by the "Create" pop-up window that is opened when the
 * user selects Application-->Create from the menu.
 */
public class CreateController implements Controller {

    /* The main window controller isn't needed here */
    public void setMainController(Controller controller) {
        /* Do nothing */
    }

    /**
     * This function is called when the "Create" button is clicked. It creates a new
     * dictionary, using the specified Open Library ID, and names it using the
     * specified Dictionary ID.
     * 
     * @param event the event fired when the button is clicked.
     */
    @FXML
    private void createDictionary(ActionEvent event) {
        /* Assume failure (red color). If the call is successful, the color will be changed to green later. */
        messageLabel.setStyle("-fx-text-fill: red");

        /* Fetch Open Library ID and Dictionary ID from user */
        String openLibraryID = openLibraryIDTextField.getText();
        String dictionaryID = dictionaryIDTextField.getText();

        try {
            DictionaryCreator dictionaryCreator = new DictionaryCreator();
            dictionaryCreator.create(openLibraryID, dictionaryID);

            /* Successful creation */
            messageLabel.setText("The dictionary has been created successfully.");
            messageLabel.setStyle("-fx-text-fill: green");
        }
        catch (UndersizeException e) {
            messageLabel.setText("The dictionary must have 20+ words.");
        }
        catch (UnbalancedException e) {
            messageLabel.setText("At least 20% of the words should have 9+ letters.");
        }
        catch (MalformedURLException e) {
            messageLabel.setText("The URL is malformed. Please try again.");
        }
        catch (IOException e) {
            messageLabel.setText("There is no book that matches the specified Open Library ID.");
        }
        catch (NoDescriptionFieldException e) {
            messageLabel.setText("There is no \"description\" field for the specified Open Library ID.");
        }
    }

    @FXML
    private TextField openLibraryIDTextField; /* A text field where the user enters the Open Library ID of the desired book. */
    @FXML
    private TextField dictionaryIDTextField; /* A text field where the user enters the desired Dictionary ID. */
    @FXML
    private Label messageLabel; /* A label where the corresponding success/failure message is displayed. */
}
