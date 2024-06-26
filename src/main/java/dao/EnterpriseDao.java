package dao;

import po.Enterprise;

import java.sql.SQLException;
import java.util.List;

public interface EnterpriseDao {
    List<Enterprise> showEnterprise() throws SQLException;
    List<Enterprise> serachEnterprise(String keyword) throws SQLException;
    int createEnterprise(Enterprise enterprise) throws SQLException;
    int transfer_money(String username,String enterpriseName, String transfer_name, double money)throws SQLException;
}
