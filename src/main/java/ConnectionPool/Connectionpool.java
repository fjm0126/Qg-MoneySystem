package ConnectionPool;

import java.sql.Connection;

/**
 * 连接池接口
 */
public interface Connectionpool {
    Connection getconnection();
    void releaseConnection(Connection connection); //释放一个数据库连接
    void closeSource(); //关闭所有资源
}
