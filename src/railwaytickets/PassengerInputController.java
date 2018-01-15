package railwaytickets;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PassengerInputController implements Initializable {

    @FXML
    private Label trainNo;
    @FXML
    private Label trainName;
    @FXML
    private Label departureTime;
    @FXML
    private Label arrivalTime;
    @FXML
    private Label fromStation;
    @FXML
    private Label toStation;
    @FXML
    private Label durationInHours;
    @FXML
    private Label dateOfJourney;
    @FXML
    private TableView<PassengerModel> passengerTable;
    @FXML
    private TableColumn<PassengerModel, String> passengerName;
    @FXML
    private TableColumn<PassengerModel, String> passengerSex;
    @FXML
    private TextField passengerNameInput;
    @FXML
    private ComboBox<String> passengerSexInput;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton confirmButton;

    private String userName;
    private TrainListModel trainListModel;
    private ObservableList<TrainListModel> trainList;
    private LocalDate dateofJourney;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTrainListModel(TrainListModel trainListModel) {
        this.trainListModel = trainListModel;
    }
    
    public void setTrainList(ObservableList<TrainListModel> trainList) {
        this.trainList = trainList;
    }

    public void setDateofJourney(LocalDate dateofJourney) {
        this.dateofJourney = dateofJourney;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passengerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        passengerName.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
        passengerSex.setCellValueFactory(new PropertyValueFactory<>("passengerSex"));
        passengerSexInput.getItems().addAll("Male", "Female", "Other");

        addButton.disableProperty().bind(
                passengerNameInput.textProperty().isEmpty()
                        .or(passengerSexInput.valueProperty().isNull()));

        BooleanBinding booleanBinding = Bindings.size(passengerTable.getItems()).isEqualTo(0);

        confirmButton.disableProperty().bind(
                booleanBinding);

    }

    public void setLabels() {
        trainNo.setText(trainListModel.getTrainNo().toString());
        trainName.setText(trainListModel.getTrainName());
        departureTime.setText(trainListModel.getDepartureTime().toString());
        arrivalTime.setText(trainListModel.getArrivalTime().toString());
        fromStation.setText(trainListModel.getFromStation());
        toStation.setText(trainListModel.getToStation());
        durationInHours.setText(trainListModel.getDurationInHours().toString());
        dateOfJourney.setText(dateofJourney.toString());

    }

    @FXML
    private void onSignOut(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "LogIn.fxml");

    }

    @FXML
    private void addPassengers(ActionEvent event) {

        PassengerModel passengerModel = new PassengerModel(passengerNameInput.getText(), passengerSexInput.getValue());
        passengerTable.getItems().add(passengerModel);
        passengerNameInput.clear();
        passengerSexInput.setValue(null);
    }

    @FXML
    private void deletePassengers(ActionEvent event) {

        ObservableList<PassengerModel> selectedItems = passengerTable.getSelectionModel().getSelectedItems();
        passengerTable.getItems().removeAll(selectedItems);

    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) throws IOException {

        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            ObservableList<PassengerModel> passengerList = passengerTable.getItems();
            String insertTicket = "INSERT INTO ticket(train_number,source,destination,departure_time,arrival_time,duration_in_hours,date_of_journey,seats,username) VALUES(?,?,?,?,?,?,?,?,?)";
            String insertPassenger = "INSERT INTO passengers(pnr_number,passenger_name,passenger_sex) VALUES(?,?,?)";
            String lastPNRNo = "SELECT MAX(pnr_number) 'last_entered_pnrNo' FROM ticket";

            preparedStatement = DBConnection.getConnection().prepareStatement(insertTicket);
            preparedStatement.setInt(1, trainListModel.getTrainNo());
            preparedStatement.setString(2, trainListModel.getFromStation());
            preparedStatement.setString(3, trainListModel.getToStation());
            preparedStatement.setTime(4, trainListModel.getDepartureTime());
            preparedStatement.setTime(5, trainListModel.getArrivalTime());
            preparedStatement.setInt(6, trainListModel.getDurationInHours());
            preparedStatement.setDate(7, java.sql.Date.valueOf(dateofJourney));
            preparedStatement.setInt(8, passengerTable.getItems().size());
            preparedStatement.setString(9, userName);

            preparedStatement.executeUpdate();

            statement = DBConnection.getConnection().createStatement();
            resultSet = statement.executeQuery(lastPNRNo);
            resultSet.next();

            preparedStatement = DBConnection.getConnection().prepareStatement(insertPassenger);

            for (PassengerModel passengerModel : passengerList) {

                preparedStatement.setInt(1, resultSet.getInt("last_entered_pnrNo"));
                preparedStatement.setString(2, passengerModel.getPassengerName());
                preparedStatement.setString(3, passengerModel.getPassengerSex());

                preparedStatement.executeUpdate();

            }
            
            GetAlert.showInformation("You have successfully booked a ticket!");

            FXMLLoader trainQueryLoader = new FXMLLoader(getClass().getResource("TrainQuery.fxml"));
            Parent trainQueryParent = (Parent) trainQueryLoader.load();

            TrainQueryController trainQueryController = trainQueryLoader.<TrainQueryController>getController();
            trainQueryController.setUserName(userName);

            Scene scene = new Scene(trainQueryParent);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            GetAlert.showError(e.toString());
        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                GetAlert.showError(e.toString());
            }
        }

    }

    @FXML
    private void onBackLinkClick(ActionEvent event) throws IOException {

        FXMLLoader trainListLoader = new FXMLLoader(getClass().getResource("TrainList.fxml"));
        Parent root = (Parent) trainListLoader.load();

        TrainListController trainListController = trainListLoader.<TrainListController>getController();

        trainListController.setUserName(userName);
        trainListController.setTrainList(trainList);
        trainListController.setDateofJourney(dateofJourney);
        trainListController.setTrains();

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

}
