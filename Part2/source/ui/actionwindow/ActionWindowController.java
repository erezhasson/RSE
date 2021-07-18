package ui.actionwindow;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ui.app.MainAppController;

import static ui.app.ResourcesConstants.DARKAPP_CSS_RESOURCE;

public class ActionWindowController implements ActionWindowInterface{
    protected MainAppController mainAppController;

    protected String CSS_URL;

    @FXML
    protected GridPane actionWindowComponent;

    @FXML
    private Label initMessageLabel;

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    public void setActionWindowComponent(GridPane actionWindowComponent) {
        this.actionWindowComponent = actionWindowComponent;
    }

    public void initWindow() {
        initMessageLabel.setText("");
    }

    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
        initWindow();
    }

    public void loadStyleSheet(boolean isDarkMode) {
    }
}
