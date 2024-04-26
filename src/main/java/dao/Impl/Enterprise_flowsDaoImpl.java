package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.Enterprise_flowsDao;
import po.Enterprise_flows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Enterprise_flows> showEnterprise_flows(String enterprise_name) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from enterprisefinancialflow where enterprise_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,enterprise_name);
        rs=stmt.executeQuery();
        List<Enterprise_flows> enterpriseFlows=new ArrayList<>();
        while(rs.next()){
            Enterprise_flows enterprise_flows=new Enterprise_flows();
            enterprise_flows.setId(rs.getInt("id"));
            enterprise_flows.setEnterprise_name(enterprise_name);
            enterprise_flows.setType((rs.getString("type")));
            enterprise_flows.setTime(rs.getString("time"));
            enterprise_flows.setObject(rs.getString("object"));
            enterprise_flows.setMoney(rs.getDouble("amount"));
            enterpriseFlows.add(enterprise_flows);
        }
        return enterpriseFlows;
    }
}
