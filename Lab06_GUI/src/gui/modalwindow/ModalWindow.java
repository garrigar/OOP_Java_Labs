package gui.modalwindow;

import gui.utils.ErrorAlert;
import gui.utils.FXMLErrorException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalWindow {

    private Stage stage;

    private ModalWindowController controller;

    public static final int OK = 0;
    public static final int CANCEL = 1;
    private int lastButtonClicked;

    public ModalWindow() throws FXMLErrorException {

        stage = new Stage();
        stage.setTitle("New tabulated function parameters");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            stage.hide();
            lastButtonClicked = CANCEL;
            event.consume();
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModalWindow.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new FXMLErrorException("Modal Window: FXML Error", e);
        }
        controller = loader.getController();

        stage.setScene(new Scene(root, 340, 170));

        controller.initSpinner();
        controller.getOkButton().setOnAction(event -> {
            try{
                if (this.getLeftDomainBorder() >= this.getRightDomainBorder())
                    throw new IllegalArgumentException();
                stage.hide();
                lastButtonClicked = OK;
            } catch (NumberFormatException e){
                new ErrorAlert("Number format exception",
                        e.getMessage(),
                        "Input only double values").showAndWait();
            } catch (IllegalArgumentException e){
                new ErrorAlert("Illegal argument exception",
                        "Incorrect domain borders",
                        e.getMessage()).showAndWait();
            }
        });
        controller.getCancelButton().setOnAction(event -> {
            stage.hide();
            lastButtonClicked = CANCEL;
        });
    }

    public int showDialog(){
        stage.showAndWait();
        return lastButtonClicked;
    }

    public void close(){
        stage.close();
    }

    public double getLeftDomainBorder() throws NumberFormatException {
        return Double.parseDouble(controller.getLdbField().getText());
    }

    public double getRightDomainBorder() throws NumberFormatException {
        return Double.parseDouble(controller.getRdbField().getText());
    }

    public int getPointsCount(){
        return controller.getPcSpinner().getValue();
    }

}
