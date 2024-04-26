package dao;

import po.Enterprise_flows;
import po.Personal_flows;

import java.sql.SQLException;
import java.util.List;

public interface Enterprise_flowsDao {
    void insert(Enterprise_flows enterprise_flows) throws SQLException;
    List<Enterprise_flows> showEnterprise_flows(String enterprise_name) throws SQLException;
}
