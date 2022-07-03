package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This controller is used by the "Dictionary" pop-up window that is opened when
 * the user selects Details-->Dictionary from the menu.
 */
public class DictionaryController implements Controller {
    /**
     * This function is called when the "Dictionary" pop-up window is created. It
     * renders the dictionary stats on the screen.
     */
    @FXML
    public void renderDictionaryUI() {
        words6Number.setText(String.valueOf(mainController.getActiveDictionary().getNumberOfLength6()));
        words7To9Number.setText(String.valueOf(mainController.getActiveDictionary().getNumberOfLength7To9()));
        words10PlusNumber.setText(String.valueOf(mainController.getActiveDictionary().getNumberOfLength10Plus()));
    }

    /* Refer to the Controller interface for more details on this function. */
    public void setMainController(Controller controller) {
        mainController = (MainController) controller;
    }

    @FXML
    private Label words6Number; /* A label holding the number of 6-letter words in the active dictionary */
    @FXML
    private Label words7To9Number; /* A label holding the number of 7-to-9-letter words in the active dictionary */
    @FXML
    private Label words10PlusNumber; /* A label holding the number of 10-or-more-letter words in the active dictionary */

    private MainController mainController; /* The controller of the main window (used for communication between windows) */

}
