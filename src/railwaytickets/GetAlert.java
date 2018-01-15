package railwaytickets;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GetAlert {

    static void showError(String error) {

        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
        alert.showAndWait();
    }

    static void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
