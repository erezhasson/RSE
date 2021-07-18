package ui.graphcomponent.graphtabs;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import ui.app.MainAppController;

import static ui.app.ResourcesConstants.DARKADMINWINDOW_CSS_RESOURCE;
import static ui.app.ResourcesConstants.DARKGRAPHTABSRWINDOW_CSS_RESOURCE;

public class GraphTabsController {
    private MainAppController mainAppController;

    @FXML
    private ComboBox<String> tabsComponent;

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    @FXML
    public void initialize() {
    }

    public void clearAllMenuItems() {
        if (!tabsComponent.getItems().isEmpty()) {
            tabsComponent.getItems().clear();
        }
    }

    public void createStockTab(String symbol) {
        tabsComponent.getItems().add(symbol);
    }

    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            tabsComponent.getStylesheets().add(DARKGRAPHTABSRWINDOW_CSS_RESOURCE);
        }
        else {
            tabsComponent.getStylesheets().remove(DARKGRAPHTABSRWINDOW_CSS_RESOURCE);
        }

    }
}
