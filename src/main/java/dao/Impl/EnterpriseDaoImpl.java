package dao.Impl;
import ConnectionPool.*;
import dao.EnterpriseDao;
import po.Enterprise;
import po.Enterprise_flows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            group.setStatus(rs.getString("status"));
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
    @Override
    public int createEnterprise(Enterprise enterprise) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        String sql = "insert into enterprisegroup(name,member_count,scale,direction,creator_name,access_mode) values(?,?,?,?,?,?) ";
        stmt = conn.prepareStatement(sql);
        stmt.setObject(1, enterprise.getName());
        stmt.setObject(2, enterprise.getPeopleNumber());
        stmt.setObject(3, enterprise.getScale());
        stmt.setObject(4, enterprise.getWork_orientation());
        stmt.setObject(5, enterprise.getCreator_name());
        stmt.setObject(6, enterprise.getAccess_mode());
        return stmt.executeUpdate();
    }
    public int transfer_money(String username,String enterpriseName, String transfer_name, double money)throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn = comeTrueConnectionpool.getconnection();
        UserEnterpriseDaoImpl userEnterpriseDaoImpl=new UserEnterpriseDaoImpl();
        Enterprise_flows enterpriseFlows1=new Enterprise_flows();
        Enterprise_flows enterpriseFlows2=new Enterprise_flows();
        Enterprise_flowsDaoImpl enterpriseFlowsDaoImpl=new Enterprise_flowsDaoImpl();
        if(!userEnterpriseDaoImpl.isPrincipalOrAdministrator(username)){
            return -3; //不是企业管理员或负责人，不能转账！
        }
        String sql1="select enterprise_fund from enterprisegroup where name=?";
        stmt=conn.prepareStatement(sql1);
        stmt.setObject(1,enterpriseName);
        ResultSet rs=stmt.executeQuery();
        if (rs.next()){
            double check_money=rs.getDouble("enterprise_fund");
            if(check_money<money){
                return -1; //企业资金不足，转账失败
            }
        }
        String sql2="select name from enterprisegroup where name=?";
        stmt=conn.prepareStatement(sql2);
        stmt.setObject(1,transfer_name);
        rs=stmt.executeQuery();
        if (!rs.next()){
                return -2; //转账企业不存在
        }
        conn.setAutoCommit(false);
        try {
            synchronized (this) {
                String sql3 = "update enterprisegroup set enterprise_fund=enterprise_fund-? where name=?";
                stmt = conn.prepareStatement(sql3);
                stmt.setObject(1, money);
                stmt.setString(2, enterpriseName);
                stmt.executeUpdate();
                enterpriseFlows1.setEnterprise_name(enterpriseName);
                enterpriseFlows1.setMoney(money);
                enterpriseFlows1.setType("转账支出");
                enterpriseFlows1.setObject(transfer_name);
                enterpriseFlowsDaoImpl.insert(enterpriseFlows1);

                String sql4 = "update enterprisegroup set enterprise_fund=enterprise_fund+? where name=?";
                stmt = conn.prepareStatement(sql4);
                stmt.setObject(1, money);
                stmt.setString(2, transfer_name);
                stmt.executeUpdate();
                enterpriseFlows2.setEnterprise_name(transfer_name);
                enterpriseFlows2.setMoney(money);
                enterpriseFlows2.setObject(enterpriseName);
                enterpriseFlows2.setType("转账收入");
                enterpriseFlowsDaoImpl.insert(enterpriseFlows2);
                conn.commit();
            }
        }catch (SQLException e) {
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
