package ui.alerts;

import datafiles.dto.RizpaCommandDto;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class CommandAlert extends Alert {
    public CommandAlert(String headerTxt, RizpaCommandDto command) {
        super(AlertType.CONFIRMATION);

        initAlertComponents(headerTxt, command);
    }

    private void initAlertComponents(String headerTxt, RizpaCommandDto command) {
        setTitle("Applying New Command");
        setHeaderText(headerTxt);
        setContentText("The following command is about to be executed: \n" + command.toString());

        ButtonType buttonApply = new ButtonType("Apply", ButtonBar.ButtonData.APPLY);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        getButtonTypes().setAll(buttonApply, buttonCancel);
    }
}
