package sample;

import dataModel.*;
import dataModel.Package;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by krzysiek on 08.01.2017.
 */
public class DetailsController implements Initializable {

    @FXML private DatePicker datePicker;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private TextField sourceTextField;
    @FXML private TextField destinationTextField;
    @FXML private Label infoLabel;
    @FXML private Pane pane;

    private Double cost;
    private User user;
    private Package pack;


    public void setPack(Package pack) {
        this.pack = pack;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assert confirmButton != null : "Confirm button fail";

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!sourceTextField.getText().isEmpty() && !destinationTextField.getText().isEmpty() && datePicker.getValue() != null) {
                    Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    if(date.before(new Date())) {
                        infoLabel.setText("Data niepoprawna");
                    }
                    else {
                        infoLabel.setText("");
                        order();
                    }
                }
                else {
                    infoLabel.setText("Wypełnij wszystkie pola");
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });

        showUser();

    }


    private void order() {
        infoLabel.setText("Zamawianie kuriera...");
        LocalDate date = datePicker.getValue();
        if (pack != null) {
            pack.setDate(date.toString());
            pack.setDestinationAddres(destinationTextField.getText());
            pack.setSourceAddress(sourceTextField.getText());
            ServerRequest request = new ServerRequest("orderCourier", pack);
            sendRequest(request);
        }
        else {
            infoLabel.setText("Błąd - spróbuj ponownie");
        }

    }

    private void sendRequest(ServerRequest request) {
        Thread t = new Thread(new ServerRunnable(request, this::parseResponse));
        t.start();
    }

    private void parseResponse(ServerResponse response) {
        if (response.responseName.equals("orderCourier")) {
            if (response.error == null) {
                infoLabel.setText("Zamówiono kuriera!");
            } else {
                infoLabel.setText(response.error);
            }
            cancelButton.setText("Zamknij");
            confirmButton.setDisable(true);
        } else {
            infoLabel.setText("Błąd serwera");
        }
    }



    private void showUser() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(user == null){}
                sourceTextField.setText(user.getAddress());
            }
        });
        t.start();
    }

}
