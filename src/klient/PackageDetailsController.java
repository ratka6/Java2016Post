package klient;

import dataModel.Package;
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
public class PackageDetailsController implements Initializable {

    @FXML private Label heightLabel;
    @FXML private Label widthLabel;
    @FXML private Label lengthLabel;
    @FXML private Label weightLabel;
    @FXML private Label costLabel;
    @FXML private Label sourceLabel;
    @FXML private Label destinationLabel;
    @FXML private Label dateLabel;
    @FXML private Button okButton;


    private Package pack;

    public void setPack(Package pack) {
        this.pack = pack;
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

        showDetails();
    }

    private void showDetails() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(pack == null){}
                heightLabel.setText(String.format("%.2f cm", pack.getHeight()));
                widthLabel.setText(String.format("%.2f cm", pack.getWidth()));
                lengthLabel.setText(String.format("%.2f cm", pack.getLength()));
                weightLabel.setText(String.format("%.2f g", pack.getWeight()));
                costLabel.setText(String.format("%.2f zł", pack.getCost()));
                sourceLabel.setText(pack.getSourceAddress());
                destinationLabel.setText(pack.getDestinationAddres());
                dateLabel.setText(pack.getDate());
            }
        });
        t.start();
    }

}
