package dao;

import po.Personal_flows;

import java.sql.SQLException;
import java.util.List;

public interface Personal_flowsDao {
    List<Personal_flows> showPersonal_flows(String username) throws SQLException;
    void insert(Personal_flows personalFlows) throws SQLException;
    void updateUsername(String username,String new_name) throws SQLException;
}
