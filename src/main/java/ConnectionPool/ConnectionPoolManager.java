package ConnectionPool;

import java.sql.SQLException;

/**
 * 连接池维护类
 */
public class ConnectionPoolManager {
    private static ComeTrueConnectionpool connectionpool;
    public static Connectionpool getconnectionpool() throws SQLException {
            connectionpool=new ComeTrueConnectionpool();
            connectionpool.initialConnectionpool();
        return connectionpool;
    }
}
