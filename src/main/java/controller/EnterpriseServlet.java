package controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import po.Enterprise;
import dao.Impl.EnterpriseDaoImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@WebServlet("/enterprise")
public class EnterpriseServlet extends BaseServlet {
    public void showEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<Enterprise> enterpriseGroups = new ArrayList<>();
        EnterpriseDaoImpl enterpriseDao=new EnterpriseDaoImpl();
        enterpriseGroups=enterpriseDao.showEnterprise();
        String json = new Gson().toJson(enterpriseGroups);
        out.println(json);
    }
    public void searchEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String keyword=request.getParameter("keyword");
        List<Enterprise> enterpriseGroups;
        EnterpriseDaoImpl enterpriseDao=new EnterpriseDaoImpl();
        enterpriseGroups=enterpriseDao.serachEnterprise(keyword);
        String json = new Gson().toJson(enterpriseGroups);
        out.println(json);
    }
}
