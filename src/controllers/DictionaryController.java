package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DictionaryController implements Controller {
    @FXML
    private GridPane gridPane;
    @FXML
    private Label words6Label;
    @FXML
    private Label words6Number;
    @FXML
    private Label words7To9Label;
    @FXML
    private Label words7To9Number;
    @FXML
    private Label words10PlusLabel;
    @FXML
    private Label words10PlusNumber;
    @FXML
    private MainController mainController;

    /* Empty */
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }

    public void refreshDictionaryUI() {
        System.out.println(mainController);
        System.out.println(mainController.activeDictionary);
        System.out.println(mainController.activeDictionary.words6);
        words6Number.setText(String.valueOf(mainController.activeDictionary.words6));
        System.out.println(words7To9Number);
        words7To9Number.setText(String.valueOf(mainController.activeDictionary.words7To9));
        words10PlusNumber.setText(String.valueOf(mainController.activeDictionary.words10Plus));
    }
}
