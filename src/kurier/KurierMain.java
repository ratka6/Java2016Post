package kurier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class KurierMain extends Application{

    private static Socket socket;
	public static ObjectOutputStream objectOutputStream;
	public static ObjectInputStream objectInputStream;


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
		try {
			socket = new Socket("localhost", 6666);
	    	objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
	    	objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        launch(args);
    }
}