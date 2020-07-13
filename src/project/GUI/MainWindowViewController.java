/*
 *  FIT VUT
 * Project for IJA, 2018/2019
 * Authors: Jan Beran (xberan43)
 *           Daniel Bubenicek (xbuben05)
 *
 * MainWindowController class.
 *
 * */
package project.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class MainWindowViewController implements Initializable {


    @FXML private MenuItem openTabMI, closeMI, newNotationGameOpenTab;
    @FXML private TabPane tabPane;
    private Tab myDynamicTab;
    private int numberOfCreatedTabs = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTabDynamically();
        openTabMI.setOnAction((event)->{
            createTabDynamically();
        });

        closeMI.setOnAction((event)->{Platform.exit();});
    }

    private void createTabDynamically() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TabView.fxml"));
        try {
            Parent parent = loader.load();
            myDynamicTab = new Tab("Hra " + numberOfCreatedTabs++);
            myDynamicTab.setClosable(true);
            myDynamicTab.setContent(parent);
            tabPane.getTabs().add(myDynamicTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}