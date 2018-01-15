package railwaytickets;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
;
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



public class BookingHistoryController implements Initializable {

    @FXML
    private TableView<BookingHistoryModel> bookedTicketTable;
    @FXML
    private TableColumn<BookingHistoryModel, Integer> pnrNo;
    @FXML
    private TableColumn<BookingHistoryModel, Integer> trainNo;
    @FXML
    private TableColumn<BookingHistoryModel, LocalDate> dateofJourney;
    @FXML
    private TableColumn<BookingHistoryModel, Integer> noOfSeats;
    @FXML
    private JFXButton showDetails;

    private String userName;
    private ObservableList<BookingHistoryModel> bookedTicketList;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBookedTicketList(ObservableList<BookingHistoryModel> bookedTicketList) {
        this.bookedTicketList = bookedTicketList;
    }

    public void setBookedTicketTable() {
        bookedTicketTable.setItems(bookedTicketList);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        showDetails.disableProperty().bind(
                bookedTicketTable.getSelectionModel().selectedItemProperty().isNull());

        pnrNo.setCellValueFactory(new PropertyValueFactory<>("pnrNo"));
        trainNo.setCellValueFactory(new PropertyValueFactory<>("trainNo"));
        dateofJourney.setCellValueFactory(new PropertyValueFactory<>("dateofJourney"));
        noOfSeats.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));

    }

    @FXML
    private void onSignOut(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "LogIn.fxml");

    }

    @FXML
    private void showDetails(ActionEvent event) throws IOException {

        FXMLLoader ticketDetailsLoader = new FXMLLoader(getClass().getResource("TicketDetails.fxml"));
        Parent root = (Parent) ticketDetailsLoader.load();

        TicketDetailsController ticketDetailsController = ticketDetailsLoader.<TicketDetailsController>getController();
        ticketDetailsController.setUserName(userName);
        Integer pnrNoValue = bookedTicketTable.getSelectionModel().getSelectedItem().getPnrNo();
        ticketDetailsController.setPnrNo(pnrNoValue);
        ticketDetailsController.getTicketDetails();

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void bookAnotherTicket(ActionEvent event) throws IOException {

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
