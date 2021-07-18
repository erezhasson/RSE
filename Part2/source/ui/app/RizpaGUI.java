package ui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

import static ui.app.ResourcesConstants.APPICON_IMAGE_RESOURCE;
import static ui.app.ResourcesConstants.MAIN_FXML_RESOURCE;

public class RizpaGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("RSE - Rizpa Stock Exchange");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(MAIN_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(APPICON_IMAGE_RESOURCE));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("mainGUI");
        launch(args);
    }
}
