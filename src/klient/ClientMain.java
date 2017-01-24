package klient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static Socket socket;
	public static ObjectInputStream objectInputStream;
	public static ObjectOutputStream objectOutputStream;
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
    	try {
			socket = new Socket("localhost", 6666);
	    	objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	    	objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        launch(args);
    }
}
