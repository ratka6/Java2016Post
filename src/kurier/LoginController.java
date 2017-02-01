package kurier;

import dataModel.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import klient.LoginDataController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class LoginController extends TabPane implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private TextField idTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Label regInfoLabel;

    private Stage stage;
    private User user;

    public LoginController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert loginButton != null : "problem with loginButton";
        assert registerButton != null : "problem with registerButton";

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (idTextField.getCharacters().length() > 0 && passwordTextField.getCharacters().length() > 0) {
                    infoLabel.setText("Logowanie...");
                    loginRequest(idTextField.getCharacters().toString(), passwordTextField.getCharacters().toString());
                } else {
                    infoLabel.setText("Wprowadź dane");
                }
            }
        });



        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nameTextField.getCharacters().length() > 0 && addressTextField.getCharacters().length() > 0 && numberTextField.getCharacters().length() > 0 && emailTextField.getCharacters().length() > 0) {
                    register(nameTextField.getCharacters().toString(), addressTextField.getCharacters().toString(), numberTextField.getCharacters().toString(), emailTextField.getCharacters().toString());
                } else {
                    regInfoLabel.setText("Wprowadź dane");
                }
            }
        });
    }

    private void register(String name, String address, String phoneNumber, String email) {
        regInfoLabel.setText("Rejestracja....");
        User user = new User(name, address, phoneNumber, email);
        ServerRequest request = new ServerRequest("kurierRegister", user);
        sendRequest(request);
    }

    private void loginRequest(String username, String password) {
        infoLabel.setText("Logowanie....");
        LoginData data = new LoginData(username, password);
        ServerRequest request = new ServerRequest("kurierLogin", data);
        sendRequest(request);
    }

    private void sendRequest(ServerRequest request) {
        Thread t = new Thread(new KurierServerRunnable(request, this::parseResponse));
        t.start();
    }

    private void parseResponse(ServerResponse response) {

        if (response.responseName.equals("kurierLogin")) {
            if (response.error == null) {
                if (response.object != null) {
                    this.user = (User) response.object;
                    infoLabel.setText("Logowanie udane!");
                    login();
                } else {
                    infoLabel.setText("Błąd servera");
                }
            } else {
                infoLabel.setText(response.error);
            }
        } else if (response.responseName.equals("kurierRegister")) {
            if (response.error == null) {
                if (response.object != null) {
                    LoginData data = (LoginData) response.object;
                    regInfoLabel.setText("Zarejestrowano");
                    showLoginDetails(data);
                } else {
                    regInfoLabel.setText("Błąd servera");
                }
            } else {
                regInfoLabel.setText(response.error);
            }
        } else {
            infoLabel.setText("Błąd serwera");
            regInfoLabel.setText("Błąd serwera");
        }

    }


    private void login() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("KurierTabView.fxml"));
        TabPane login = null;
        try {
            login = (TabPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KurierMenuController controller = (KurierMenuController) loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        stage.setTitle("Aplikacja kuriera");
        stage.setScene(new Scene(login, 600, 400));
        stage.show();
    }


    private void showLoginDetails(LoginData data) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginDataView.fxml"));
        try {
            Pane details = (Pane) loader.load();
            LoginDataController controller = (LoginDataController) loader.getController();
            controller.setLoginData(data);
            final Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initOwner(stage);
            details.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(details, 352, 182));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
