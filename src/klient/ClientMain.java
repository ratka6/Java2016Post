package klient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginView.fxml"));
        TabPane login = (TabPane) loader.load();
        LoginController controller = (LoginController) loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Aplikacja kliencka");
        primaryStage.setScene(new Scene(login, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
