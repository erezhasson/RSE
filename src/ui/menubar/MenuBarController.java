package ui.menubar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;
import ui.MainController;

import java.io.File;

public class MenuBarController {
    private MainController mainController;

    @FXML
    private Menu MenuBar;

    @FXML
    private MenuItem import_xml_btn;

    @FXML
    private MenuItem load_system_btn;

    @FXML
    private MenuItem system_save_btn;

    @FXML
    private RadioMenuItem mode_btn;

    @FXML
    private MenuItem about_btn;

    @FXML
    private MenuItem how_to_btn;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void aboutActionListner(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);

        a.setTitle("About");
        a.setHeaderText("RSE - Rizpa Stock Exchange");
        a.setContentText("RSE is a stock exchange platform which allows you to load stock's and user's data to system" +
                " and trade them by input different commands which relates them.\n\n" +
                "Version: 1.0.0.0\n" +
                "Last Updated: 18.05.2021");
        a.show();
    }

    @FXML
    void loadSystemActionHandler(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void saveSystemActionListner(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fc.showSaveDialog(null);
        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void importXMLActionListner(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            mainController.buildStocksFromXMLFile(selectedFile);
        }
    }
}
