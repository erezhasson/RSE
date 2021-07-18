package ui.usermenu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ui.app.MainAppController;

import static ui.app.ResourcesConstants.ACTIONWINDOW_FXML_RESOURCE;
import static ui.app.ResourcesConstants.DARKUSERMENUWINDOW_CSS_RESOURCE;

public class UserMenuController {
    private MainAppController mainAppController;

    @FXML
    private TabPane userMenuComponent;

    @FXML void initialize() {
        userMenuComponent.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        mainAppController.mountActionWindow("", ACTIONWINDOW_FXML_RESOURCE);
                        if (t1 != null && t1.getText().equals("Admin")) {
                            mainAppController.disableOpearteCommandLabels();
                        }
                        else if (t1 != null){
                            mainAppController.enableOpearteCommandLabels();
                        }
                    }
                }
        );
    }

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    public void clearAllMenuItems() {
        if (!userMenuComponent.getTabs().isEmpty()) {
            userMenuComponent.getTabs().clear();
        }
    }

    public void createMenuItem(String name) {
        Tab newUserTab = new Tab();

        newUserTab.setText(name);
        userMenuComponent.getTabs().add(newUserTab);
    }

    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            userMenuComponent.getStylesheets().add(DARKUSERMENUWINDOW_CSS_RESOURCE);
        }
        else {
            userMenuComponent.getStylesheets().remove(DARKUSERMENUWINDOW_CSS_RESOURCE);
        }
    }
}
