package railwaytickets;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogInController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    public void attemptLogin(ActionEvent event) throws IOException {

        if (LogInModel.isLoginSuccessful(username.getText(), password.getText())) {
            FXMLLoader trainQueryLoader = new FXMLLoader(getClass().getResource("TrainQuery.fxml"));
            Parent root = (Parent) trainQueryLoader.load();

            TrainQueryController trainQueryController = trainQueryLoader.<TrainQueryController>getController();
            trainQueryController.setUserName(username.getText());

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }

    }

    @FXML
    public void onSignUpButtonClick(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "SignUp.fxml");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
