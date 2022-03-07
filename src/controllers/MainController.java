package src.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.classes.Dictionary;
import src.classes.Game;
import src.classes.PastGamesInfo;
import src.classes.Tuple;

public class MainController implements Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu applicationMenu;
    @FXML
    private MenuItem startMenuItem;
    @FXML
    private MenuItem loadMenuItem;
    @FXML
    private MenuItem createMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private Menu detailsMenu;
    @FXML
    private MenuItem dictionaryMenuItem;
    @FXML
    private MenuItem roundsMenuItem;
    @FXML
    private MenuItem solutionMenuItem;
    @FXML
    private Label availableWordsTextLabel;
    @FXML
    public Label availableWordsNumberLabel;
    @FXML
    private Label totalPointsTextLabel;
    @FXML
    private Label totalPointsNumberLabel;
    @FXML
    private Label correctChoicesTextLabel;
    @FXML
    private Label correctChoicesNumberLabel;
    @FXML
    private ImageView hangmanImageView;
    @FXML
    private ScrollPane wordScrollPane;
    @FXML
    private Label wordLabel;
    @FXML
    private ScrollPane availableLettersScrollPane;
    @FXML
    private Label availableLettersLabel;
    @FXML
    private Label letterTextLabel;
    @FXML
    private ComboBox<String> letterChoiceComboBox;
    @FXML
    private Label positionTextLabel;
    @FXML
    private ComboBox<String> positionChoiceComboBox;
    @FXML
    private Button submitChoiceButton;

    public Game game;

    public Dictionary activeDictionary;

    public PastGamesInfo pastGamesInfo;

    public boolean gameAlreadySaved;

    public void initialize() {
        pastGamesInfo = new PastGamesInfo(); /* τα games δεν κρατιούνται αν κλείσεις το παράθυρο */
    }

    public void testPrintPastGamesInfo() {
        for (int i = 0; i < 5; i++) {
            System.out.println(pastGamesInfo.chosenWord[i] + " " + pastGamesInfo.numberOfChoices[i] + " "
                    + pastGamesInfo.winner[i]);
        }
    }

    @FXML
    public void handleStart() throws MalformedURLException {
        try {
            game = new Game(activeDictionary);
            gameAlreadySaved = false;

            refreshCorrectChoices();

            refreshWordLabel();
            wordLabel.setStyle("-fx-text-fill: BLACK");
            wordLabel.setFont(Font.font("Monospace", 24)); /* Might break the system if the font isn't there */

            refreshAvailableLetters();

            letterChoiceComboBox.getItems().clear();
            positionChoiceComboBox.getItems().clear();

            for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                letterChoiceComboBox.getItems().add(String.valueOf(c));
            }

            for (int i = 0; i < game.pickedWord.length(); i++) { /* i = 1 might be better */
                positionChoiceComboBox.getItems().add(String.format("%02d", i));
            }

            letterChoiceComboBox.getSelectionModel().selectFirst();
            positionChoiceComboBox.getSelectionModel().selectFirst();

            refreshTotalPoints();

            Image hangman = new Image(
                    new File(String.format("images/wrong0.png", game.wrongChoices)).toURI().toURL().toString(), 400,
                    400, false, true);
            hangmanImageView.setImage(hangman);
        }
        catch (

        NullPointerException e) {
            e.printStackTrace();
            wordLabel.setText(
                    "You haven't loaded a dictionary yet.\nSelect Application/Load to load a dictionary first.");
            wordLabel.setStyle("-fx-text-fill: RED");
        }
    }

    @FXML
    public void handleLoad() throws Exception {
        createPopUpWindow("../fxml/load.fxml", "Load");
    }

    @FXML
    public void handleCreate() throws Exception {
        createPopUpWindow("../fxml/create.fxml", "Create");
    }

    @FXML
    public void handleExit() {
        Stage stage = (Stage) submitChoiceButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleDictionary() throws Exception {
        createPopUpWindow("../fxml/dictionary.fxml", "Dictionary");
    }

    @FXML
    public void handleRounds() throws Exception {
        createPopUpWindow("../fxml/rounds.fxml", "Rounds");
    }

    @FXML
    public void handleSolution() throws IOException {
        executeLossSequence();

        /* show solution */
    }

    @FXML
    public void executeLossSequence() throws IOException {
        game.lose = true;
        game.wrongChoices = 6;
        this.handleSubmit();
    }

    public void saveGameInfo() {
        pastGamesInfo.addGameInfo(game.pickedWord, String.valueOf(game.correctChoices + game.wrongChoices),
                game.win ? "You" : "Computer");
    }

    @FXML
    public void handleSubmit() throws IOException {
        System.out.println("handleSubmit() invoked");
        char letter = letterChoiceComboBox.getValue().charAt(0);
        int charIndex = Integer.parseInt(positionChoiceComboBox.getValue());
        int ret = game.makeChoice(letter, charIndex);
        System.out.println(ret);
        switch (ret) {
        case -1:
        case -2:
            Image hangman = new Image(
                    new File(String.format("images/wrong%d.png", game.wrongChoices)).toURI().toURL().toString(), 400,
                    400, false, true);
            hangmanImageView.setImage(hangman);
            break;
        case 0:
        case 1:
            break;
        case 2:
            Image winningHangman = new Image(new File("images/victory.png").toURI().toURL().toString(), 400, 400, false,
                    true);
            hangmanImageView.setImage(winningHangman);
            break;
        }

        if (!gameAlreadySaved && (game.win || game.lose)) {
            saveGameInfo();
            gameAlreadySaved = true;
        }
        refreshWordLabel();
        refreshAvailableLetters();
        refreshTotalPoints();
        refreshCorrectChoices();
    }

    @FXML
    public void refreshCorrectChoices() {
        try {
            correctChoicesNumberLabel.setText(
                    String.valueOf(100 * game.correctChoices / (game.wrongChoices + game.correctChoices)) + "%");
        }
        catch (ArithmeticException e) {
            correctChoicesNumberLabel.setText("---");
        }
    }

    @FXML
    public void refreshAvailableLetters() {
        StringBuilder sb2 = new StringBuilder("Available letters in each position:" + "\n\n");

        int i = 0;
        for (ArrayList<Tuple> letterList : game.letters) {
            sb2.append("Position " + String.valueOf(i) + ": ");
            for (Tuple t : letterList) {
                if (t.probability != 0) {
                    sb2.append(t.character + " ");
                }
            }
            sb2.append("\n");
            i++;
        }

        availableLettersLabel.setText(sb2.toString());

    }

    @FXML
    public void refreshWordLabel() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int index = 0;
        for (String s : game.filledLetters) {
            String space = index == 0 ? " " : "  ";
            sb1.append(space + s.charAt(0));
            sb2.append(String.format("%02d", index) + " ");
            index++;
        }

        wordLabel.setText("");

        if (game.win) {
            wordLabel.setText(wordLabel.getText() + "You won!" + "\n");
            wordLabel.setStyle("-fx-text-fill: GREEN");
        }
        else if (game.lose) {
            wordLabel.setText(wordLabel.getText() + "You lost! The word was: " + game.pickedWord + "\n");
            wordLabel.setStyle("-fx-text-fill: RED");
        }
        // wordLabel.setText(wordLabel.getText() + game.pickedWord + "\n");
        System.out.println(game.pickedWord);

        wordLabel.setText(wordLabel.getText() + sb2.toString() + "\n");
        wordLabel.setText(wordLabel.getText() + sb1.toString());
    }

    @FXML
    public void refreshTotalPoints() {
        totalPointsNumberLabel.setText(String.valueOf(game.points));
    }

    @FXML
    private void createPopUpWindow(String fxmlFilePath, String windowTitle) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setMainController(this);
        if (windowTitle == "Dictionary") { /* REALLY BAD PRACTICE! */
            DictionaryController dummycontroller = (DictionaryController) controller;
            dummycontroller.refreshDictionaryUI();
        }
        if (windowTitle == "Rounds") {
            RoundsController dummycontroller = (RoundsController) controller;
            dummycontroller.refreshUI();
        }
        // System.out.println(this);

        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void setMainController(Controller controller) {
    }

    public void test() {
        System.out.println(availableWordsTextLabel.getText());
    }

    public String testGetText() {
        return availableWordsTextLabel.getText();
    }

    public void testSetText(String text) {
        availableWordsTextLabel.setText(text);
    }

    public void setActiveDictionary(Dictionary dictionary) {
        activeDictionary = dictionary;
    }

    public Dictionary getActiveDictionary() {
        return game.getActiveDictionary();
    }
}
