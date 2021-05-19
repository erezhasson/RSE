package ui;

import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import logicinterface.RizpaEngine;
import logicinterface.RizpaInterface;
import ui.menubar.MenuBarController;

import javax.xml.bind.JAXBException;
import java.io.*;

public class MainController {
    private static final RizpaInterface engine;

    @FXML
    BorderPane mainComponent;

    @FXML
    MenuBarController menuBarComponentController;
    @FXML
    private MenuBar menuBarComponent;

    @FXML
    public void initialize() {
        if (menuBarComponentController != null) {
            menuBarComponentController.setMainController(this);
        }
    }

    static {
        engine = new RizpaEngine();
    }

    public void setMenuBarController(MenuBarController menuBarController) {
        this.menuBarComponentController = menuBarController;
        menuBarController.setMainController(this);
    }

    public void buildStocksFromXMLFile(File file) {
        try {
            engine.buildStocksFromXMLFile(new FileInputStream(file.getAbsolutePath()));
            enableComponents();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SymbolException e) {
            e.printStackTrace();
        } catch (StockException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void enableComponents() {
        mainComponent.getCenter().setDisable(false);
        mainComponent.getBottom().setDisable(false);
    }
}


