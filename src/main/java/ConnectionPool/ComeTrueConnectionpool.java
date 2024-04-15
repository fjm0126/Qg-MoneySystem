package ConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 连接池实现类
 */
public class ComeTrueConnectionpool implements Connectionpool {
    private String url;
    private String username;
    private String password;
    private String DriverName;
    private int poolSize;
    private int poolinitialSize;
    private boolean autogrow;
    //管理连接的分配和回收
    private List<ConnectionClass> availableConnection;
    private List<ConnectionClass> usedConnection;
    public void initialConnectionpool() throws SQLException {
        //初始化连接池对象
        Connection connection = null;
        Properties properties=new Properties();
        InputStream ips =getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(ips);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        url = properties.getProperty("jdbc.url");
        username = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
        DriverName = properties.getProperty("jdbc.DriverName");
        poolSize = Integer.parseInt(properties.getProperty("jdbc.poolSize"));
        poolinitialSize = Integer.parseInt(properties.getProperty("jdbc.poolinitialSize"));
        autogrow = Boolean.parseBoolean(properties.getProperty("jdbc.autogrow"));
        availableConnection = new ArrayList<>(poolSize);
        usedConnection = new ArrayList<>(poolSize);
        try {
            Class.forName(DriverName);
            for (int i = 0; i <poolinitialSize ; i++) {
                connection = DriverManager.getConnection(url, username, password);
                availableConnection.add(new ConnectionClass(connection));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Connection getconnection(){
        if(!availableConnection.isEmpty()){ //判断可用连接是否为空
            ConnectionClass connectionClass=availableConnection.remove(0);//取出第一个可用连接
            usedConnection.add(connectionClass);
            connectionClass.setIsAvailable(false);//防止多人使用一个连接
            return connectionClass.getConnection();
        }
        //自动增长
        if(usedConnection.size()>=poolSize&&autogrow){
            Connection connection= null;
            try {
                connection = DriverManager.getConnection(url,username,password);
                ConnectionClass connectionClass=new ConnectionClass(connection);
                usedConnection.add(connectionClass);
                connectionClass.setIsAvailable(false);
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public void releaseConnection(Connection connection) {
        usedConnection.removeIf(con->getconnection()==connection );
        availableConnection.add(new ConnectionClass(connection));
    }

    @Override
    public void closeSource() {
        usedConnection.forEach(ConnectionClass::close);
        availableConnection.forEach(ConnectionClass::close);
        //清空列表
        availableConnection.clear();
        usedConnection.clear();
    }
}

