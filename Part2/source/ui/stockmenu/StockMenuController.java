package ui.stockmenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import ui.app.MainAppController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ui.app.ResourcesConstants.*;

public class StockMenuController {
    private MainAppController mainAppController;

    private List<StockMenuItemController> itemControllers;

    @FXML
    private Accordion stockMenuComponent;

    @FXML
    public void initialize() {
        itemControllers = new ArrayList<>();
    }

    public void setMainController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    public void clearAllMenuItems() {
        if (!stockMenuComponent.getPanes().isEmpty()) {
            stockMenuComponent.getPanes().clear();
        }
    }

    public void createMenuItem(String symbol) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(MENUITEM_FXML_RESOURCE);
            loader.setLocation(url);
            TitledPane singleStockItem = loader.load();

            StockMenuItemController singleStockItemController = loader.getController();
            singleStockItemController.setStockName(symbol);
            singleStockItemController.setMainController(this);

            stockMenuComponent.getPanes().add(singleStockItem);
            itemControllers.add(singleStockItemController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestStockInfo(String stockSymbol) {
        mainAppController.requestStockWindowInfo(stockSymbol);
    }

    public void requestCommandInfo(String stockSymbol) {
        mainAppController.requestCommandWindowInfo(stockSymbol, mainAppController.getCurrentUserName());
    }

    public void disableOperateCommandLabels() {
        for (StockMenuItemController controller : itemControllers) {
            controller.disableOperateCommandLabel();
        }
    }

    public void enableOperateCommandLabels() {
        for (StockMenuItemController controller : itemControllers) {
            controller.enableOperateCommandLabel();
        }
    }


    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            stockMenuComponent.getStylesheets().add(DARKSTOCKMENU_CSS_RESOURCE);
        }
        else {
            stockMenuComponent.getStylesheets().remove(DARKSTOCKMENU_CSS_RESOURCE);
        }
    }

    public void loadItemsStyleSheet(boolean darkMode) {
        for (TitledPane pane : stockMenuComponent.getPanes()) {
            if (darkMode && !pane.getStylesheets().contains(DARKSTOCKMENUITEMWINDOW_CSS_RESOURCE)) {
                pane.getStylesheets().add(DARKSTOCKMENUITEMWINDOW_CSS_RESOURCE);
            }
            else if (!darkMode){
                pane.getStylesheets().remove(DARKSTOCKMENUITEMWINDOW_CSS_RESOURCE);
            }
        }
    }
}
