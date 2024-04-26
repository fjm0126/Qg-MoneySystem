package dao.Impl;

import ConnectionPool.ComeTrueConnectionpool;
import dao.UserEnterpriseDao;
import po.Enterprise_flows;
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
    public String check_status(String username) throws SQLException {
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
        String enterprise_group_name=null;
        if(rs.next()) {
            enterprise_group_name = rs.getString("enterprise_group_name");
        }
        String sql2="select status from enterprisegroup where name=?";
        stmt=conn.prepareStatement(sql2);
        stmt.setObject(1,enterprise_group_name);
        rs=stmt.executeQuery();
        if(rs.next()){
            return rs.getString("status");
        }
        return null;
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
    @Override
    public String getEnterprise_name(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        String sql="select enterprise_group_name from userenterprisegroup where user_name=?";
        stmt= conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs= stmt.executeQuery();
        String name = null;
        if(rs.next()){
            name= rs.getString("enterprise_group_name");
        }
        return name;
    }
    @Override
    public boolean isPrincipalOrAdministrator(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        String sql="select role from userenterprisegroup where user_name=?";
        stmt= conn.prepareStatement(sql);
        stmt.setObject(1,username);
        rs= stmt.executeQuery();
        if(rs.next()){
            String role=rs.getString("role");
            return Objects.equals(role, "负责人") || Objects.equals(role, "管理员");
        }
        conn.close();
        stmt.close();
        rs.close();
        return false;
    }
    @Override
    public boolean chargeMoney(String username, double money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        if(!isPrincipalOrAdministrator(username))
        {
            return false;
        }
        String sql="update enterprisegroup set enterprise_fund=enterprise_fund+? where name=?";
        stmt= conn.prepareStatement(sql);
        stmt.setObject(1,money);
        stmt.setObject(2,getEnterprise_name(username));
        stmt.executeUpdate();
        Enterprise_flowsDaoImpl enterpriseFlowsDao=new Enterprise_flowsDaoImpl();
        Enterprise_flows enterpriseFlows=new Enterprise_flows();
        enterpriseFlows.setMoney(money);
        enterpriseFlows.setEnterprise_name(getEnterprise_name(username));
        enterpriseFlows.setType("充值");
        enterpriseFlows.setObject(getEnterprise_name(username));
        enterpriseFlowsDao.insert(enterpriseFlows);
        return true;
    }

    @Override
    public boolean apply_administrator(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        if(isPrincipalOrAdministrator(username))
        {
            return false;
        }
        String sql="update userenterprise set role=? where user_name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,"管理员");
        stmt.setObject(2,username);
        stmt.executeUpdate();
        return true;
    }

    @Override
    public int allot_money(String username,String allot_name, double money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        try {
            conn.setAutoCommit(false); // 开启事务
            if (!isPrincipalOrAdministrator(username)) {
                return -1; //不是负责人或者管理员，无法分配资金
            }
            String enterprise_name = getEnterprise_name(username);
            String sql1 = "select enterprise_group_name=? from userenterprisegroup where user_name=?";
            stmt = conn.prepareStatement(sql1);
            stmt.setObject(1, enterprise_name);
            stmt.setObject(2, allot_name);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return -2;  //企业中不存在此分配用户
            }
            synchronized (this) {
                String sql2 = "select personal_fund from userenterprisegroup where enterprise_group_name=?";
                stmt = conn.prepareStatement(sql2);
                stmt.setObject(1, enterprise_name);
                rs = stmt.executeQuery();
                double all_money = 0;
                while (rs.next()) {
                    all_money += rs.getDouble("personal_fund");
                }
                String sql3 = "select enterprise_fund from enterprisegroup where name=?";
                stmt = conn.prepareStatement(sql3);
                stmt.setObject(1, enterprise_name);
                rs = stmt.executeQuery();
                double enterprise_money = 0;
                if (rs.next()) {
                    enterprise_money = rs.getDouble("enterprise_fund");
                }
                if (money + all_money > enterprise_money) {
                    return -3; //剩余分配资金不足，分配失败
                }
                String sql4 = "update userenterprisegroup set personal_fund=personal_fund+? where user_name=?";
                stmt = conn.prepareStatement(sql4);
                stmt.setObject(1, money);
                stmt.setObject(2, allot_name);
                stmt.executeUpdate();
                conn.commit(); // 提交事务
                return 1; //分配成功
            }
        }catch (SQLException e){
            conn.rollback(); // 回滚事务
            throw e;
        }
    }

    @Override
    public int pull_enter(String pull_name,String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        if(!isPrincipalOrAdministrator(username)){
            return -1; //不是该企业管理员或负责人，无法拉人
        }
        UserDaoImpl userDao=new UserDaoImpl();
        if(!userDao.isUsernameExists(pull_name)){
            return -3; //拉取的用户不存在
        }
        if(searchIfExist(pull_name)==1){
            return -2; //拉取对象已经加入群组，拉取失败
        }
        UserEnterprise userEnterprise=new UserEnterprise();
        userEnterprise.setUsername(pull_name);
        userEnterprise.setRole("普通成员");
        userEnterprise.setEnterprise_group_name(getEnterprise_name(username));
        insert(userEnterprise);
        userDao.updateEnterprise(getEnterprise_name(username),pull_name);
        return 1;
    }
}
