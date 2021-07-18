package ui.actionwindow;

import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static ui.app.ResourcesConstants.*;

public class StockInfoWindowController extends ActionWindowController implements ActionWindowInterface {

    @FXML
    private Label stockNameLabel;

    @FXML
    private Label companyOwnerLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private TableView<RizpaCommandDto> transactionsTable;

    @FXML
    private Label totalTransLabel;

    @FXML
    private Label transCycleLabel;

    @FXML
    private TableColumn<RizpaCommandDto, String> timeColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> commandTypeColumn;

    @FXML
    private TableColumn<RizpaCommandDto, Number> amountColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> directionColumn;

    @FXML
    private TableColumn<RizpaCommandDto, Number> priceColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> sellerColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> buyerColumn;

    @FXML
    private void initialize() {
        directionColumn.setCellValueFactory(cellData -> cellData.getValue().directionProperty());
        commandTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        sellerColumn.setCellValueFactory(cellData -> cellData.getValue().sellerProperty());
        buyerColumn.setCellValueFactory(cellData -> cellData.getValue().buyerProperty());
    }


    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
        StockDto stockInfo = (StockDto)windowInfo;

        stockNameLabel.setText(stockInfo.getSymbol());
        companyOwnerLabel.setText("Company Owner: " + stockInfo.getCompanyName());
        priceLabel.setText("Price: " + stockInfo.getPrice());
        totalTransLabel.setText("Total Transactions: " + stockInfo.getRecords().getCompleted().size());
        transCycleLabel.setText("Transactions Cycle: " + stockInfo.getCycle());
        loadStockCommands(stockInfo.getRecords().getCompleted());
    }

    @Override
    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            actionWindowComponent.getStylesheets().add(DARKSTOCKWINDOW_CSS_RESOURCE);
            CSS_URL = DARKSTOCKWINDOW_CSS_RESOURCE;
        }
        else {
            if (CSS_URL != null) {
                actionWindowComponent.getStylesheets().remove(DARKSTOCKWINDOW_CSS_RESOURCE);
            }
            CSS_URL = null;
        }
    }

    private void loadStockCommands(ObservableList<RizpaCommandDto> commands) {
        transactionsTable.setItems(commands);
    }

}
