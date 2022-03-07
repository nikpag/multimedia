package src.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import src.classes.Dictionary;
import src.classes.Game;
import src.exceptions.DictionaryNotFoundException;

public class LoadController implements Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label dictionaryIDLabel;
    @FXML
    private TextField dictionaryIDTextField;
    @FXML
    private Button loadButton;
    @FXML
    private Label errorMessageLabel; /* maybe errorMessageLabel is not the best name, try plain messageLabel */

    private MainController mainController;

    // @FXML
    // public List<String> loadDictionary(ActionEvent event) throws IOException {
    // String dictionaryID = dictionaryIDTextField.getText();

    // Path path = Paths.get(String.format("medialab/hangman_%s.txt",
    // dictionaryID));
    // List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

    // FXMLLoader mainLoader = new
    // FXMLLoader(getClass().getResource("../fxml/main.fxml"));
    // Parent root = mainLoader.load();

    // MainController mainController = mainLoader.getController();
    // return new ArrayList<String>(); /* this is dummy return; change it! */
    // }

    // public LoadController() {
    // }

    // public LoadController(MainController mc) {
    // mainController = mc;
    // }

    @FXML
    public void loadDictionary() throws IOException, DictionaryNotFoundException {
        String dictionaryID = dictionaryIDTextField.getText();
        Path path = Paths.get(String.format("medialab/hangman_%s.txt", dictionaryID));

        try {
            List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
            Dictionary dictionary = new Dictionary(lines);
            mainController.setActiveDictionary(dictionary);
            errorMessageLabel.setText("Dictionary loaded successfully");
            errorMessageLabel.setStyle("-fx-text-fill: green");
            mainController.availableWordsNumberLabel.setText(String.valueOf(lines.size()));
        }
        catch (NoSuchFileException e) {
            errorMessageLabel.setText("The dictionary doesn't exist");
            errorMessageLabel.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }
}
