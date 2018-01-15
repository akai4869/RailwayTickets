package railwaytickets;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TrainListController implements Initializable {

    @FXML
    private TableView<TrainListModel> trainTable;
    @FXML
    private TableColumn<TrainListModel, Integer> trainNo;
    @FXML
    private TableColumn<TrainListModel, Integer> durationInHours;
    @FXML
    private TableColumn<TrainListModel, String> trainName;
    @FXML
    private TableColumn<TrainListModel, String> fromStation;
    @FXML
    private TableColumn<TrainListModel, String> toStation;
    @FXML
    private TableColumn<TrainListModel, Time> departureTime;
    @FXML
    private TableColumn<TrainListModel, Time> arrivalTime;
    @FXML
    private JFXDatePicker dateofJourney;
    @FXML
    private JFXButton bookATicket;

    private String userName;
    private ObservableList<TrainListModel> trainList;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTrainList(ObservableList<TrainListModel> trainList) {
        this.trainList = trainList;
    }

    public void setDateofJourney(LocalDate dateofJourney) {
        this.dateofJourney.setValue(dateofJourney);
    }

    public void setTrains() {

        trainTable.setItems(trainList);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bookATicket.disableProperty().bind(
                dateofJourney.valueProperty().isNull()
                        .or(trainTable.getSelectionModel().selectedItemProperty().isNull()));

        trainNo.setCellValueFactory(new PropertyValueFactory<>("trainNo"));
        durationInHours.setCellValueFactory(new PropertyValueFactory<>("durationInHours"));
        trainName.setCellValueFactory(new PropertyValueFactory<>("trainName"));
        fromStation.setCellValueFactory(new PropertyValueFactory<>("fromStation"));
        toStation.setCellValueFactory(new PropertyValueFactory<>("toStation"));
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

    }

    @FXML
    private void onSignOut(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "LogIn.fxml");

    }

    @FXML
    public void onBookATicketButtonClick(ActionEvent event) throws IOException {

        FXMLLoader passengerInputLoader = new FXMLLoader(getClass().getResource("PassengerInput.fxml"));
        Parent root =  (Parent)passengerInputLoader.load();

        PassengerInputController passengerInputController = passengerInputLoader.<PassengerInputController>getController();

        TrainListModel trainListModel = trainTable.getSelectionModel().getSelectedItem();

        passengerInputController.setUserName(userName);
        passengerInputController.setTrainListModel(trainListModel);
        passengerInputController.setTrainList(trainList);
        passengerInputController.setDateofJourney(dateofJourney.getValue());
        passengerInputController.setLabels();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void onBackLinkClick(ActionEvent event) throws IOException {

        FXMLLoader trainQueryLoader = new FXMLLoader(getClass().getResource("TrainQuery.fxml"));
        Parent root = (Parent) trainQueryLoader.load();

        TrainQueryController trainQueryController = trainQueryLoader.<TrainQueryController>getController();

        trainQueryController.setUserName(userName);

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

}
