package controller;

import com.google.gson.Gson;
import dao.Impl.UserDaoImpl;
import dao.Impl.UserEnterpriseDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import po.Enterprise;
import dao.Impl.EnterpriseDaoImpl;
import po.UserEnterprise;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

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
    public void createEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        String username=request.getParameter("username");
        String member=request.getParameter("member_count");
        int member_count= Integer.parseInt(request.getParameter("member_count"));
        String scale=request.getParameter("scale");
        String direction=request.getParameter("direction");
        String access_mode=request.getParameter("access_mode");
        Map<String, Object> resultMap = new HashMap<>();
        UserDaoImpl userDao=new UserDaoImpl();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        UserEnterprise userEnterprise=new UserEnterprise();
        userEnterprise.setUsername(username);
        userEnterprise.setEnterprise_group_name(name);
        userEnterprise.setRole("负责人");
        if(Objects.equals(name, "") || Objects.equals(username, "") || Objects.equals(member, "") || Objects.equals(scale, "") || Objects.equals(direction, "") || Objects.equals(access_mode, "")){
            resultMap.put("success",false);
            resultMap.put("msg","请输入全部信息");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        EnterpriseDaoImpl enterpriseDao=new EnterpriseDaoImpl();
        Enterprise enterprise=new Enterprise(name,member_count,scale,username,direction,access_mode);
        int rs=enterpriseDao.createEnterprise(enterprise);
        if(rs>0){
            userEnterpriseDao.insert(userEnterprise);
            userDao.updateEnterprise(name,username);
            resultMap.put("success",true);
            resultMap.put("msg","申请创建成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","申请创建失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
}
