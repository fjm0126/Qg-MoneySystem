package dao;

import po.User;
import po.UserEnterprise;

import java.sql.SQLException;
import java.util.Map;

public interface UserDao{
     int insert(User user) throws SQLException;
     boolean login(User user) throws SQLException;
     String findPassword(String username,String Sdcard,String phoneNumber) throws SQLException;
     Map<String, Object> showInformation(User user) throws SQLException;
     int updateInformation(String username ,String col,String newValue) throws SQLException;
     boolean isUsernameExists(String username) throws SQLException;
     boolean isSdcardExists(String Sdcard) throws SQLException;
     boolean chargeMoney(String username,double money) throws SQLException;
     void updateEnterprise(String enterprise,String username) throws SQLException;
     int transferMoney(String username,String method,double money,String transfer_name) throws SQLException;
     boolean ismoneyEnough(String username,double money) throws SQLException;
}
