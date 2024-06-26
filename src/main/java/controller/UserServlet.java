package controller;

import Utils.mailutils;
import com.google.gson.Gson;
import dao.Impl.Personal_flowsDaoImpl;
import dao.Impl.UserDaoImpl;
import dao.Impl.UserEnterpriseDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import po.Personal_flows;
import po.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@WebServlet("/user")
public class UserServlet extends BaseServlet{
    public void login(HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("application/json");
        UserDaoImpl userDao=new UserDaoImpl();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        HttpSession session = request.getSession();
        Map<String, Object> resultMap = new HashMap<>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String Sdcard=request.getParameter("Sdcard");
        String check_code=request.getParameter("checkcode");
        String correctCheckCode = (String) request.getSession().getAttribute("checkcode");
        if(Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(Sdcard, "")|| Objects.equals(check_code, ""))
        {
            resultMap.put("success",false);
            resultMap.put("msg","所有信息不能为空！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(!check_code.equals(correctCheckCode)){
            resultMap.put("success",false);
            resultMap.put("msg","验证码错误，请重新输入！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        User user_info=new User();
        user_info.setUsername(username);
        user_info.setPassword(password);
        user_info.setSdcard(Sdcard);
        boolean flag= false;
        try {
            flag = userDao.login(user_info);
        } catch (SQLException e) {
           log.info(e.getMessage());
        }
        if(flag){
            resultMap.put("success",true);
            resultMap.put("msg","登录成功");
            resultMap.put("user",user_info);
            session.setAttribute("user", user_info); // 将用户信息存储在session中
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","登录失败，输入信息有误或者账号不存在！");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void loginOut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession();
        session.invalidate();;
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg","退出成功！");
        out.println(new Gson().toJson(resultMap));
    }
    public void register(HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Map<String, Object> resultMap = new HashMap<>();
        String username=request.getParameter("username");
        String Sdcard=request.getParameter("Sdcard");
        String nickname=request.getParameter("nickname");
        String password=request.getParameter("password");
        String check_password=request.getParameter("check_password");
        String phoneNumber=request.getParameter("phoneNumber");
        String address=request.getParameter("address");
        String email=request.getParameter("email");
        boolean flag=password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");
        if(Objects.equals(username, "")|| Objects.equals(Sdcard, "") || Objects.equals(nickname, "") || password.isEmpty() || Objects.equals(check_password, "") ||
                Objects.equals(phoneNumber, "") ||
                Objects.equals(address, "") ||
                Objects.equals(email, "")){
            resultMap.put("msg","请输入全部信息，不能为空！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(!flag){
            resultMap.put("msg","输入的密码不符合要求，请重新输入");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(!password.equals(check_password))
        {
            resultMap.put("msg","两次输入密码不一致，请重新输入！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        User user=new User(username,Sdcard,nickname,password,phoneNumber,address,email);
        UserDaoImpl userDao=new UserDaoImpl();
        try {
            if(userDao.isUsernameExists(username))
            {
                resultMap.put("success",false);
                resultMap.put("msg","注册失败!用户名已存在,请重新输入");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        try {
            if(userDao.isSdcardExists(Sdcard))
            {
                resultMap.put("success",false);
                resultMap.put("msg","注册失败!身份证号已存在,请重新输入");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        int rows= 0;
        try {
            rows = userDao.insert(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rows>0){
            resultMap.put("success",true);
            resultMap.put("msg","注册成功，请登录！");
            out.println(new Gson().toJson(resultMap));
        }
        else{
            resultMap.put("success",false);
            resultMap.put("msg","注册失败！");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void findPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        UserDaoImpl userDaoImpl=new UserDaoImpl();
        String username=request.getParameter("username");
        String Sdcard=request.getParameter("Sdcard");
        String phoneNumber=request.getParameter("phoneNumber");
        String msg= null;
        try {
            msg = userDaoImpl.findPassword(username,Sdcard,phoneNumber);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(Objects.equals(username, "") || Objects.equals(Sdcard, "") || Objects.equals(phoneNumber, ""))
        {
            resultMap.put("success",false);
            resultMap.put("msg","请输入全部信息");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(Objects.equals(msg, "找回密码失败，信息有误或账户不存在！"))
        {
            resultMap.put("success",false);
        }else{
            resultMap.put("success",true);
        }
        resultMap.put("msg",msg);
        out.println(new Gson().toJson(resultMap));
    }


    public void showInformation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // 从session中获取用户信息
        Map<String, Object> resultMap = new HashMap<>();
        if (user == null) {
            // 用户未登录，返回错误信息
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        try {
            if(Objects.equals(userDaoImpl.check_status(user.getUsername()), "封禁")){
                resultMap.put("success", false);
                resultMap.put("msg", "用户已被封禁");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        try {
            resultMap = userDaoImpl.showInformation(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        out.println(new Gson().toJson(resultMap));
    }
    public void updateInformation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String col=request.getParameter("infoType");
        String newValue=request.getParameter("newValue");
        UserDaoImpl userDaoImpl=new UserDaoImpl();
        Personal_flowsDaoImpl personalFlowsDao=new Personal_flowsDaoImpl();
        try {
            if(Objects.equals(col, "username") &&userDaoImpl.isUsernameExists(newValue)){
                resultMap.put("success", false);
                resultMap.put("msg","修改失败,用户名已存在");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        try {
            if(Objects.equals(col, "Sdcard") &&userDaoImpl.isSdcardExists(newValue)){
                resultMap.put("success", false);
                resultMap.put("msg","修改失败,身份证号已存在");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        String name=user.getUsername();
        int rs= 0;
        try {
            rs = userDaoImpl.updateInformation(user.getUsername(),col,newValue);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs>0){

            // 更新用户信息中的对应字段
            switch (col) {
                case "avatar":
                    user.setAvatat(newValue);
                    break;
                case "username":
                    user.setUsername(newValue);
                    try {
                        personalFlowsDao.updateUsername(name,newValue);
                    } catch (SQLException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "password":
                    user.setPassword(newValue);
                    break;
                case "email":
                    user.setEmail(newValue);
                    break;
                case "phoneNumber":
                    user.setPhoneNumber(newValue);
                    break;
                case "nickname":
                    user.setNickname(newValue);
                    break;
                case "Sdcard":
                    user.setSdcard(newValue);
                    break;
                case "address":
                    user.setAddress(newValue);
                    break;
            }
            // 将更新后的用户信息重新存储到会话中
            session.setAttribute("user", user);
            resultMap.put("success", true);
            resultMap.put("msg", "修改信息成功");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success", false);
            resultMap.put("msg","修改失败");
            out.println(new Gson().toJson(resultMap));
        }
    }

    public void chargeMoney(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        UserDaoImpl userDaoImpl=new UserDaoImpl();
        Personal_flows personalFlows=new Personal_flows();
        Personal_flowsDaoImpl personalFlowsDao=new Personal_flowsDaoImpl();
        double money= Double.parseDouble(request.getParameter("amount"));
        String password=request.getParameter("password");
        if(Objects.equals(password, user.getPassword())){
            boolean flag= false;
            try {
                flag = userDaoImpl.chargeMoney(user.getUsername(),money);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            if(flag) {
                personalFlows.setUsername(user.getUsername());
                personalFlows.setMoney(money);
                personalFlows.setType("充值");
                personalFlows.setObject(user.getUsername());
                try {
                    personalFlowsDao.insert(personalFlows);
                } catch (SQLException e) {
                    log.error(e.getMessage());
                }
                resultMap.put("success", true);
                resultMap.put("msg", "充值成功");
                out.println(new Gson().toJson(resultMap));

            }else{
                resultMap.put("success", false);
                resultMap.put("msg","充值失败");
                out.println(new Gson().toJson(resultMap));
            }
        }else{
            resultMap.put("success", false);
            resultMap.put("msg","充值失败，密码不正确");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void showPersonal_flows(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Personal_flows> flows = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        Personal_flowsDaoImpl personalFlowsDao=new Personal_flowsDaoImpl();
        try {
            flows=personalFlowsDao.showPersonal_flows(user.getUsername());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(flows.isEmpty())
        {
            resultMap.put("success", false);
            resultMap.put("msg", "暂无任何流水账单记录");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        String json = new Gson().toJson(flows);
        out.println(json);
    }
    public void showPersonal_enterprise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // 从session中获取用户信息
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        Map<String, Object> resultMap =userEnterpriseDao.showPersonal_enterprise(user.getUsername());
        if(resultMap.isEmpty())
        {
            resultMap.put("success",false);
            resultMap.put("msg","用户尚未加入企业群组");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        if(Objects.equals(userEnterpriseDao.check_status(user.getUsername()), "封禁")){
            resultMap.put("success",false);
            resultMap.put("msg","该企业已被封禁！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
         out.println(new Gson().toJson(resultMap));
    }
    public void exitPersonal_enterprise(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // 从session中获取用户信息
        Map<String, Object> resultMap = new HashMap<>();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        try {
            int rs=userEnterpriseDao.exit_enterprise(user.getUsername());
            if(rs>0){
                resultMap.put("success",true);
                resultMap.put("msg","退出群组成功");
                out.println(new Gson().toJson(resultMap));
            }else{
                resultMap.put("success",false);
                resultMap.put("msg","退出群组失败");
                out.println(new Gson().toJson(resultMap));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void applyJoinEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String group_name=request.getParameter("groupName");
        User user = (User) session.getAttribute("user"); // 从session中获取用户信息
        Map<String, Object> resultMap = new HashMap<>();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        UserDaoImpl userDao=new UserDaoImpl();
        try {
            if(userEnterpriseDao.searchIfExist(user.getUsername())==1){
                resultMap.put("success",false);
                resultMap.put("msg","申请失败，您已加入了一个企业群组，请先退出原来的企业群组");
                out.println(new Gson().toJson(resultMap));
                return;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        userEnterpriseDao.applyJoinEnterprise(user.getUsername(),group_name);
        try {
            userDao.updateEnterprise(group_name,user.getUsername());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        resultMap.put("success",true);
        resultMap.put("msg","申请加入成功");
        out.println(new Gson().toJson(resultMap));
    }
    public void deleteEnterprise(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        try {
            if(userEnterpriseDao.deleteEnterprise(user.getUsername())){
                resultMap.put("success",true);
                resultMap.put("msg","注销成功");
                out.println(new Gson().toJson(resultMap));
            }else{
                resultMap.put("success",false);
                resultMap.put("msg","注销失败，您不是该企业负责人");
                out.println(new Gson().toJson(resultMap));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
    public void transferMoney(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        String paymentMethod=request.getParameter("paymentMethod");
        String check_money=request.getParameter("money");
        String transfer_name=request.getParameter("transfer_name");
        String password=request.getParameter("password");
        resultMap.put("success",false);
        if(Objects.equals(paymentMethod, "") || Objects.equals(check_money, "") || Objects.equals(transfer_name, "") || Objects.equals(password, "")){
            resultMap.put("msg","请输入全部信息！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        double money= Double.parseDouble(request.getParameter("money"));
        if(!Objects.equals(user.getPassword(), password)){
            resultMap.put("msg","转账失败，密码错误！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserDaoImpl userDao=new UserDaoImpl();
        int rs= 0;
        try {
            rs = userDao.transferMoney(user.getUsername(),paymentMethod,money,transfer_name);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        if(rs==-3){
            resultMap.put("msg","转账失败，转账对象用户名不存在");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==0){
            resultMap.put("msg","转账失败，个人转账资金不足");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-1){
            resultMap.put("msg","转账失败，您尚未加入企业群组");
            out.println(new Gson().toJson(resultMap));
        }else if(rs==-2){
            resultMap.put("msg","转账失败，企业分配的个人资金不足");
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",true);
            resultMap.put("msg","转账成功");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void apply_administrator(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        UserEnterpriseDaoImpl userEnterpriseDao=new UserEnterpriseDaoImpl();
        try {
            if(userEnterpriseDao.apply_administrator(user.getUsername())){
                resultMap.put("msg","申请成功");
                out.println(new Gson().toJson(resultMap));
            }else{
                resultMap.put("msg","申请失败，你已是该企业负责人或管理员！");
                out.println(new Gson().toJson(resultMap));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
    public void apply_Unban(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        response.setContentType("application/json");
        String name = request.getParameter("name");
        String reason = request.getParameter("reason");
        try {
            mailutils.sendMail2(name,"3123009960@mail2.gdut.edu.cn",reason);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg","申请成功,请等待管理员处理！");
        PrintWriter out = response.getWriter();
        out.println(new Gson().toJson(resultMap));
    }
}
