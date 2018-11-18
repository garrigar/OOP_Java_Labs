package gui.mainwindow;

import functions.Function;
import functions.exceptions.InappropriateFunctionPointException;
import gui.FunctionLoader;
import gui.utils.Definitions;
import gui.FunctionDocument;
import gui.FunctionTableHandler;
import gui.utils.ErrorAlert;
import gui.utils.FXMLErrorException;
import gui.modalwindow.ModalWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class MainWindow {

    private Stage stage;

    private MainWindowController controller;

    private ModalWindow modalWindow;
    private FileChooser fileChooser;
    private FunctionDocument functionDocument;

    private FileChooser classChooser;
    private FunctionLoader loader;

    private boolean virgin = true;
    private String currentFilename;

    private int loadsCount = 0;

    public MainWindow() throws FXMLErrorException {

        stage = new Stage();
        stage.setTitle(Definitions.APP_NAME);
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        // ---------------------------------------------------------------------------------
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new FXMLErrorException("Main Window: FXML Error", e);
        }
        controller = fxmlLoader.getController();

        stage.setScene(new Scene(root, 500, 450));
        // ---------------------------------------------------------------------------------

        modalWindow = new ModalWindow();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tabulated function files", "*" + Definitions.EXT),
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        functionDocument = new FunctionDocument();
        // ---------------------------------------------------

        final FunctionTableHandler tableHandler = new FunctionTableHandler(functionDocument, controller.getTable());

        final MenuItem newMI = controller.getNewMI();
        final MenuItem openMI = controller.getOpenMI();
        final MenuItem saveMI = controller.getSaveMI();
        final MenuItem saveAsMI = controller.getSaveAsMI();
        final MenuItem exitMI = controller.getExitMI();
        final MenuItem loadMI = controller.getLoadMI();

        final TextField xField = controller.getXField();
        final TextField yField = controller.getYField();
        final Button addButton = controller.getAddButton();
        final Button deleteButton = controller.getDeleteButton();

        setMenuItemsDisable(true, saveMI, saveAsMI);
        setNodesDisable(true, xField, yField, addButton, deleteButton);
        xField.setText(Definitions.VIRGIN_HINT);
        yField.setText(Definitions.VIRGIN_HINT);

        newMI.setOnAction(event -> {
            if (wantContinueActionIfModifiedWithSavingAsSelected(saveMI.getOnAction(), "creating a new file")) {
                if (modalWindow.showDialog() == ModalWindow.OK){
                    functionDocument.newFunction(modalWindow.getLeftDomainBorder(), modalWindow.getRightDomainBorder(),
                            modalWindow.getPointsCount());
                    tableHandler.reloadPointItems();

                    deflower();

                    currentFilename = Definitions.UNTITLED_FILE;
                    stage.setTitle(currentFilename + Definitions.APP_NAME_POSTFIX);
                    fileChooser.setInitialFileName(currentFilename);
                }
            }
        });

        openMI.setOnAction(event -> {
            if (wantContinueActionIfModifiedWithSavingAsSelected(saveMI.getOnAction(), "opening another file")) {
                fileChooser.setTitle("Open file");
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    fileChooser.setInitialDirectory(selectedFile.getParentFile());
                    try {
                        functionDocument.loadFunction(selectedFile.getAbsolutePath());
                        tableHandler.reloadPointItems();

                        deflower();

                        currentFilename = selectedFile.getName();
                        stage.setTitle(currentFilename + Definitions.APP_NAME_POSTFIX);
                        fileChooser.setInitialFileName(currentFilename);
                    } catch (IOException e) {
                        new ErrorAlert("Input/output exception",
                                "Error opening file",
                                "Check file or filename").showAndWait();
                    } catch (IllegalArgumentException e) {
                        new ErrorAlert("Illegal argument exception",
                                "Points are invalid",
                                e.getMessage()).showAndWait();
                    }
                }
            }
        });

        saveMI.setOnAction(event -> {
            if (functionDocument.isFilenameAssigned()){
                try {
                    functionDocument.saveFunction();
                } catch (IOException e) {
                    new ErrorAlert("Input/output exception",
                            "Error saving file",
                            "Check file or filename").showAndWait();
                }
            } else {
                saveAsMI.getOnAction().handle(event);
            }
        });

        saveAsMI.setOnAction(event -> {
            fileChooser.setTitle("Save file as");
            File selectedFile = fileChooser.showSaveDialog(stage);
            if (selectedFile != null) {
                fileChooser.setInitialDirectory(selectedFile.getParentFile());
                try {
                    functionDocument.saveFunctionAs(selectedFile.getAbsolutePath());

                    currentFilename = selectedFile.getName();
                    stage.setTitle(currentFilename + Definitions.APP_NAME_POSTFIX);
                    fileChooser.setInitialFileName(currentFilename);
                } catch (IOException e) {
                    new ErrorAlert("Input/output exception",
                            "Error saving file",
                            "Check file or filename").showAndWait();
                }
            }
        });

        exitMI.setOnAction(event -> {
            if (wantContinueActionIfModifiedWithSavingAsSelected(saveMI.getOnAction(), "exit")){
                modalWindow.close();
                stage.close();
                //System.exit(0);
            }
        });

        stage.setOnCloseRequest(event -> {
            if (wantContinueActionIfModifiedWithSavingAsSelected(saveMI.getOnAction(), "exit")){
                modalWindow.close();
                stage.close();
                //System.exit(0);
            } else {
                event.consume();
            }
        });

        // -------------------------------------------------------------------------------------------------------------

        classChooser = new FileChooser();
        classChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Java class files", "*.class")
        );
        classChooser.setTitle("Open class file");
        File dir = new File(".\\out\\production\\Lab06_GUI\\functions\\basic");
        if (dir.exists())
            classChooser.setInitialDirectory(dir);

        loader = new FunctionLoader();

        loadMI.setOnAction(event -> {
            if (wantContinueActionIfModifiedWithSavingAsSelected(saveMI.getOnAction(), "loading a new function")) {
                if (wantContinueLoad()) {
                    File selectedFile = classChooser.showOpenDialog(stage);
                    if (selectedFile != null) {
                        classChooser.setInitialDirectory(selectedFile.getParentFile());
                        if (modalWindow.showDialog() == ModalWindow.OK) {
                            try {
                                Class<?> clazz = loader.loadClassFromFile(selectedFile);
                                Object obj = clazz.getConstructor().newInstance();

                                if (obj instanceof Function) {

                                    functionDocument.tabulateFunction((Function) obj,
                                            modalWindow.getLeftDomainBorder(), modalWindow.getRightDomainBorder(),
                                            modalWindow.getPointsCount());

                                    tableHandler.reloadPointItems();

                                    deflower();

                                    currentFilename = Definitions.UNTITLED + '-' + clazz.getName() + Definitions.EXT;
                                    stage.setTitle(currentFilename + Definitions.APP_NAME_POSTFIX);
                                    fileChooser.setInitialFileName(currentFilename);

                                } else {
                                    new ErrorAlert("Class mismatch",
                                            "Not a Function",
                                            "This class doesn't implement Function interface").showAndWait();
                                }

                            } catch (IllegalArgumentException e) {
                                new ErrorAlert("Illegal argument exception",
                                        "Check tabulation parameters",
                                        e.getMessage()).showAndWait();
                            } catch (IOException e) {
                                new ErrorAlert("Input/output exception",
                                        "Error loading class from file",
                                        "Check file or filename").showAndWait();
                            } catch (ClassFormatError e) {
                                new ErrorAlert("Class format error",
                                        e.getMessage(),
                                        "Check class format").showAndWait();
                            } catch (InstantiationException e) {
                                new ErrorAlert("Instantiation exception",
                                        e.getMessage(), "").showAndWait();
                            } catch (InvocationTargetException e) {
                                new ErrorAlert("Invocation target exception",
                                        e.getMessage(), "").showAndWait();
                            } catch (NoSuchMethodException e) {
                                new ErrorAlert("No such method exception",
                                        e.getMessage(),
                                        "Make sure an empty constructor exists").showAndWait();
                            } catch (IllegalAccessException e) {
                                new ErrorAlert("Illegal access exception",
                                        e.getMessage(), "").showAndWait();
                            } catch (LinkageError e) {
                                new ErrorAlert("Linkage error",
                                        e.getMessage(),
                                        "Corresponding class has been already loaded. " +
                                                "Maybe you will need to restart the application").showAndWait();
                            }
                        }

                    }
                }
            }
        });

        // -------------------------------------------------------------------------------------------------------------

        addButton.setOnAction(event -> {
            try {
                tableHandler.addItem(xField.getText(), yField.getText());
                xField.clear();
                yField.clear();
            } catch (NumberFormatException e) {
                new ErrorAlert("Number format exception",
                        e.getMessage(),
                        "Input only double values").showAndWait();
            } catch (InappropriateFunctionPointException e) {
                new ErrorAlert("Inappropriate function point exception",
                        "Incorrect point X",
                        e.getMessage()).showAndWait();
            }
        });

        deleteButton.setOnAction(event -> {
            try {
                tableHandler.deleteSelectedItem();
            } catch (IllegalStateException e){
                new ErrorAlert("Illegal state exception",
                        "Too few points left",
                        "Any tabulated function must have at least two points").showAndWait();
            }
        });

    }

    public void show(){
        stage.show();
    }

    private boolean wantContinueActionIfModifiedWithSavingAsSelected(EventHandler<ActionEvent> savior, String actionDescription){

        if (!functionDocument.isModified()) return true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Current file (" + currentFilename + ") is not saved.\n" +
                        "Do you want to save it before " + actionDescription + "?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Warning");
        alert.setHeaderText("Current file is not saved");

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) return false; // probably impossible

        if (result.get() == ButtonType.YES){
            savior.handle(new ActionEvent());
            return true;
        } else if (result.get() == ButtonType.NO){
            return true;
        } else if (result.get() == ButtonType.CANCEL){
            return false;
        }
        // probably unreachable
        return false;
    }

    private boolean wantContinueLoad(){
        loadsCount++;
        if (loadsCount == 1) return true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Linkage error risk");
        alert.setContentText("We recommend you to load the classes that are independent\n" +
                "from already previously loaded classes or reload one of\n" +
                "previously successfully loaded classes. If you attempt to load\n" +
                "the class on that one of previous classes can depend,\n" +
                "a Linkage Error may occur. If you still want to load such a class,\n" +
                "or Linkage Error occurred, it is helpful to restart the application\n" +
                "and load the class from scratch.");

        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) return false; // probably impossible

        if (result.get() == ButtonType.OK){
            return true;
        } else if (result.get() == ButtonType.CANCEL){
            return false;
        }
        // probably unreachable
        return false;
    }

    private void setNodesDisable(boolean value, Node... items){
        for (Node node : items) {
            node.setDisable(value);
        }
    }

    private void setMenuItemsDisable(boolean value, MenuItem... items){
        for (MenuItem item : items) {
            item.setDisable(value);
        }
    }

    private void deflower(){
        if (virgin) {
            setMenuItemsDisable(false,
                    controller.getSaveMI(),
                    controller.getSaveAsMI()
            );
            setNodesDisable(false,
                    controller.getXField(),
                    controller.getYField(),
                    controller.getAddButton(),
                    controller.getDeleteButton()
            );
            controller.getXField().clear();
            controller.getYField().clear();
            virgin = false;
        }
    }

}
