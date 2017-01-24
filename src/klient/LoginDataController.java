package klient;

import dataModel.LoginData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by krzysiek on 18.01.2017.
 */
public class LoginDataController implements Initializable {

    private LoginData loginData;

    @FXML private Label loginLabel;
    @FXML private Label passwordLabel;
    @FXML private Button okButton;

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            }
        });

        showUser();
    }

    private void showUser() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(loginData == null){}
                loginLabel.setText(loginData.id);
                passwordLabel.setText(loginData.password);
            }
        });
        t.start();
    }
}
