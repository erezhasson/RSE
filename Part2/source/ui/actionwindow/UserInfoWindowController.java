package ui.actionwindow;

import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import datafiles.dto.UserStocksDto;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import ui.app.MainAppController;

import java.io.File;

import static ui.app.ResourcesConstants.*;

public class UserInfoWindowController extends ActionWindowController implements ActionWindowInterface {
    @FXML
    private ImageView userImage;

    @FXML
    private Label userNameLabel;

    @FXML
    private TableView<StockTableRow> transactionsTable;

    @FXML
    private TableColumn<StockTableRow, String> symbolColumn;

    @FXML
    private TableColumn<StockTableRow, Number> amountColumn;

    @FXML
    private TableColumn<StockTableRow, Number> priceColumn;

    @FXML
    private Label investValLabel;

    @FXML
    public void initialize() {
        symbolColumn.setCellValueFactory(cellData -> cellData.getValue().stockSymbolProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().stockAmountProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().stockPriceProperty());
        actionWindowComponent.getStylesheets().add(DEFAULTUSERWINDOW_CSS_RESOURCE);
    }

    @FXML
    protected void changeUserImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg");

        fileChooser.getExtensionFilters().add(fileExtensions);
        File image = fileChooser.showOpenDialog(null);
        if (image != null) {
            Image newImage = new Image(image.toURI().toString());
            MainAppController.setUserImage(userNameLabel.getText(), newImage);
            userImage.setImage(newImage);
        }
    }

    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
        UserWindowInfo userWindowInfo = (UserWindowInfo)windowInfo;
        UserDto userInfo = userWindowInfo.getUser();

        userImage.setImage(MainAppController.getUserImage(userInfo.getName()));
        userNameLabel.setText(userInfo.getName());
        investValLabel.setText("Total Investments Value: " + userInfo.investValue());
        loadStockInfo(buildTableRows(userWindowInfo));
        startImageAnimation(userImage);
    }

    static void startImageAnimation(ImageView userImage) {
        ScaleTransition transition = new ScaleTransition();

        transition.setDuration(Duration.seconds(1));
        transition.setNode(userImage);
        transition.setByX(10f);
        transition.setToX(0f);
        transition.setCycleCount(2);
        transition.setAutoReverse(true);
        transition.play();


        userImage.setOnMouseEntered((e) -> transition.pause());
        userImage.setOnMouseExited((e) -> transition.play());
    }

    private ObservableList<StockTableRow> buildTableRows(UserWindowInfo userWindowInfo) {
        UserStocksDto userStocks = userWindowInfo.getUser().getStockStructure();
        ObservableList<StockTableRow> tableRows = FXCollections.observableArrayList();

        for (StockDto stock : userStocks.getAllStocks()) {
            tableRows.add(new StockTableRow(stock.getSymbol(),
                    userStocks.getStockAmount(stock.getSymbol()), stock.getPrice()));
        }

        return tableRows;
    }

    @Override
    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            actionWindowComponent.getStylesheets().add(DARKUSERWINDOW_CSS_RESOURCE);
            CSS_URL = DARKUSERWINDOW_CSS_RESOURCE;
        }
        else {
            if (CSS_URL != null) {
                actionWindowComponent.getStylesheets().clear();
                actionWindowComponent.getStylesheets().add(DEFAULTUSERWINDOW_CSS_RESOURCE);
            }
            CSS_URL = null;
        }
    }

    private void loadStockInfo(ObservableList<StockTableRow> rows) {
        transactionsTable.setItems(rows);
    }
}
