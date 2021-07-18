package ui.actionwindow;

import datafiles.dto.StockDto;
import javafx.animation.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ui.alerts.CommandAlert;

import static ui.app.ResourcesConstants.*;

public class CommandInfoWindowController extends ActionWindowController implements ActionWindowInterface {
    @FXML
    private Label operateCommandLabel;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private TextField priceInput;

    @FXML
    private TextField amountInput;

    @FXML
    private RadioButton buyingOption;

    @FXML
    private RadioButton sellingOption;

    @FXML
    private Label priceLabel;

    @FXML
    private Button applyButton;

    @FXML
    private ImageView dollarImage;

    final ToggleGroup commandOptions = new ToggleGroup();

    private String operatedStock;

    @FXML
    void initialize() {
        actionWindowComponent.getStylesheets().add(DEFAULTCOMMANDWINDOW_CSS_RESOURCE);
    }

    @Override
    public <T> void loadActionWindowInfo(T windowInfo) {
        StockDto stockInfo = (StockDto)windowInfo;

        operatedStock = stockInfo.getSymbol();
        operateCommandLabel.setText("Operate Command For " + operatedStock);
        buyingOption.setToggleGroup(commandOptions);
        sellingOption.setToggleGroup(commandOptions);
        commandOptions.selectToggle(buyingOption);
        typeChoiceBox.getItems().add("LMT");
        typeChoiceBox.getItems().add("MKT");
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    if (new_val.equals("LMT")) {
                        priceInput.visibleProperty().setValue(true);
                        priceLabel.visibleProperty().setValue(true);
                    }
                    else {
                        priceInput.visibleProperty().setValue(false);
                        priceLabel.visibleProperty().setValue(false);
                    };
                });
    }

    public void startDealAnimation() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(dollarImage);

        transition.setFromY(0);
        transition.setToY(-20);
        ScaleTransition transition1 = new ScaleTransition(Duration.seconds(2), dollarImage);
        transition1.setFromX(0);
        transition1.setFromY(0);
        transition1.setToX(0.75);
        transition1.setToY(0.75);

        RotateTransition transition2 = new RotateTransition(Duration.seconds(2), dollarImage);
        transition2.setFromAngle(0);
        transition2.setToAngle(360);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), dollarImage);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(transition, transition1, transition2, fadeIn);

        dollarImage.visibleProperty().setValue(true);
        parallelTransition.setOnFinished((event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), dollarImage);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.play();
                }));
        parallelTransition.play();
    }

    @FXML
    void applyRizpaCommand(MouseEvent event) {
        RadioButton selectedCommandOption = (RadioButton) commandOptions.getSelectedToggle();
        String issuedCommandUser = mainAppController.getCurrentUserName();

        mainAppController.applyRizpaCommand(operatedStock, typeChoiceBox.getValue(),
                priceInput.getText(), amountInput.getText(), selectedCommandOption.getText(), issuedCommandUser);
    }

    @Override
    public void loadStyleSheet(boolean darkMode) {
        if (darkMode) {
            actionWindowComponent.getStylesheets().add(DARKCOMMANDWINDOW_CSS_RESOURCE);
            CSS_URL = DARKCOMMANDWINDOW_CSS_RESOURCE;
        }
        else {
            actionWindowComponent.getStylesheets().clear();
            actionWindowComponent.getStylesheets().add(DEFAULTCOMMANDWINDOW_CSS_RESOURCE);
            CSS_URL = null;
        }
    }
}
