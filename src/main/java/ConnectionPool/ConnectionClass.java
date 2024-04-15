package ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionClass {
    private Connection connection;
    private boolean isAvailable;

    public ConnectionClass(Connection connection, boolean isAvailable) {
        this.connection = connection;
        this.isAvailable = true;
    }

    public ConnectionClass() {
    }

    public ConnectionClass(Connection connection) {
        this.connection=connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
