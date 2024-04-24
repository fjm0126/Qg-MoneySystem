package controller;

import com.google.gson.Gson;
import dao.Impl.Enterprise_flowsDaoImpl;
import dao.Impl.UserDaoImpl;
import dao.Impl.UserEnterpriseDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import po.Enterprise;
import dao.Impl.EnterpriseDaoImpl;
import po.Enterprise_flows;
import po.User;
import po.UserEnterprise;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
@Slf4j
@WebServlet("/enterprise")
public class EnterpriseServlet extends BaseServlet {
    public void showEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<Enterprise> enterpriseGroups = new ArrayList<>();
        EnterpriseDaoImpl enterpriseDao=new EnterpriseDaoImpl();
        try {
            enterpriseGroups=enterpriseDao.showEnterprise();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        String json = new Gson().toJson(enterpriseGroups);
        out.println(json);
    }
    public void searchEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String keyword=request.getParameter("keyword");
        List<Enterprise> enterpriseGroups = List.of();
        EnterpriseDaoImpl enterpriseDao=new EnterpriseDaoImpl();
        try {
            enterpriseGroups=enterpriseDao.serachEnterprise(keyword);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        String json = new Gson().toJson(enterpriseGroups);
        out.println(json);
    }
    public void createEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException{
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
        int rs= 0;
        try {
            rs = enterpriseDao.createEnterprise(enterprise);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs>0){
            try {
                userEnterpriseDao.insert(userEnterprise);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            try {
                userDao.updateEnterprise(name,username);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            resultMap.put("success",true);
            resultMap.put("msg","申请创建成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","申请创建失败");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void chargeMoney(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String check_money=request.getParameter("amount");
        String password=request.getParameter("password");
        resultMap.put("success",false);
        if(Objects.equals(check_money, "") || Objects.equals(password, ""))
        {
            resultMap.put("msg","请输入全部信息！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        double money= Double.parseDouble(request.getParameter("amount"));
        if(!Objects.equals(password, user.getPassword())){
            resultMap.put("msg","充值失败，密码错误！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        boolean rs= false;
        try {
            rs = userEnterpriseDao.chargeMoney(user.getUsername(),money);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs) {
            resultMap.put("success", true);
            resultMap.put("msg", "充值成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("msg", "充值失败，您不是该企业负责人或管理员！");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void showEnterprise_flows(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        List<Enterprise_flows> enterpriseFlows=new ArrayList<>();
        Enterprise_flowsDaoImpl enterpriseFlowsDao=new Enterprise_flowsDaoImpl();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        String enterprise_name= null;
        try {
            enterprise_name = userEnterpriseDao.getEnterprise_name(user.getUsername());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        try {
            enterpriseFlows=enterpriseFlowsDao.showEnterprise_flows(enterprise_name);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        boolean flag= false;
        try {
            flag = userEnterpriseDao.isPrincipalOrAdministrator(user.getUsername());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(!flag){
            resultMap.put("success", false);
            resultMap.put("msg", "您不是该企业负责人或管理员，没有权限查看");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(enterpriseFlows.isEmpty()){
            resultMap.put("success", false);
            resultMap.put("msg", "暂无任何流水账单记录");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        String json = new Gson().toJson(enterpriseFlows);
        out.println(json);
    }
    public void allot_Money(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        String check_money=request.getParameter("money");
        String password=request.getParameter("password");
        String allot_name=request.getParameter("name");
        resultMap.put("success",false);
        if(Objects.equals(check_money, "") || Objects.equals(password, "") || Objects.equals(allot_name, "")){
            resultMap.put("msg","请输入全部信息！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(!Objects.equals(password, user.getPassword())){
            resultMap.put("msg","分配失败，密码错误！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        double money= Double.parseDouble(request.getParameter("money"));
        int rs= 0;
        try {
            rs = userEnterpriseDao.allot_money(user.getUsername(),allot_name,money);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs==-1){
            resultMap.put("msg","您不是该企业负责人或管理员，无法分配资金！");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-2){
            resultMap.put("msg","分配失败，企业中不存在此分配用户！");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-3){
            resultMap.put("msg","分配失败，企业可分配资金不足！");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success", true);
            resultMap.put("msg", "分配成功！");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void pull_enter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        String pull_name=request.getParameter("name");
        resultMap.put("success",false);
        if(Objects.equals(pull_name, "")){
            resultMap.put("msg","请输入用户名！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        int rs= 0;
        try {
            rs = userEnterpriseDao.pull_enter(pull_name,user.getUsername());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs==-1){
            resultMap.put("msg","您不是该企业管理员或负责人，无法拉人");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-2){
            resultMap.put("msg","拉取对象已经加入群组，拉入失败！");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-3){
            resultMap.put("msg","拉取对象用户不存在！");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",true);
            resultMap.put("msg","拉入群组成功！");
            out.println(new Gson().toJson(resultMap));
        }
    }
}
