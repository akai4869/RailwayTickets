package railwaytickets;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {

    void setNewScene(ActionEvent event, String urlFXMLDocument) throws IOException {

        Parent newScene = FXMLLoader.load(getClass().getResource(urlFXMLDocument));
        Scene scene = new Scene(newScene);
        //To get the stage on which the event occured //Super Important
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    

}
