package controller;

import com.google.gson.Gson;
import dao.Impl.UserDaoImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import po.User;
;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@WebServlet("/user")
public class UserServlet extends BaseServlet{
    //User user_info=new User();
    public void login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        response.setContentType("application/json");
        UserDaoImpl userDao=new UserDaoImpl();
        PrintWriter out = response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String Sdcard=request.getParameter("Sdcard");
        if(Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(Sdcard, ""))
        {
            resultMap.put("success",false);
            resultMap.put("msg","所有信息不能为空！");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        User user_info=new User();
        user_info.setUsername(username);
        user_info.setPassword(password);
        user_info.setSdcard(Sdcard);
        boolean flag=userDao.login(user_info);
        if(flag){
            resultMap.put("success",true);
            resultMap.put("msg","登录成功");
            resultMap.put("user",user_info);
            HttpSession session= request.getSession();
            session.setAttribute("user", user_info); // 将用户信息存储在session中
            out.println(new Gson().toJson(resultMap));
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","登录失败，输入信息有误或者账号不存在！");
            out.println(new Gson().toJson(resultMap));
        }
    }
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        session.invalidate();;
        PrintWriter out = response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg","退出成功！");
        out.println(new Gson().toJson(resultMap));
    }
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
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
        int rows=userDao.insert(user);
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
    public void findPassword(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        UserDaoImpl userDaoImpl=new UserDaoImpl();
        String username=request.getParameter("username");
        String Sdcard=request.getParameter("Sdcard");
        String phoneNumber=request.getParameter("phoneNumber");
        String msg=userDaoImpl.findPassword(username,Sdcard,phoneNumber);
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
    public void showInformation(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // 从session中获取用户信息
        if (user == null) {
            // 用户未登录，返回错误信息
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录");
            out.println(new Gson().toJson(resultMap));
            return;
        }
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        Map<String, Object> resultMap = userDaoImpl.showInformation(user);
        out.println(new Gson().toJson(resultMap));
    }
}
