package railwaytickets;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TicketDetailsController implements Initializable {

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
    private Label pnrNo;

    private Integer pnrNoValue;

    public void setPnrNo(Integer pnrNoValue) {
        this.pnrNoValue = pnrNoValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        passengerName.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
        passengerSex.setCellValueFactory(new PropertyValueFactory<>("passengerSex"));
        passengerTable.setSelectionModel(null);

    }

    public void getTicketDetails() {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null, resultSetTrainName = null;

        try {

            String trainNameQuery = "SELECT train_name FROM train WHERE train_number = (SELECT train_number FROM ticket WHERE pnr_number = ?)";
            String generalInfoQuery = "SELECT train_number, source, destination, departure_time, arrival_time, duration_in_hours, date_of_journey FROM ticket WHERE pnr_number = ?";
            String passengerInfoQuery = "SELECT passenger_name, passenger_sex FROM passengers WHERE pnr_number = ?";

            preparedStatement = DBConnection.getConnection().prepareStatement(trainNameQuery);
            preparedStatement.setInt(1, pnrNoValue);
            resultSetTrainName = preparedStatement.executeQuery();
            resultSetTrainName.next();

            preparedStatement = DBConnection.getConnection().prepareStatement(generalInfoQuery);
            preparedStatement.setInt(1, pnrNoValue);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            pnrNo.setText(pnrNoValue.toString());
            Integer trainNoValue = resultSet.getInt("train_number");
            trainNo.setText(trainNoValue.toString());
            trainName.setText(resultSetTrainName.getString("train_name"));
            departureTime.setText(resultSet.getTime("departure_time").toString());
            arrivalTime.setText(resultSet.getTime("arrival_time").toString());
            fromStation.setText(resultSet.getString("source"));
            toStation.setText(resultSet.getString("destination"));
            Integer durationInHoursValue = resultSet.getInt("duration_in_hours");
            durationInHours.setText(durationInHoursValue.toString());
            dateOfJourney.setText(resultSet.getDate("date_of_journey").toString());

            preparedStatement = DBConnection.getConnection().prepareStatement(passengerInfoQuery);
            preparedStatement.setInt(1, pnrNoValue);
            resultSet = preparedStatement.executeQuery();

            ObservableList<PassengerModel> passengerList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                passengerList.add(new PassengerModel(resultSet.getString("passenger_name"), resultSet.getString("passenger_sex")));
            }

            passengerTable.setItems(passengerList);

        } catch (SQLException e) {

            GetAlert.showError(e.toString());

        } finally {

            try {

                if (resultSet != null) {
                    resultSet.close();
                }

                if (resultSetTrainName != null) {
                    resultSetTrainName.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                GetAlert.showError(e.toString());
            }

        }

    }

}
