package datafiles.user;

import java.util.ArrayList;
import java.util.List;

public class UserAlerts {
    private final List<String> alerts;

    public void addAlert(String alert) {
        alerts.add(alert);
    }

    public String getNextAlert() {
        String alert = null;

        if (alerts.size() != 0) {
            alert = alerts.get(0);
            alerts.remove(0);
        }

        return alert;
    }

    public UserAlerts() {
        this.alerts = new ArrayList<>();
    }
}
