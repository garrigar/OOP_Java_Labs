package gui.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainWindowController {

    @FXML
    private MenuItem newMI, openMI, saveMI, saveAsMI, exitMI, loadMI;
    @FXML
    private TableView table;
    @FXML
    private TextField xField, yField;
    @FXML
    private Button addButton, deleteButton;


    MenuItem getNewMI() {
        return newMI;
    }

    MenuItem getOpenMI() {
        return openMI;
    }

    MenuItem getSaveMI() {
        return saveMI;
    }

    MenuItem getSaveAsMI() {
        return saveAsMI;
    }

    MenuItem getExitMI() {
        return exitMI;
    }

    MenuItem getLoadMI() {
        return loadMI;
    }

    TableView getTable() {
        return table;
    }

    TextField getXField() {
        return xField;
    }

    TextField getYField() {
        return yField;
    }

    Button getAddButton() {
        return addButton;
    }

    Button getDeleteButton() {
        return deleteButton;
    }
}
