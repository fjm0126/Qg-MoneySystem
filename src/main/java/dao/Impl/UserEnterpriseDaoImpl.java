package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.UserEnterpriseDao;
import po.UserEnterprise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserEnterpriseDaoImpl implements UserEnterpriseDao {
    @Override
    public Map<String, Object> showPersonal_enterprise(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ResultSet rs2=null;
        Map<String, Object> enterpriseInfo = new HashMap<>();
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select enterprise_group_name from userenterprisegroup where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeQuery();
        String enterprise_group_name;
        if(rs.next()) {
            enterprise_group_name = rs.getString("enterprise_group_name");
        }else{
            return enterpriseInfo;
        }
        String sql2="select * from enterprisegroup where name=?";
        stmt=conn.prepareStatement(sql2);
        stmt.setObject(1,enterprise_group_name);
        rs2= stmt.executeQuery();
        if(rs2.next()){
            enterpriseInfo.put("name",rs2.getString("name"));
            enterpriseInfo.put("member_count",rs2.getDouble("member_count"));
            enterpriseInfo.put("scale",rs2.getString("scale"));
            enterpriseInfo.put("direction",rs2.getString("direction"));
            enterpriseInfo.put("creator_name",rs2.getString("creator_name"));
            enterpriseInfo.put("enterprise_fund",rs2.getDouble("enterprise_fund"));
            enterpriseInfo.put("access_mode",rs2.getString("access_mode"));
        }
        return enterpriseInfo;
    }

    @Override
    public void insert(UserEnterprise userEnterprise) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="insert into userenterprisegroup(user_name,enterprise_group_name,role) values(?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,userEnterprise.getUsername());
        stmt.setObject(2,userEnterprise.getEnterprise_group_name());
        stmt.setObject(3,userEnterprise.getRole());
        stmt.executeUpdate();
    }

    @Override
    public int exit_enterprise(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update user set company_name='无' where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        stmt.executeUpdate();
        String sql2="delete from userenterprisegroup where user_name=?";
        stmt=conn.prepareStatement(sql2);
        stmt.setObject(1,username);
        return stmt.executeUpdate();
    }

    @Override
    public void applyJoinEnterprise(String username, String group_name) {
        UserEnterprise userEnterprise=new UserEnterprise();
        userEnterprise.setUsername(username);
        userEnterprise.setEnterprise_group_name(group_name);
        userEnterprise.setRole("普通成员");
        try {
            insert(userEnterprise);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int searchIfExist(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        try {
            comeTrueConnectionpool.initialConnectionpool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from userenterprisegroup where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs= stmt.executeQuery();
        if(rs.next()){
            return 1;
        }else{
            return 0;
        }
    }
    public boolean deleteEnterprise(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        try {
            comeTrueConnectionpool.initialConnectionpool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        conn=comeTrueConnectionpool.getconnection();
        String sql="select role from userenterprisegroup where user_name=?";
        try {
            stmt=conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setObject(1,username);
        rs= stmt.executeQuery();
        rs.next();
        String role=rs.getString("role");
        if (Objects.equals(role, "负责人")){
            String sql2="delete from enterprisegroup where creator_name=?";
            stmt=conn.prepareStatement(sql2);
            stmt.setObject(1,username);
            stmt.executeUpdate();
            String sql3="delete from userenterprisegroup where user_name=?";
            stmt=conn.prepareStatement(sql3);
            stmt.setObject(1,username);
            stmt.executeUpdate();
            exit_enterprise(username);
            return true;
        }else{
            return false;
        }
    }
    public boolean ismoneyEnough(String username,double money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select personal_fund from userenterprisegroup where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeQuery();
        double check_money = 0;
        if(rs.next()){
            check_money=rs.getDouble("personal_fund");
        }
        return !(check_money < money);
    }
    public String serachEnterprise(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        try {
            comeTrueConnectionpool.initialConnectionpool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from userenterprisegroup where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs= stmt.executeQuery();
        if(rs.next()){
            return rs.getString("enterprise_group_name");
        }else{
            return null;
        }
    }
}
