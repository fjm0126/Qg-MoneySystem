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
}
