package ui.task;

import exceptions.name.NameException;
import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;
import exceptions.user.UserException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import ui.actionwindow.ActionWindowController;
import ui.alerts.ErrorAlert;
import ui.alerts.ExceptionAlert;

import java.util.Optional;

import static ui.app.ResourcesConstants.*;

public class TaskWindowController extends ActionWindowController {
    @FXML
    private GridPane actionWindowComponent;

    @FXML
    private ProgressBar progressBarComponent;

    @FXML
    private Label taskMessageLabel;

    @FXML
    private Label progressBarLabel;

    private SimpleStringProperty taskMessage;

    private SimpleLongProperty totalStocks;

    private SimpleLongProperty totalUsers;

    public TaskWindowController() {
        this.taskMessage = new SimpleStringProperty();
        this.totalStocks = new SimpleLongProperty(0);
        this.totalUsers = new SimpleLongProperty(0);
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.taskMessageLabel.textProperty().unbind();
        this.progressBarLabel.textProperty().unbind();
        this.progressBarComponent.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
    }

    @Override
    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            actionWindowComponent.getStylesheets().add(DARKTASKWINDOW_CSS_RESOURCE);
            CSS_URL = DARKTASKWINDOW_CSS_RESOURCE;
        }
        else {
            if (CSS_URL != null) {
                actionWindowComponent.getStylesheets().remove(DARKTASKWINDOW_CSS_RESOURCE);
            }
            CSS_URL = null;
        }
    }

    @FXML
    private void initialize() {
        taskMessageLabel.textProperty().bind(Bindings.format("%,d", taskMessage));
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        progressBarComponent.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressBarLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });

        aTask.setOnFailed(evt -> {
            if (aTask.getException() instanceof SymbolException || aTask.getException() instanceof StockException) {
                ErrorAlert alert = new ErrorAlert("Error while generating system from file",
                        "Error while importing stocks:", aTask.getException().getMessage());
                alert.showAndWait();
            } else if (aTask.getException() instanceof NameException || aTask.getException() instanceof UserException){
                ErrorAlert alert = new ErrorAlert("Error while generating system from file",
                        "Error while importing user:", aTask.getException().getMessage());
                alert.showAndWait();
            } else {
                ExceptionAlert alert = new ExceptionAlert("Error while generating system from file", aTask.getException());
                alert.showAndWait();
            }
            mainAppController.toggleComponents(true);
        });
    }

    public long getTotalStocks() {
        return totalStocks.get();
    }

    public SimpleLongProperty totalStocksProperty() {
        return totalStocks;
    }

    public long getTotalUsers() {
        return totalUsers.get();
    }

    public SimpleLongProperty totalUsersProperty() {
        return totalUsers;
    }
}
