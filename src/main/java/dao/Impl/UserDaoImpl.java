package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.UserDao;
import po.Enterprise_flows;
import po.Personal_flows;
import po.User;
import po.UserEnterprise;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public String check_status(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select status from user where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeQuery();
        if(rs.next()){
            return rs.getString("status");
        }
        return null;
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
    public int updateInformation(String username ,String col,String newValue) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql = "UPDATE user SET " + col + " = '" + newValue + "' WHERE username = ?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeUpdate();
        return rs;
    }
    public boolean isUsernameExists(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        ResultSet rs=stmt.executeQuery();
        return rs.next();
    }

    @Override
    public boolean isSdcardExists(String Sdcard) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user where Sdcard=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,Sdcard);
        ResultSet rs=stmt.executeQuery();
        return rs.next();
    }

    @Override
    public boolean chargeMoney(String username,double money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update user set personal_fund=personal_fund+? where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,money);
        stmt.setObject(2,username);
        int rs=stmt.executeUpdate();
        return rs > 0;
    }

    @Override
    public void updateEnterprise(String enterprise, String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update user set company_name=? where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,enterprise);
        stmt.setObject(2,username);
        stmt.executeUpdate();
    }
    public boolean ismoneyEnough(String username,double money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select personal_fund from user where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs=stmt.executeQuery();
        double check_money = 0;
        if(rs.next()){
           check_money=rs.getDouble("personal_fund");
        }
        return !(check_money < money);
    }

    public int transferMoney(String username, String method, double money, String transfer_name) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();

        // 开启事务
        conn.setAutoCommit(false);
        try {
            if (!isUsernameExists(transfer_name)) {
                return -3; // 转账对象用户名不存在
            }
            UserEnterpriseDaoImpl userEnterpriseDao = new UserEnterpriseDaoImpl();
            Personal_flowsDaoImpl personalFlowsDao = new Personal_flowsDaoImpl();
            Personal_flows personalFlows = new Personal_flows();
            personalFlows.setUsername(username);
            personalFlows.setMoney(money);
            personalFlows.setType("转账支出");
            Personal_flows personalFlows2 = new Personal_flows();
            personalFlows2.setUsername(transfer_name);
            personalFlows2.setMoney(money);
            personalFlows2.setType("转账收入");
            synchronized (this) {
                if (Objects.equals(method, "personalFunds")) {
                    if (ismoneyEnough(username, money)) {
                        String sql1 = "update user set personal_fund=personal_fund-? where username=?";
                        stmt = conn.prepareStatement(sql1);
                        stmt.setObject(1, money);
                        stmt.setObject(2, username);
                        stmt.executeUpdate();
                        personalFlowsDao.insert(personalFlows);

                        String sql2 = "update user set personal_fund=personal_fund+? where username=?";
                        stmt = conn.prepareStatement(sql2);
                        stmt.setObject(1, money);
                        stmt.setObject(2, transfer_name);
                        stmt.executeUpdate();
                        personalFlowsDao.insert(personalFlows2);

                        // 提交事务
                        conn.commit();
                    } else {
                        return 0; // 个人资金不足，转账失败
                    }
                } else {
                    if (userEnterpriseDao.searchIfExist(username) == 1) {
                        if (userEnterpriseDao.ismoneyEnough(username, money)) {
                            String sql1 = "update userenterprisegroup set personal_fund=personal_fund-? where user_name=?";
                            stmt = conn.prepareStatement(sql1);
                            stmt.setObject(1, money);
                            stmt.setObject(2, username);
                            stmt.executeUpdate();
                            Enterprise_flows enterprise_flows = new Enterprise_flows();
                            Enterprise_flowsDaoImpl enterpriseFlowsDao = new Enterprise_flowsDaoImpl();
                            enterprise_flows.setEnterprise_name(userEnterpriseDao.serachEnterprise(username));
                            enterprise_flows.setMoney(money);
                            enterprise_flows.setType("转账支出");
                            enterprise_flows.setObject(transfer_name);
                            enterpriseFlowsDao.insert(enterprise_flows);
                            String sql2 = "update user set personal_fund=personal_fund+? where username=?";
                            stmt = conn.prepareStatement(sql2);
                            stmt.setObject(1, money);
                            stmt.setObject(2, transfer_name);
                            stmt.executeUpdate();
                            personalFlowsDao.insert(personalFlows2);

                            String sql3="update enterprisegroup set enterprise_fund=enterprise_fund-? where name=?";
                            String enterprise_name=userEnterpriseDao.getEnterprise_name(username);
                            stmt=conn.prepareStatement(sql3);
                            stmt.setObject(1,money);
                            stmt.setObject(2,enterprise_name);
                            stmt.executeUpdate();
                            // 提交事务
                            conn.commit();
                        } else {
                            return -2; // 企业分配的个人资金不足，转账失败
                        }
                    } else {
                        return -1; // 用户尚未加入企业群组，转账失败
                    }
                }
            }
        }catch(SQLException e){
                // 回滚事务
                conn.rollback();
                throw e; // 抛出异常以通知调用者发生了错误
            } finally{
                // 关闭连接
                if (stmt != null) {
                    stmt.close();
                }
                conn.close();
            }
        return 1;
        }
}
