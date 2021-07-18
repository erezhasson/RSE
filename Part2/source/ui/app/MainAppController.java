package ui.app;

import datafiles.commands.BuyingCommand;
import datafiles.commands.CommandDetails;
import datafiles.commands.RizpaCommand;
import datafiles.dto.*;
import exceptions.command.CommandException;
import exceptions.stock.StockException;
import exceptions.stock.StockNotFoundException;
import exceptions.symbol.SymbolException;
import exceptions.user.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import logicinterface.RizpaEngine;
import logicinterface.RizpaInterface;
import ui.actionwindow.*;
import ui.alerts.CommandAlert;
import ui.alerts.ErrorAlert;
import ui.graphcomponent.graphtabs.GraphTabsController;
import ui.menubar.MenuBarController;
import ui.stockmenu.StockMenuController;
import ui.task.TaskWindowController;
import ui.usermenu.UserMenuController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ui.app.ResourcesConstants.*;

public class MainAppController {

    private static final HashMap<String, Image> User2Image;
    private static final RizpaInterface engine;
    private boolean isDarkMode;

    @FXML
    BorderPane mainComponent;
    @FXML
    MenuBarController menuBarComponentController;
    @FXML
    private HBox menuBarComponent;
    @FXML
    private Accordion stockMenuComponent;
    @FXML
    StockMenuController stockMenuComponentController;
    @FXML
    private TabPane userMenuComponent;
    @FXML
    UserMenuController userMenuComponentController;
    @FXML
    private GridPane actionWindowComponent;
    @FXML
    ActionWindowController actionWindowComponentController;
    @FXML
    private GridPane centerGridComponent;
    @FXML
    private GraphTabsController graphTabsComponentController;
    @FXML
    private ComboBox<String> graphTabsComponent;
    @FXML
    private ImageView refreshGraphComponent;
    @FXML
    private LineChart<String, Double> stockGraphComponent;
    @FXML
    private ImageView dollarImage;

    @FXML
    public void initialize() {
        if (menuBarComponentController != null && stockMenuComponentController != null &&
                userMenuComponentController != null && actionWindowComponentController != null &&
                graphTabsComponentController != null) {
            menuBarComponentController.setMainController(this);
            stockMenuComponentController.setMainController(this);
            userMenuComponentController.setMainController(this);
            actionWindowComponentController.setMainController(this);
            graphTabsComponentController.setMainController(this);
        }
    }

    static {
        engine = new RizpaEngine();
        User2Image = new HashMap<>();
    }

    public static Image getUserImage(String userName) {
        return User2Image.get(userName);
    }

    public static void setUserImage(String userName, Image newImage) {
        User2Image.put(userName, newImage);
    }

    public void setStockMenuController(StockMenuController stockMenuController) {
        this.stockMenuComponentController = stockMenuController;
        stockMenuController.setMainController(this);
    }

    public void setMenuBarController(MenuBarController menuBarController) {
        this.menuBarComponentController = menuBarController;
        menuBarController.setMainController(this);
    }

    public void setUserBarController(UserMenuController userMenuController) {
        this.userMenuComponentController = userMenuController;
        userMenuController.setMainController(this);
    }

    public void setActionWindowComponentController(ActionWindowController actionWindowController) {
        this.actionWindowComponentController = actionWindowController;
        actionWindowController.setMainController(this);
    }

    public void setGraphTabsComponentController(GraphTabsController graphTabsController) {
        this.graphTabsComponentController = graphTabsController;
        graphTabsComponentController.setMainController(this);
    }

    public void createSystemSaveFile(File selectedFile) {
        try {
            engine.saveSystemInfoToStream(new FileOutputStream(selectedFile));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("System File Created");
            alert.setContentText("Created Save File Successfully");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (IOException ignored) {
        }
    }

    public void loadSystemFile(File file) {
        toggleComponents(false);

        mountActionWindow(null, TASKWINDOW_FXML_RESOURCE);
        if (actionWindowComponentController instanceof TaskWindowController) {
            TaskWindowController taskWindowController = (TaskWindowController) actionWindowComponentController;
            engine.collectDataFromFile(taskWindowController, file.getAbsolutePath(),
                    taskWindowController.totalStocksProperty()::set,
                    taskWindowController.totalUsersProperty()::set,
                    () -> {
                        buildStockMenuComponent();
                        buildUserMenuComponent();
                        initComponents();
                        toggleComponents(true);
                    }
            );
        }
    }

    public void buildUserMenuComponent() {
        Collection<UserDto> users = null;
        try {
            users = engine.getAllUsers();
        } catch (StockNotFoundException ignored) {
        }

        userMenuComponentController.clearAllMenuItems();
        userMenuComponentController.createMenuItem("Admin");
        setUserImage("Admin", new Image(USERDEFAULT_IMAGE_RESOURCE));
        for (UserDto user : users) {
            String userName = user.getName();

            userMenuComponentController.createMenuItem(userName);
            setUserImage(userName, new Image(USERDEFAULT_IMAGE_RESOURCE));
        }
    }

    public void buildStockMenuComponent() {
        Collection<StockDto> stocks = engine.getAllStocks();

        stockMenuComponentController.clearAllMenuItems();
        graphTabsComponentController.clearAllMenuItems();
        for (StockDto stock: stocks) {
            String symbol = stock.getSymbol().toUpperCase();
            stockMenuComponentController.createMenuItem(symbol);
            stockMenuComponentController.loadItemsStyleSheet(isDarkMode);
            graphTabsComponentController.createStockTab(symbol);
        }
    }

    public void initComponents() {
        mainComponent.getCenter().setDisable(false);
        mainComponent.getBottom().setDisable(false);
        initActionWindow();
        menuBarComponentController.enableAccountBtn();
    }

    public void requestStockWindowInfo(String stockSymbol) {
        try {
            StockDto stock = engine.getStock(stockSymbol);
            actionWindowComponentController = new StockInfoWindowController();
            mountActionWindow(stock, STOCKINFOWINDOW_FXML_RESOURCE);
        } catch (StockNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void requestCommandWindowInfo(String stockSymbol, String issuedCommandUser) {
        if (!issuedCommandUser.equals("Admin")) {
            try {
                StockDto stock = engine.getStock(stockSymbol);
                actionWindowComponentController = new CommandInfoWindowController();
                mountActionWindow(stock, COMMANDINFOWINDOW_FXML_RESOURCE);
            } catch (StockNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void requestUserWindowInfo() {
        try {
            UserDto user = engine.getUser(userMenuComponent.getSelectionModel().getSelectedItem().getText());
            UserWindowInfo windowInfo = new UserWindowInfo(user, getUserImage(user.getName()));
            actionWindowComponentController = new UserInfoWindowController();
            mountActionWindow(windowInfo, USERINFOWINDOW_FXML_RESOURCE);
        } catch (StockNotFoundException | UserNotFoundException ignored) {
        }
    }

    public void requestAdminWindowInfo() {
        AdminWindowInfo windowInfo = new AdminWindowInfo(engine.getAllStocks(), getUserImage("Admin"));
        actionWindowComponentController = new AdminInfoWindowController();
        mountActionWindow(windowInfo, ADMININFOWINDOW_FXML_RESOURCE);
    }

    public <T> void mountActionWindow(T windowInfo, String windowURL) {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(windowURL);
        loader.setLocation(url);
        int row = 1, col = 1;

        try {
            centerGridComponent.getChildren().remove(actionWindowComponent);
            actionWindowComponent = loader.load();
            actionWindowComponentController = loader.getController();
            actionWindowComponentController.setMainController(this);
            actionWindowComponentController.loadStyleSheet(isDarkMode);
            actionWindowComponentController.loadActionWindowInfo(windowInfo);
            centerGridComponent.add(actionWindowComponent, row, col, 3, 3);
        } catch (IOException ignored) {
        }
    }

    public void applyRizpaCommand(String stockSymbol, String type, String price, String amount, String direction, String issuedCommandUser) {
        try {
            CommandDetails commandDetails = new CommandDetails(stockSymbol, type, price, amount, direction);
            RizpaCommand newCommand = engine.createNewCommand(commandDetails, issuedCommandUser);
            RizpaCommandDto commandDto = newCommand instanceof BuyingCommand ?
                    new BuyingCommandDto(newCommand.getStockSymbol(), newCommand.getType(), newCommand.getPrice(),
                            newCommand.getAmount(), newCommand.getTimestamp(), issuedCommandUser, "None") :
                    new SellingCommandDto(newCommand.getStockSymbol(), newCommand.getType(), newCommand.getPrice(),
                            newCommand.getAmount(), newCommand.getTimestamp(), issuedCommandUser, "None");

            displayNewCommand(newCommand, commandDto);
        } catch (CommandException | SymbolException | StockException | UserNotFoundException e) {
            ErrorAlert alert = new ErrorAlert("Error Creating Command",
                    "Error while creating new Rizpa command: ",
                    e.getMessage());
            alert.showAndWait();
        }
    }

    private void displayNewCommand(RizpaCommand newCommand, RizpaCommandDto commandDto) {
        Alert alert = new CommandAlert("Confirm applying new command", commandDto);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().getText().equals("Apply")){
            operateCommand(newCommand, commandDto);
        }
    }

    private void operateCommand(RizpaCommand newCommand, RizpaCommandDto commandDto) {
        Collection<RizpaCommandDto> newDeals =  engine.operateCommand(newCommand);

        if (newDeals.size() != 0) {
            if (actionWindowComponentController instanceof CommandInfoWindowController) {
                CommandInfoWindowController commandInfoWindowController = (CommandInfoWindowController) actionWindowComponentController;
                commandInfoWindowController.startDealAnimation();
            }
        }
        displayCommandDetails(commandDto, newDeals);
    }



    private void displayCommandDetails(RizpaCommandDto newCommand, Collection<RizpaCommandDto> deals) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        StringBuilder alertBody = new StringBuilder();

        if (deals.size() != 0) {
            alertBody.append(newCommand.toString()).append("This command has been executed successfully.\n" +
                    "The following deals have been completed after executing command:\n");

            for (RizpaCommandDto deal : deals) {
                alertBody.append(deal.toString()).append("\n");
            }
        }
        else {
            alertBody.append(newCommand.toString()).append(
                    "This command has been not executed and have been recorded in stock's pending commands.");
        }

        a.setTitle("Created new Rizpa command");
        a.setHeaderText("Successfully Created New Command!");
        a.setContentText(alertBody.toString());
        a.show();
    }

    public String getCurrentUserName() {
        int tabIndex = userMenuComponent.getSelectionModel().getSelectedIndex();
        Tab userTab = userMenuComponent.getTabs().get(tabIndex);

        return userTab.getText();
    }

    public void refreshGraph() {
        String stockSymbol = graphTabsComponent.getValue();

        if (stockSymbol != null) {
            try {
                StockDto stock = engine.getStock(stockSymbol);
                List<RizpaCommandDto> deals = stock.getRecords().getCompleted();
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                XYChart.Series<String, Double> series = new XYChart.Series<>();

                stockGraphComponent.getData().clear();
                xAxis.setLabel("Time");
                yAxis.setLabel("Price");
                for (RizpaCommandDto deal : deals) {
                    series.getData().add(new XYChart.Data<>(deal.getTimestamp(), deal.getPrice()));
                }
                stockGraphComponent.getData().add(series);
            } catch (StockNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void disableOpearteCommandLabels() {
        stockMenuComponentController.disableOperateCommandLabels();
    }

    public void enableOpearteCommandLabels() {
        stockMenuComponentController.enableOperateCommandLabels();
    }

    public void toggleComponents(boolean isActive) {
        menuBarComponent.disableProperty().setValue(!isActive);
        userMenuComponent.disableProperty().setValue(!isActive);
        stockMenuComponent.disableProperty().setValue(!isActive);
        mainComponent.getBottom().disableProperty().setValue(!isActive);
    }

    public void initActionWindow() {
        mountActionWindow(null, ACTIONWINDOW_FXML_RESOURCE);
    }

    public void changeAppMode(boolean darkMode) {
        actionWindowComponentController.loadStyleSheet(darkMode);
        graphTabsComponentController.loadStyleSheet(darkMode);
        menuBarComponentController.loadStyleSheet(darkMode);
        stockMenuComponentController.loadStyleSheet(darkMode);
        stockMenuComponentController.loadItemsStyleSheet(darkMode);
        userMenuComponentController.loadStyleSheet(darkMode);
        if (darkMode) {
            mainComponent.getStylesheets().add(DARKAPP_CSS_RESOURCE);
        }
        else {
            mainComponent.getStylesheets().remove(DARKAPP_CSS_RESOURCE);
        }
        isDarkMode = darkMode;
    }
}


