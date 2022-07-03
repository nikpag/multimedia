package src.controllers;

/**
 * The Controller interface is used for every window in the JavaFX app, thus
 * every window has it's own controller.
 */
public interface Controller {
    /**
     * This function gets the controller of the main window (MainController) and
     * sets it as an attribute of the controller of the pop-up window. This way, we
     * can transfer data from/to the two controllers.
     * 
     * @param controller the controller to be set as the main controller of the
     *                   pop-up window.
     */
    public void setMainController(Controller controller);
}
