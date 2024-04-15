package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class BaseServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        //获取请求标识
        String methodName=request.getParameter("method");
        //获取指定类的字节码对象
        Class<? extends BaseServlet> clazz=this.getClass();
        //通过类的字节码对象获取方法的字节码对象
        try {
            Method method=clazz.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            //让方法执行
            method.invoke(this,request,response);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void doPost(HttpServletRequest request,HttpServletResponse response){
        doGet(request,response);
    }
}
