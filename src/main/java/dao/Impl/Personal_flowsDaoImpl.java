package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.Personal_flowsDao;
import po.Personal_flows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personal_flowsDaoImpl implements Personal_flowsDao {
    @Override
    public List<Personal_flows> showPersonal_flows(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from userfinancialflow where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeQuery();
        List<Personal_flows> flows = new ArrayList<>();
        while(rs.next())
        {
            Personal_flows personalFlows=new Personal_flows();
            personalFlows.setId(rs.getInt("id"));
            personalFlows.setUsername(rs.getString("user_name"));
            personalFlows.setType(rs.getString("type"));
            personalFlows.setMoney(rs.getDouble("amount"));
            personalFlows.setTime(rs.getString("time"));
            personalFlows.setObject(rs.getString("object"));
            flows.add(personalFlows);
        }
        return flows;
    }
    public void insert(Personal_flows personalFlows) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="insert into userfinancialflow(user_name,amount,type,object) values(?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,personalFlows.getUsername());
        stmt.setObject(2,personalFlows.getMoney());
        stmt.setObject(3,personalFlows.getType());
        stmt.setObject(4,personalFlows.getObject());
        stmt.executeUpdate();
    }

    @Override
    public void updateUsername(String username,String new_name) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update userfinancialflow set user_name=? where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,new_name);
        stmt.setObject(2,username);
        stmt.executeUpdate();
    }
}
