package kurier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class KurierMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("KurierLoginView.fxml"));
        TabPane login = (TabPane) loader.load();
        LoginController controller = (LoginController) loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Aplikacja kuriera");
        primaryStage.setScene(new Scene(login, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
