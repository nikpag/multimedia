package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This controller is used by the "Rounds" pop-up window that is opened when the
 * user selects Details-->Rounds from the menu.
 */
public class RoundsController implements Controller {
    /**
     * This function is called when the "Rounds" pop-up window is created. It
     * renders the 5 most recent game stats on the screen.
     */
    @FXML
    public void renderRoundsUI() {
        /* Create arrays for easy traversal */
        Label[] wordLabelArray = { word1Label, word2Label, word3Label, word4Label, word5Label };
        Label[] choicesLabelArray = { choices1Label, choices2Label, choices3Label, choices4Label, choices5Label };
        Label[] winnerLabelArray = { winner1Label, winner2Label, winner3Label, winner4Label, winner5Label };

        /* 
         * Fetch {chosen word, number of choices, winner} from the main controller's 
         * PastGamesInfo instance and show them on the screen. 
         */
        for (int i = 0; i < 5; i++) {
            wordLabelArray[i].setText(mainController.getPastGamesInfo().getChosenWord(i));
            choicesLabelArray[i].setText(mainController.getPastGamesInfo().getNumberOfChoices(i));
            winnerLabelArray[i].setText(mainController.getPastGamesInfo().getWinner(i));
        }
    }

    /* Refer to the Controller interface for more details on this function. */
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }

    @FXML
    private Label word1Label; /* A label that holds the picked word for the most recent game */
    @FXML
    private Label choices1Label; /* A label that holds the number of choices/guesses for the most recent game */
    @FXML
    private Label winner1Label; /* A label that holds the winner (You/Computer) for the most recent game */
    @FXML
    private Label word2Label; /* 2nd most recent game */
    @FXML
    private Label choices2Label;
    @FXML
    private Label winner2Label;
    @FXML
    private Label word3Label; /* 3rd most recent game */
    @FXML
    private Label choices3Label;
    @FXML
    private Label winner3Label;
    @FXML
    private Label word4Label; /* 4th most recent game */
    @FXML
    private Label choices4Label;
    @FXML
    private Label winner4Label;
    @FXML
    private Label word5Label; /* 5th most recent game */
    @FXML
    private Label choices5Label;
    @FXML
    private Label winner5Label;

    private MainController mainController; /* The controller of the main window (used for communication between windows) */
}
