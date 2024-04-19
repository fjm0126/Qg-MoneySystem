package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.Enterprise_flowsDao;
import po.Enterprise_flows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Enterprise_flowsDaoImpl implements Enterprise_flowsDao {
    public void insert(Enterprise_flows enterprise_flows) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="insert into enterprisefinancialflow(enterprise_name,amount,type,object) values(?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,enterprise_flows.getEnterprise_name());
        stmt.setObject(2,enterprise_flows.getMoney());
        stmt.setObject(3,enterprise_flows.getType());
        stmt.setObject(4,enterprise_flows.getObject());
        stmt.executeUpdate();
        conn.close();
        stmt.close();
    }
}
