package gui.modalwindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class ModalWindowController {

    @FXML
    private TextField ldbField, rdbField;
    @FXML
    private Spinner<Integer> pcSpinner;
    @FXML
    private Button okButton, cancelButton;

    TextField getLdbField() { return ldbField; }

    TextField getRdbField() { return rdbField; }

    Spinner<Integer> getPcSpinner() { return pcSpinner; }

    Button getOkButton() { return okButton; }

    Button getCancelButton() { return cancelButton; }

    void initSpinner(){
        pcSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE, 11));
    }


}
