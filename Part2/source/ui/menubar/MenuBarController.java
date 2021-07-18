package ui.menubar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import ui.app.MainAppController;

import java.io.File;

import static ui.app.ResourcesConstants.DARKADMINWINDOW_CSS_RESOURCE;
import static ui.app.ResourcesConstants.DARKMENUBAR_CSS_RESOURCE;

public class MenuBarController {
    private MainAppController mainAppController;

    @FXML
    private HBox menuContainer;

    @FXML
    private Menu menuBar;

    @FXML
    private MenuItem load_system_btn;

    @FXML
    private MenuItem system_save_btn;

    @FXML
    private MenuItem about_btn;

    @FXML
    private Button accountBtn;

    @FXML
    private RadioMenuItem dark_mode_btn;

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    @FXML
    void changeAppMode(ActionEvent event) {
        mainAppController.changeAppMode(dark_mode_btn.isSelected());
    }

    @FXML
    public void aboutActionListner(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setTitle("About");
        a.setHeaderText("RSE - Rizpa Stock Exchange");
        a.setContentText("RSE is a stock exchange platform which allows you to load stock's and user's data to system" +
                " and trade them by input different commands which relates them.\n\n" +
                "Version: 1.0.0.0\n" +
                "Last Updated: 04.06.2021");
        a.show();
    }

    @FXML
    public void loadSystemFile(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            mainAppController.loadSystemFile(selectedFile);
        }
    }

    @FXML
    public void saveSystemActionListner(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fc.showSaveDialog(null);
        if (selectedFile != null) {
            mainAppController.createSystemSaveFile(selectedFile);
        }
    }

    @FXML
    public void requestUserWindowInfo(MouseEvent event) {
        if (mainAppController.getCurrentUserName().equals("Admin")) {
            mainAppController.requestAdminWindowInfo();
        }
        else {
            mainAppController.requestUserWindowInfo();
        }

    }

    public void enableAccountBtn() {
        accountBtn.setDisable(false);
    }

    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            menuContainer.getStylesheets().add(DARKMENUBAR_CSS_RESOURCE);
        }
        else {
            menuContainer.getStylesheets().remove(DARKMENUBAR_CSS_RESOURCE);
        }
    }
}
