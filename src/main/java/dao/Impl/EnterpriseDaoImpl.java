package dao.Impl;
import ConnectionPool.*;
import dao.EnterpriseDao;
import po.Enterprise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnterpriseDaoImpl implements EnterpriseDao {
    @Override
    public List<Enterprise> showEnterprise() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from enterprisegroup where access_mode='public'";
        stmt=conn.prepareStatement(sql);
        rs=stmt.executeQuery();
        List<Enterprise> enterpriseGroups = new ArrayList<>();
        while(rs.next())
        {
            Enterprise group = new Enterprise();
            group.setName(rs.getString("name"));
            group.setPeopleNumber(rs.getInt("member_count"));
            group.setScale(rs.getString("scale"));
            group.setWork_orientation(rs.getString("direction"));
            group.setCreator_name(rs.getString("creator_name"));
            group.setMoney(rs.getDouble("enterprise_fund"));
            enterpriseGroups.add(group);
        }
        return enterpriseGroups;
    }
    @Override
    public List<Enterprise> serachEnterprise(String keyword) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from enterprisegroup where name=? and access_mode='public'";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,keyword);
        rs= stmt.executeQuery();
        List<Enterprise> enterpriseGroups = new ArrayList<>();
        while(rs.next())
        {
            Enterprise group = new Enterprise();
            group.setName(rs.getString("name"));
            group.setPeopleNumber(rs.getInt("member_count"));
            group.setScale(rs.getString("scale"));
            group.setWork_orientation(rs.getString("direction"));
            group.setCreator_name(rs.getString("creator_name"));
            group.setMoney(rs.getDouble("enterprise_fund"));
            enterpriseGroups.add(group);
        }
        return enterpriseGroups;
    }

}
