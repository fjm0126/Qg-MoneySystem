package dao;

import po.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserDao{
     int insert(User user) throws SQLException;
     boolean login(User user) throws SQLException;
     String findPassword(String username,String Sdcard,String phoneNumber) throws SQLException;
     Map<String, Object> showInformation(User user) throws SQLException;
}
