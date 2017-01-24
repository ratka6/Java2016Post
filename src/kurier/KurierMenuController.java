package kurier;

import dataModel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by krzysiek on 19.01.2017.
 */
public class KurierMenuController implements Initializable {

    @FXML private TableView tableView;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn statusColumn;
    @FXML private TableColumn sourceColumn;
    @FXML private TableColumn destinationColumn;
    @FXML private Button logoutButton;

    private Stage stage;
    private User user;
    private ObservableList<KurierPackageInfo> packageInfos;
    private Timer timer = new Timer();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logout();
            }
        });

        tableView.setEditable(false);

        idColumn.setCellValueFactory(new PropertyValueFactory<KurierPackageInfo, String>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<KurierPackageInfo, String>("status"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<KurierPackageInfo, String>("source"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<KurierPackageInfo, String>("destination"));


        tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = tableView.getSelectionModel().selectedIndexProperty().get();
            showInfo(index);
        });



        timer.schedule(new Refresh(), 1500);

    }



    private void loadData() {
        System.out.println("loadData");
        ServerRequest request = new ServerRequest("kurierPackagesInfo", user);
        sendRequest(request);
    }

    private void sendRequest(ServerRequest request) {
        Thread t = new Thread(new ServerRunnable(request, this::parseResponse));
        t.start();
    }

    private void parseResponse(ServerResponse response) {
        if (response.error == null) {
            if (response.object != null && response.object instanceof ArrayList<?>) {
                packageInfos = FXCollections.observableArrayList((ArrayList<KurierPackageInfo>)response.object);
                tableView.setItems(packageInfos);
            }
        }
    }

    private void showInfo(int index) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("KurierTaskDetailsView.fxml"));
        try {
            Pane details = (Pane) loader.load();
            KurierTaskDetailsController controller = loader.getController();

            //set

            final Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initOwner(stage);
            details.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(details, 528, 380));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void logout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("KurierLoginView.fxml"));
        try {
            TabPane login = (TabPane) loader.load();
            LoginController controller = (LoginController) loader.getController();
            controller.setStage(stage);
            stage.setTitle("Aplikacja kuriera");
            stage.setScene(new Scene(login, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

