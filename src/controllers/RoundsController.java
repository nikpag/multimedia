package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import src.classes.PastGamesInfo;

public class RoundsController implements Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label chosenWordLabel;
    @FXML
    private Label numberOfChoicesLabel;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label word1Label;
    @FXML
    private Label choices1Label;
    @FXML
    private Label winner1Label;
    @FXML
    private Label word2Label;
    @FXML
    private Label choices2Label;
    @FXML
    private Label winner2Label;
    @FXML
    private Label word3Label;
    @FXML
    private Label choices3Label;
    @FXML
    private Label winner3Label;
    @FXML
    private Label word4Label;
    @FXML
    private Label choices4Label;
    @FXML
    private Label winner4Label;
    @FXML
    private Label word5Label;
    @FXML
    private Label choices5Label;
    @FXML
    private Label winner5Label;

    public MainController mainController;

    /* Empty */
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }

    public void refreshUI() {
        word1Label.setText(mainController.pastGamesInfo.chosenWord[0]);
        choices1Label.setText(mainController.pastGamesInfo.numberOfChoices[0]);
        winner1Label.setText(mainController.pastGamesInfo.winner[0]);

        word2Label.setText(mainController.pastGamesInfo.chosenWord[1]);
        choices2Label.setText(mainController.pastGamesInfo.numberOfChoices[1]);
        winner2Label.setText(mainController.pastGamesInfo.winner[1]);

        word3Label.setText(mainController.pastGamesInfo.chosenWord[2]);
        choices3Label.setText(mainController.pastGamesInfo.numberOfChoices[2]);
        winner3Label.setText(mainController.pastGamesInfo.winner[2]);

        word4Label.setText(mainController.pastGamesInfo.chosenWord[3]);
        choices4Label.setText(mainController.pastGamesInfo.numberOfChoices[3]);
        winner4Label.setText(mainController.pastGamesInfo.winner[3]);

        word5Label.setText(mainController.pastGamesInfo.chosenWord[4]);
        choices5Label.setText(mainController.pastGamesInfo.numberOfChoices[4]);
        winner5Label.setText(mainController.pastGamesInfo.winner[4]);

    }

    // public void initialize() {
    // word1Label.setText(mainController.pastGamesInfo.chosenWord[0]);
    // choices1Label.setText(mainController.pastGamesInfo.numberOfChoices[0]);
    // winner1Label.setText(mainController.pastGamesInfo.winner[0]);

    // word2Label.setText(mainController.pastGamesInfo.chosenWord[1]);
    // choices2Label.setText(mainController.pastGamesInfo.numberOfChoices[1]);
    // winner2Label.setText(mainController.pastGamesInfo.winner[1]);

    // word3Label.setText(mainController.pastGamesInfo.chosenWord[2]);
    // choices3Label.setText(mainController.pastGamesInfo.numberOfChoices[2]);
    // winner3Label.setText(mainController.pastGamesInfo.winner[2]);

    // word4Label.setText(mainController.pastGamesInfo.chosenWord[3]);
    // choices4Label.setText(mainController.pastGamesInfo.numberOfChoices[3]);
    // winner4Label.setText(mainController.pastGamesInfo.winner[3]);

    // word5Label.setText(mainController.pastGamesInfo.chosenWord[4]);
    // choices5Label.setText(mainController.pastGamesInfo.numberOfChoices[4]);
    // winner5Label.setText(mainController.pastGamesInfo.winner[4]);
    // }
}
