package kurier;

import dataModel.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class KurierTaskDetailsController implements Initializable {

    @FXML private Label heightLabel;
    @FXML private Label widthLabel;
    @FXML private Label lengthLabel;
    @FXML private Label weightLabel;
    @FXML private Label nameLabel;
    @FXML private Label sourceLabel;
    @FXML private Label destinationLabel;
    @FXML private Label dateLabel;
    @FXML private Button okButton;
    @FXML private Button changeStatusButton;
    @FXML private Label infoLabel;
    @FXML private ChoiceBox<Statuses> statusComboBox;
    
    private Timer timer = new Timer();

    private KurierPackageInfo info;
    private ObservableList<Statuses> statuses = FXCollections.observableArrayList(
            Statuses.ATCOURIER,
            Statuses.AWAITS,
            Statuses.CANCELLED,
            Statuses.DELIVERED,
            Statuses.UNKNOWN
    );

    public void setInfo(KurierPackageInfo info) {
        this.info = info;
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
        
        changeStatusButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeStatus();
            }
        });
        timer.schedule(new Refresh(), 100);

    }

    private void loadData() {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	System.out.println(info);
                while(info == null){}
                statusComboBox.setItems(statuses);
                heightLabel.setText(String.format("%.2f cm", info.getPackageStatus().getPack().getHeight()));
                widthLabel.setText(String.format("%.2f cm", info.getPackageStatus().getPack().getWidth()));
                lengthLabel.setText(String.format("%.2f cm", info.getPackageStatus().getPack().getLength()));
                weightLabel.setText(String.format("%.2f g", info.getPackageStatus().getPack().getWeight()));
                nameLabel.setText(info.getUser().getName());
                sourceLabel.setText(info.getPackageStatus().getPack().getSourceAddress());
                destinationLabel.setText(info.getPackageStatus().getPack().getDestinationAddres());
                dateLabel.setText(info.getPackageStatus().getPack().getDate());
                int index = statuses.indexOf(info.getStatus());
                statusComboBox.getSelectionModel().select(index);
            }
        });
    }

    private void changeStatus() {
        String current = statusComboBox.getSelectionModel().getSelectedItem().toString();
        ServerRequest request = new ServerRequest("changeStatus", new ChangeStatus(info.getId(), current));
        System.out.println(request.requestName);
        System.out.println(request.object);
        sendRequest(request);
    }

    private void sendRequest(ServerRequest request) {
        Thread t = new Thread(new KurierServerRunnable(request, this::parseResponse));
        t.start();
    }

    private void parseResponse(ServerResponse response) {
        if(response.error == null) {
            infoLabel.setText("Zmieniono status");
            changeStatusButton.setDisable(true);
        }
        else {
            infoLabel.setText(response.error);
        }
    }

    class Refresh extends TimerTask {
        @Override
        public void run() {
            System.out.println("Timer action");
            loadData();
        }
    }

}
