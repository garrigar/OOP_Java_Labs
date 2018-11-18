package gui.utils;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {
    public ErrorAlert(String title, String header, String content){
        super(AlertType.ERROR, content);
        this.setTitle(title);
        this.setHeaderText(header);
    }
}
