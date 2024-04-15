package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.UserDao;
import po.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDaoImpl implements UserDao{
    @Override
    public int insert(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="insert into user(username,Sdcard,nickname,password,phoneNumber,address,email) values(?,?,?,?,?,?,?)";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,user.getUsername());
        stmt.setObject(2,user.getSdcard());
        stmt.setObject(3,user.getNickname());
        stmt.setObject(4,user.getPassword());
        stmt.setObject(5,user.getPhoneNumber());
        stmt.setObject(6,user.getAddress());
        stmt.setObject(7,user.getEmail());
        return stmt.executeUpdate();
    }
    public boolean login(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user where username=? and password=? and Sdcard=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1, user.getUsername());
        stmt.setObject(2,user.getPassword());
        stmt.setObject(3,user.getSdcard());
        rs=stmt.executeQuery();
        return rs.next();
    }

    public String findPassword(String username, String Sdcard, String phoneNumber) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user where username=? and Sdcard=? and phoneNumber=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        stmt.setObject(2,Sdcard);
        stmt.setObject(3,phoneNumber);
        rs=stmt.executeQuery();
        if(rs.next())
        {
            String password=rs.getString("password");
            return "找回密码成功，密码为："+password;
        }else{
            return "找回密码失败，信息有误或账户不存在！";
        }
    }
    public Map<String, Object> showInformation(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,user.getUsername());
        rs=stmt.executeQuery();
        Map<String, Object> userInfo = new HashMap<>();
        if(rs.next())
        {
            userInfo.put("avatarUrl",rs.getString("avatar"));
            userInfo.put("username",rs.getString("username"));
            userInfo.put("Sdcard",rs.getString("Sdcard"));
            userInfo.put("nickname",rs.getString("nickname"));
            userInfo.put("phoneNumber",rs.getString("phoneNumber"));
            userInfo.put("address",rs.getString("address"));
            userInfo.put("email",rs.getString("email"));
            userInfo.put("personal_fund",rs.getString("personal_fund"));
            userInfo.put("company_name",rs.getString("company_name"));
        }
        return userInfo;
    }
}
