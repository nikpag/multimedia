package src.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.classes.Dictionary;
import src.classes.Game;
import src.classes.PastGamesInfo;
import src.classes.Tuple;

/**
 * This controller is used by the main window, and is the first controller
 * created when the user starts the Hangman app.
 */
public class MainController implements Controller {
    /**
     * We already have access to the main controller, so we don't have to do
     * anything in this function.
     */
    public void setMainController(Controller controller) {
        /* Do nothing */
    }

    /**
     * Get the currently active dictionary.
     * 
     * @return the currently active dictionary.
     */
    public Dictionary getActiveDictionary() {
        return activeDictionary;
    }

    /**
     * Sets the active dictionary to the given value.
     * 
     * @param dictionary the dictionary to set.
     */
    public void setActiveDictionary(Dictionary dictionary) {
        activeDictionary = dictionary;
    }

    /**
     * Get the label that holds the number of available words in the dictionary.
     * 
     * @return the available words number label.
     */
    @FXML
    public Label getAvailableWordsNumberLabel() {
        return availableWordsNumberLabel;
    }

    /**
     * Get the PastGamesInfo object used to hold round statistics.
     * 
     * @return the PastGamesInfo object.
     */
    public PastGamesInfo getPastGamesInfo() {
        return pastGamesInfo;
    }

    /**
     * This method is called when the FXML file is loaded.
     */
    public void initialize() {
        pastGamesInfo = new PastGamesInfo();
    }

    /**
     * This method is called when the "Application/Start" menu item is selected. It
     * starts a new game/round using the currently active dictionary.
     */
    @FXML
    private void handleStart() {
        try {
            /* Start a new game */
            game = new Game(activeDictionary);
            gameAlreadySaved = false;

            /* Refresh all elements */
            refreshTotalPoints();
            refreshCorrectChoices();
            refreshWordLabel();
            refreshAvailableLetters();
            letterChoiceComboBox.getItems().clear();
            positionChoiceComboBox.getItems().clear();

            /* Set black color and bigger font for the wordLabel */
            wordLabel.setStyle("-fx-text-fill: BLACK");
            wordLabel.setFont(Font.font("Monospace", 24));

            /* Add letter and position choices to ComboBoxes */
            for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                letterChoiceComboBox.getItems().add(String.valueOf(c));
            }
            for (int i = 0; i < game.getChosenWord().length(); i++) {
                positionChoiceComboBox.getItems().add(String.format("%02d", i));
            }

            /* Default to first option for both ComboBoxes (prevents null choices) */
            letterChoiceComboBox.getSelectionModel().selectFirst();
            positionChoiceComboBox.getSelectionModel().selectFirst();

            /* Render hangman image (0 errors) */
            String hangmanImageString = new File(String.format("images/wrong0.png", game.getWrongChoices())).toURI()
                    .toURL().toString();
            Image hangman = new Image(hangmanImageString, 400, 400, false, true);
            hangmanImageView.setImage(hangman);
        }
        catch (NullPointerException e) {
            showNoDictionaryLoadedYetMessage();
        }
        catch (MalformedURLException e) {
        }
    }

    /**
     * This method is called when the "Application/Load" menu item is selected. It
     * opens a pop-up window used to load an existing dictionary and set it as
     * active.
     */
    @FXML
    private void handleLoad() {
        createPopUpWindow("../fxml/load.fxml", "Load");
    }

    /**
     * This method is called when the "Application/Create" menu item is selected. It
     * opens a pop-up window used to create a new dictionary using book descriptions
     * from the Open Library Works API.
     */
    @FXML
    private void handleCreate() {
        createPopUpWindow("../fxml/create.fxml", "Create");
    }

    /**
     * This method is called when the "Application/Exit" menu item is selected. It
     * exits the application.
     */
    @FXML
    private void handleExit() {
        Stage stage = (Stage) wordLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is called when the "Details/Dictionary" menu item is selected. It
     * creates a pop-up window that shows stats for the active dictionary (how many
     * words have 6, 7-9 and 10+ letters).
     */
    @FXML
    private void handleDictionary() {
        try {
            createPopUpWindow("../fxml/dictionary.fxml", "Dictionary");
        }
        catch (NullPointerException e) {
            showNoDictionaryLoadedYetMessage();
        }
    }

    /**
     * This method is called when the "Details/Rounds" menu item is selected. It
     * opens a pop-up window that shows stats for the 5 most recent games (chosen
     * word, number of choices, winner).
     * 
     */
    @FXML
    private void handleRounds() {
        createPopUpWindow("../fxml/rounds.fxml", "Rounds");
    }

    /**
     * This method is called when the "Details/Solution" menu item is selected. It
     * unveils the hidden word and the game is considered lost.
     */
    @FXML
    private void handleSolution() {
        try {
            game.setGameLost(true); /* This simulates a loss */
            game.setWrongChoices(6); /* This makes sure the correct hangman image shows up */
            this.handleSubmit(); /* This dummy submit is guaranteed to be a game loss because of the game.gameLost variable */
        }
        catch (NullPointerException e) {
            showNoDictionaryLoadedYetMessage();
        }
    }

    /**
     * This method is called when the "Submit" button is pressed. It submits the
     * choice and refreshes the UI accordingly.
     */
    @FXML
    private void handleSubmit() {
        char letter;
        /* Get the letter... */
        try {
            letter = letterChoiceComboBox.getValue().charAt(0);
        }
        catch (NullPointerException e) {
            return; /* Submit without started game */
        }
        /* ...and the position */
        int position = Integer.parseInt(positionChoiceComboBox.getValue());
        /* Make the choice */
        int returnValue = game.makeChoice(letter, position);

        /* Change the image if the choice was wrong */
        try {
            switch (returnValue) {
            /* if returnValue = {-1, -2}, then the choice was wrong... */
            case -1:
            case -2:
                /* ...so change the image accordingly: wrong{i}.png -> wrong{i+1}.png */
                String hangmanImageString = new File(String.format("images/wrong%d.png", game.getWrongChoices()))
                        .toURI().toURL().toString();
                Image hangman = new Image(hangmanImageString, 400, 400, false, true);
                hangmanImageView.setImage(hangman);
                break;
            /* if returnValue = 2, then the player has won... */
            case 2:
                /* ...so change the image accordingly: victory.png */
                String winningHangmanImageString = new File("images/victory.png").toURI().toURL().toString();
                Image winningHangman = new Image(winningHangmanImageString, 400, 400, false, true);
                hangmanImageView.setImage(winningHangman);
                break;
            }
        }
        catch (MalformedURLException e) {
        }

        /* if the game was won or lost, then save it (if it hasn't been already saved) */
        if (!gameAlreadySaved && (game.getGameWon() || game.getGameLost())) {
            saveGameInfo();
            gameAlreadySaved = true;
        }

        /* Refresh the UI again */
        refreshTotalPoints();
        refreshCorrectChoices();
        refreshWordLabel();
        refreshAvailableLetters();
    }

    /**
     * This method creates a pop-up window from an FXML file. The pop-up window must
     * be closed in order to return to the main window.
     * 
     * @param fxmlFilePath a string representing the path to the FXML file.
     * @param windowTitle  the title given to the pop-up window
     */
    @FXML
    private void createPopUpWindow(String fxmlFilePath, String windowTitle) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        }
        catch (IOException e) {
            return;
        }
        Controller controller = fxmlLoader.getController();
        /* Pass the main controller (this) to the controller of the pop-up window, to transfer data between controllers */
        controller.setMainController(this);

        /* Handle controllers that don't have any buttons and must render the window immediately upon creation */
        if (windowTitle == "Dictionary") {
            /* Turn the Controller into a DictionaryController and render the "Dictionary" window UI */
            DictionaryController _controller = (DictionaryController) controller;
            _controller.renderDictionaryUI();
        }
        if (windowTitle == "Rounds") {
            /* Turn the Controller into a RoundsController and render the "Rounds" window UI */
            RoundsController _controller = (RoundsController) controller;
            _controller.renderRoundsUI();
        }

        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        /* This makes it so you can't use the main window while the pop-up window is still open */
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * This method shows an info message when there is no active dictionary and the
     * user performs actions that require one.
     */
    @FXML
    private void showNoDictionaryLoadedYetMessage() {
        wordLabel.setText("You haven't loaded a dictionary yet.\nSelect Application/Load to load a dictionary first.");
        wordLabel.setStyle("-fx-text-fill: RED");
    }

    /**
     * This method saves the current game info to the PastGamesInfo object. The game
     * info can then be shown on the "Rounds" table.
     */
    private void saveGameInfo() {
        pastGamesInfo.addGameInfo(game.getChosenWord(),
                String.valueOf(game.getCorrectChoices() + game.getWrongChoices()),
                game.getGameWon() ? "You" : "Computer");
    }

    /**
     * This method refreshes the number of correct choices the player has made in
     * the UI.
     */
    @FXML
    private void refreshCorrectChoices() {
        try {
            int gameTotalChoices = game.getWrongChoices() + game.getCorrectChoices();
            int correctChoicePercentage = 100 * game.getCorrectChoices() / (gameTotalChoices);
            correctChoicesNumberLabel.setText(String.valueOf(correctChoicePercentage) + "%");
        }
        catch (ArithmeticException e) { /* Division with zero */
            correctChoicesNumberLabel.setText("---");
        }
    }

    /**
     * This method refreshes the available letters for each position (in the UI).
     */
    @FXML
    private void refreshAvailableLetters() {
        StringBuilder stringBuilder = new StringBuilder("Available letters in each position:" + "\n\n");

        int i = 0;
        for (ArrayList<Tuple> letterList : game.getListOfAvailableLetters()) {
            stringBuilder.append("Position " + String.valueOf(i) + ": "); /* e.g. Position 1: */
            for (Tuple t : letterList) {
                if (t.getProbability() != 0) { /* Display only letters having a probability != 0 */
                    stringBuilder.append(t.getCharacter() + " "); /* e.g. Position 1: Q G S H ... */
                }
            }
            stringBuilder.append("\n");
            i++;
        }

        /*
         * The resulting string is something like:
         * 
         * Position 0: Q E K F J L D S 
         * Position 1: S L D G K J A P I O
         * Position 2: A S K L J F 
         * ...
         */
        availableLettersLabel.setText(stringBuilder.toString());
    }

    /**
     * This method refreshes the hidden word label in the UI, revealing letters if
     * necessary.
     */
    @FXML
    private void refreshWordLabel() {
        StringBuilder sb1 = new StringBuilder(); /* Holds the indexes e.g. 00 01 02 03 */
        StringBuilder sb2 = new StringBuilder(); /* Holds the letters e.g. H _ L L _ */

        int i = 0;
        for (String s : game.getFilledLetters()) {
            String space = (i == 0 ? " " : "  ");
            sb1.append(String.format("%02d", i) + " ");
            sb2.append(space + s.charAt(0));
            i++;
        }

        /* Clear the label's text */
        wordLabel.setText("");

        if (game.getGameWon()) {
            wordLabel.setText(wordLabel.getText() + "You won!" + "\n");
            wordLabel.setStyle("-fx-text-fill: GREEN");
        }
        else if (game.getGameLost()) {
            wordLabel.setText(wordLabel.getText() + "You lost! The word was: " + game.getChosenWord() + "\n");
            wordLabel.setStyle("-fx-text-fill: RED");
        }

        wordLabel.setText(wordLabel.getText() + sb1.toString() + "\n" + sb2.toString());
    }

    /**
     * This method refreshes the total points earned by the player in the UI.
     */
    @FXML
    private void refreshTotalPoints() {
        totalPointsNumberLabel.setText(String.valueOf(game.getPoints()));
    }

    @FXML
    private Label availableWordsNumberLabel; /* A label holding the number of available words in the active dictionary */
    @FXML
    private Label totalPointsNumberLabel; /* A label holding the total number of points the player has in this round */
    @FXML
    private Label correctChoicesNumberLabel; /* A label holding the percentage of correct-to-total choices */
    @FXML
    private ImageView hangmanImageView; /* Holds the image of the bound-to-be hangman (although who would actually hang Joey Tribbiani?) */
    @FXML
    private Label wordLabel; /* A label holding the hidden word (and some info on how to start the game) */
    @FXML
    private Label availableLettersLabel; /* A label holding the available letters for each position, sorted by their probability descending */
    @FXML
    private ComboBox<String> letterChoiceComboBox; /* A ComboBox holding the player's choice of letter */
    @FXML
    private ComboBox<String> positionChoiceComboBox; /* A ComboBox holding the player's choice of position */
    @FXML
    private Button submitChoiceButton; /* A button for submitting the player's choice */

    private Game game; /* The active game instance */
    private Dictionary activeDictionary; /* The active dictionary */
    private PastGamesInfo pastGamesInfo; /* Info for the 5 most recent games. Does not persist between restarts */
    private boolean gameAlreadySaved; /* Checks if the current game has already been saved (prevents duplicate entries in the "Rounds" table */

}
