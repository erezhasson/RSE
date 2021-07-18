package ui.actionwindow;

import datafiles.dto.RecordBookDto;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.Collection;

import static ui.app.ResourcesConstants.*;

public class AdminInfoWindowController extends UserInfoWindowController implements ActionWindowInterface {
    @FXML
    private ImageView userImage;

    @FXML
    private Label userNameLabel;

    @FXML
    private TableView<RizpaCommandDto> transactionsTable;

    @FXML
    private TableColumn<RizpaCommandDto, String> symbolColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> timeColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> commandTypeColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> directionColumn;

    @FXML
    private TableColumn<RizpaCommandDto, Number> amountColumn;

    @FXML
    private TableColumn<RizpaCommandDto, Number> priceColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> sellerColumn;

    @FXML
    private TableColumn<RizpaCommandDto, String> buyerColumn;

    @FXML
    private ComboBox<String> stockChooserComponent;

    @FXML
    private Label showCompletedLabel;

    @FXML
    private Label showPendingLabel;

    private Collection<StockDto> systemStocks;

    @FXML
    public void initialize() {
        symbolColumn.setCellValueFactory(cellData -> cellData.getValue().stockSymbolProperty());
        directionColumn.setCellValueFactory(cellData -> cellData.getValue().directionProperty());
        commandTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        sellerColumn.setCellValueFactory(cellData -> cellData.getValue().sellerProperty());
        buyerColumn.setCellValueFactory(cellData -> cellData.getValue().buyerProperty());
        actionWindowComponent.getStylesheets().add(DEFAULTADMINWINDOW_CSS_RESOURCE);
    }

    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
        AdminWindowInfo adminInfo = (AdminWindowInfo) windowInfo;

        systemStocks = adminInfo.getStocks();
        for (StockDto stock : systemStocks) {
            stockChooserComponent.getItems().add(stock.getSymbol());
        }
        userImage.setImage(adminInfo.getProfileImage());
        startImageAnimation();
    }

    protected void startImageAnimation() {
        UserInfoWindowController.startImageAnimation(userImage);
    }

    @FXML
    void displayCompletedCommands(MouseEvent event) {
        ObservableList<RizpaCommandDto> completedCommands = buildCommandsList(true);
        loadStockCommands(completedCommands);
    }

    private ObservableList<RizpaCommandDto> buildCommandsList(boolean completedCommandsOnly) {
        ObservableList<RizpaCommandDto> commands = FXCollections.observableArrayList();

        for (StockDto stock : systemStocks) {
            RecordBookDto book = stock.getRecords();

            if (completedCommandsOnly) {
                commands.addAll(book.getCompleted());
            }
            else {
                commands.addAll(book.getBuying());
                commands.addAll(book.getSelling());
            }
        }

        return commands;
    }

    @FXML
    void displayPendingCommands(MouseEvent event) {
        ObservableList<RizpaCommandDto> pendingCommands = buildCommandsList(false);
        loadStockCommands(pendingCommands);
    }

    @Override
    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            actionWindowComponent.getStylesheets().add(DARKADMINWINDOW_CSS_RESOURCE);
            CSS_URL = DARKADMINWINDOW_CSS_RESOURCE;
        }
        else {
            if (CSS_URL != null) {
                actionWindowComponent.getStylesheets().removeAll(DARKADMINWINDOW_CSS_RESOURCE);
            }
            CSS_URL = null;
        }
    }

    private void loadStockCommands(ObservableList<RizpaCommandDto> commands) {
        transactionsTable.setItems(commands);
    }
}
