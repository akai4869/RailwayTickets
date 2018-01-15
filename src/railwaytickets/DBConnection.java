package railwaytickets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnection {

    private static final String DB_USER = "root";
    private static final String DB_USER_PASSWORD = "";
    private static final String CONNECTION_LINK = "jdbc:mysql://localhost/rts";


    static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(CONNECTION_LINK,DB_USER,DB_USER_PASSWORD);


    }




}
