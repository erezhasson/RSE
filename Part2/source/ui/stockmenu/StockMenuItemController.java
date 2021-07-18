package ui.stockmenu;

import datafiles.dto.StockDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

public class StockMenuItemController {

    private StockMenuController stockMenuController;

    @FXML
    private TitledPane mainComponent;

    @FXML
    private Label stockInfoLabel;

    @FXML
    private Label newCommandLabel;

    public void setMainController(StockMenuController stockMenuController) {
        this.stockMenuController = stockMenuController;
    }

    public void setStockName(String symbol) {
        mainComponent.setText(symbol);
    }

    @FXML
    void requestStockInfo(MouseEvent event) {
        stockMenuController.requestStockInfo(mainComponent.getText());
    }

    @FXML
    void requestCommandInfo(MouseEvent event) {
        stockMenuController.requestCommandInfo(mainComponent.getText());
    }

    public void disableOperateCommandLabel() {
        newCommandLabel.disableProperty().setValue(true);
    }

    public void enableOperateCommandLabel() {
        newCommandLabel.disableProperty().setValue(false);
    }
}
