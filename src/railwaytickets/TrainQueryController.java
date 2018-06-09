package railwaytickets;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

public class TrainQueryController implements Initializable {

    @FXML
    private TextField fromStation;
    @FXML
    private TextField toStation;

    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            ArrayList<String> possibleSources = new ArrayList<>();
            ArrayList<String> possibleDestinations = new ArrayList<>();
            statement = DBConnection.getConnection().createStatement();
            resultSet = statement.executeQuery("SELECT source, destination FROM train");
            while (resultSet.next()) {
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                if (!possibleSources.contains(source)) {
                    possibleSources.add(source);
                }
                if (!possibleDestinations.contains(destination)) {
                    possibleDestinations.add(destination);
                }
            }
            
            TextFields.bindAutoCompletion(fromStation, possibleSources);
            TextFields.bindAutoCompletion(toStation, possibleDestinations);

        } catch (SQLException e) {
            GetAlert.showError(e.toString());
        } finally {

            try {
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
    public void onGetTrainsButtonClick(ActionEvent event) throws IOException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            ObservableList<TrainListModel> trainList = FXCollections.observableArrayList();
            preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM train WHERE source = ? AND destination = ?");
            preparedStatement.setString(1, fromStation.getText());
            preparedStatement.setString(2, toStation.getText());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                trainList.add(new TrainListModel(resultSet.getInt("train_number"), resultSet.getInt("duration_in_hours"), resultSet.getString("train_name"), resultSet.getString("source"), resultSet.getString("destination"), resultSet.getTime("departure_time"), resultSet.getTime("arrival_time")));
            }

            if (!trainList.isEmpty()) {

                FXMLLoader trainListLoader = new FXMLLoader(getClass().getResource("TrainList.fxml"));
                Parent root = (Parent) trainListLoader.load();

                TrainListController trainListController = trainListLoader.<TrainListController>getController();

                trainListController.setUserName(userName);
                trainListController.setTrainList(trainList);
                trainListController.setTrains();

                Scene scene = new Scene(root);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } else {

                GetAlert.showInformation("Sorry! We couldn't find any train for you at this moment.");

            }

        } catch (SQLException e) {

            GetAlert.showError(e.toString());

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                GetAlert.showError(e.toString());
            }

        }

    }

    @FXML
    public void onSignOut(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "LogIn.fxml");

    }

    @FXML
    private void onBookedTicketHistoryClick(ActionEvent event) throws IOException  {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            ObservableList<BookingHistoryModel> bookedTicketList = FXCollections.observableArrayList();
            String query = "SELECT pnr_number, train_number, date_of_journey, seats FROM ticket WHERE username = ? ";
            preparedStatement = DBConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookedTicketList.add(new BookingHistoryModel(resultSet.getInt("pnr_number"), resultSet.getInt("train_number"), resultSet.getInt("seats"), resultSet.getDate("date_of_journey").toLocalDate()));
            }

            if (!bookedTicketList.isEmpty()) {

                FXMLLoader bookingHistoryLoader = new FXMLLoader(getClass().getResource("BookingHistory.fxml"));
                Parent root = (Parent) bookingHistoryLoader.load();

                BookingHistoryController bookingHistoryController = bookingHistoryLoader.<BookingHistoryController>getController();

                bookingHistoryController.setUserName(userName);
                bookingHistoryController.setBookedTicketList(bookedTicketList);
                bookingHistoryController.setBookedTicketTable();

                Scene scene = new Scene(root);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } else {

                GetAlert.showInformation("You haven't booked any tickets yet!");

            }

        } catch (SQLException e) {
            GetAlert.showError(e.toString());

        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
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
