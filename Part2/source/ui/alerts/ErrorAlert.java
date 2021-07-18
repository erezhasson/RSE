package ui.alerts;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert{
    public ErrorAlert(String title, String header, String msg) {
        super(AlertType.ERROR);

        initAlertComponents(title, header, msg);
    }

    private void initAlertComponents(String title, String header, String msg) {
        setTitle(title);
        setHeaderText(header);
        setContentText(msg);
    }
}
