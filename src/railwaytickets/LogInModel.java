package railwaytickets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInModel {

    static boolean isLoginSuccessful(String username, String password) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            preparedStatement = DBConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                GetAlert.showError("Incorrect Credentials. Please try again.");
                return false;
            }

        } catch (SQLException e) {
            GetAlert.showError(e.toString());
            return false;
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
