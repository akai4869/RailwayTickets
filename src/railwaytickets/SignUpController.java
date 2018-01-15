package railwaytickets;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SignUpController implements Initializable {

    @FXML
    public void onBackClick(ActionEvent event) throws IOException {

        new SceneChanger().setNewScene(event, "LogIn.fxml");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
