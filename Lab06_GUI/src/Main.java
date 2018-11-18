import gui.mainwindow.MainWindow;
import gui.utils.FXMLErrorException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws FXMLErrorException {

        MainWindow mw = new MainWindow();
        mw.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
