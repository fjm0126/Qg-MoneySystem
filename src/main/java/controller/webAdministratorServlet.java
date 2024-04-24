package controller;

import ConnectionPool.ComeTrueConnectionpool;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import po.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
@Slf4j
@WebServlet("/administrator")
public class webAdministratorServlet extends BaseServlet{
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        Map<String, Object> resultMap = new HashMap<>();
        String password=request.getParameter("password");
        if(Objects.equals(password, "")){
            resultMap.put("success",false);
            resultMap.put("msg","请输入密钥！");
            out.println(new Gson().toJson(resultMap));
            return;
        }

            ComeTrueConnectionpool comeTrueConnectionpool = new ComeTrueConnectionpool();
            comeTrueConnectionpool.initialConnectionpool();
            conn = comeTrueConnectionpool.getconnection();
            String sql = "select password from websiteadministrator";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String check_password = rs.getString("password");
                if (!Objects.equals(check_password, password)) {
                    resultMap.put("success", false);
                    resultMap.put("msg", "登录失败,密钥错误");
                    out.println(new Gson().toJson(resultMap));
                } else {
                    resultMap.put("success", true);
                    resultMap.put("msg", "登录成功");
                    out.println(new Gson().toJson(resultMap));
                }
            }
    }
    public void showUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs=null;
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="select * from user ";
        stmt=conn.prepareStatement(sql);
        rs=stmt.executeQuery();
        while(rs.next()){
            User user=new User();
            user.setUsername(rs.getString("username"));
            user.setStatus(rs.getString("status"));
            users.add(user);
        }
        String json = new Gson().toJson(users);
        out.println(json);
    }
    public void banEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        Connection conn = null;
        PreparedStatement stmt = null;
        Map<String, Object> resultMap = new HashMap<>();
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update enterprisegroup set status='封禁' where name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,name);
        int rs=stmt.executeUpdate();
        if(rs>0){
            resultMap.put("success",true);
            resultMap.put("msg","封禁成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","封禁失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void unbanEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        Connection conn = null;
        PreparedStatement stmt = null;
        Map<String, Object> resultMap = new HashMap<>();
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update enterprisegroup set status='正常' where name=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,name);
        int rs=stmt.executeUpdate();
        if(rs>0){
            resultMap.put("success",true);
            resultMap.put("msg","解封成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","解封失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void banUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        Connection conn = null;
        PreparedStatement stmt = null;
        Map<String, Object> resultMap = new HashMap<>();
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update user set status='封禁' where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,name);
        int rs=stmt.executeUpdate();
        if(rs>0){
            resultMap.put("success",true);
            resultMap.put("msg","封禁成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","封禁失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void unbanUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name");
        Connection conn = null;
        PreparedStatement stmt = null;
        Map<String, Object> resultMap = new HashMap<>();
        ComeTrueConnectionpool comeTrueConnectionpool=new ComeTrueConnectionpool();
        comeTrueConnectionpool.initialConnectionpool();
        conn=comeTrueConnectionpool.getconnection();
        String sql="update user set status='正常' where username=?";
        stmt=conn.prepareStatement(sql);
        stmt.setObject(1,name);
        int rs=stmt.executeUpdate();
        if(rs>0){
            resultMap.put("success",true);
            resultMap.put("msg","解封成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","解封失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
}
