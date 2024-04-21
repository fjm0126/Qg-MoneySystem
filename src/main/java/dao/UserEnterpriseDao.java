package dao;

import po.UserEnterprise;

import java.sql.SQLException;
import java.util.Map;

public interface UserEnterpriseDao {
    Map<String, Object> showPersonal_enterprise(String username) throws SQLException;
    void insert (UserEnterprise userEnterprise) throws SQLException;
    int exit_enterprise(String username) throws SQLException;
    void applyJoinEnterprise(String username,String group_name) throws SQLException;
    int searchIfExist(String username) throws SQLException;
    boolean deleteEnterprise(String username) throws SQLException;
    boolean ismoneyEnough(String username,double money) throws SQLException;
    String serachEnterprise(String username) throws SQLException;
    boolean chargeMoney(String username,double money) throws SQLException;
    boolean isPrincipalOrAdministrator(String username) throws SQLException;
    String getEnterprise_name(String username) throws SQLException;
    boolean apply_administrator(String username) throws SQLException;
    int allot_money(String username,String allot_name,double money)throws SQLException;
    int pull_enter(String pull_name,String username) throws SQLException;
}
