package sample;

import dataModel.Package;
import dataModel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class ClientMenuController implements Initializable {

    @FXML private TextField heightTextField;
    @FXML private TextField widthTextField;
    @FXML private TextField lengthTextField;
    @FXML private TextField weightTextField;
    @FXML private Label infoLabel;
    @FXML private Label costLabel;
    @FXML private Button orderButton;
    @FXML private Button logoutButton;
    @FXML private TableView tableView;
    @FXML private TableColumn numberColumn;
    @FXML private TableColumn statusColumn;

    private Stage stage;



    private User user;
    private ObservableList<PackageStatus> packageStatuses;

    ChangeListener calculateListener;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setUser(User user) {this.user = user;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert orderButton != null : "Order button fail";

        calculateCost();

        calculateListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                calculateCost();
            }
        };


        heightTextField.textProperty().addListener(calculateListener);
        widthTextField.textProperty().addListener(calculateListener);
        lengthTextField.textProperty().addListener(calculateListener);
        weightTextField.textProperty().addListener(calculateListener);

        orderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(heightTextField.getCharacters().length() > 0 && widthTextField.getCharacters().length() > 0 && lengthTextField.getCharacters().length() > 0 && weightTextField.getCharacters().length() > 0) {
                    order();
                }
                else {
                    infoLabel.setText("Uzupełnij wszystkie pola!");
                }

            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logout();
            }
        });
        tableView.setEditable(false);

        numberColumn.setCellValueFactory(new PropertyValueFactory<PackageStatus, String>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<PackageStatus, String>("status"));


        tableView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = tableView.getSelectionModel().selectedIndexProperty().get();
            showPackage(index);
        });


        loadData();
    }

    private void order() {
        System.out.println("Order");
        showDetails();
    }

    private void showDetails() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DetailsView.fxml"));
        try {
            Pane details = (Pane) loader.load();
            DetailsController controller = (DetailsController) loader.getController();
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Double cost = format.parse(costLabel.getText()).doubleValue();
            Double height = format.parse(heightTextField.getText()).doubleValue();
            Double width = format.parse(widthTextField.getText()).doubleValue();
            Double length = format.parse(lengthTextField.getText()).doubleValue();
            Double weight = format.parse(weightTextField.getText()).doubleValue();
            controller.setUser(user);
            controller.setCost(cost);
            controller.setPack(new Package(height, width, length, weight, cost));
            final Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initOwner(stage);
            details.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(details, 352, 288));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void logout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LoginView.fxml"));
        try {
            TabPane login = (TabPane) loader.load();
            LoginController controller = (LoginController) loader.getController();
            controller.setStage(stage);
            stage.setTitle("Aplikacja kliencka");
            stage.setScene(new Scene(login, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateCost() {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Double height = format.parse(heightTextField.getText()).doubleValue();
            Double width = format.parse(widthTextField.getText()).doubleValue();
            Double length = format.parse(lengthTextField.getText()).doubleValue();
            Double weight = format.parse(weightTextField.getText()).doubleValue();
            Double cost = height*width*length*0.001 + weight*3/1000;
            if (cost < 10) {
                cost = 10.00;
            }
            costLabel.setText(String.format("%.2f", cost));
        } catch (Exception e) {
            System.out.println(e);
            costLabel.setText(String.format("%.2f", 10.00));
        }
    }

    private void loadData() {
        getPackagesInfo();
    }

    private void getPackagesInfo() {
        ServerRequest request = new ServerRequest("packagesInfo", user);
        sendRequest(request);
    }

    private void sendRequest(ServerRequest request) {
        Thread t = new Thread(new ServerRunnable(request, this::parseResponse));
        t.start();
    }

    private void parseResponse(ServerResponse response) {
        if (response.error == null) {
            if (response.object != null && response.object instanceof ArrayList<?>) {

                packageStatuses = FXCollections.observableArrayList((ArrayList<PackageStatus>)response.object);
                tableView.setItems(packageStatuses);

            }
            else {
                infoLabel.setText("Błąd serwera");
            }
        }
        else if (response.error != null) {
            infoLabel.setText(response.error);
        }
        else {
            infoLabel.setText("Błąd serwera");
        }
    }

    private void showPackage(Integer index) {
        System.out.println("Index: " + index);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PackageDetailsView.fxml"));
        try {
            Pane details = (Pane) loader.load();
            PackageDetailsController controller = loader.getController();
            controller.setPack(packageStatuses.get(index).getPack());
            final Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initOwner(stage);
            details.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(details, 528, 324));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
