package Utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
/**
 * 发送邮件的工具类
 */
public class mailutils {
    /*
    发送邮件的方法， email参数， 是收信人的邮箱， emailMsg是发送的信息内容
    */
    public static void sendMail2(String name,String email, String emailMsg) throws MessagingException {

        String protocal = "smtp";//协议
        String host = "smtp.qq.com";// 主机
        String port = "465";// 端口
        String auth = "true";
        String ssl = "true";
        String debug = "true";
        String username = "2021533129@qq.com";//发送者的邮箱
        String password = "gtajytkeeawadgjd";//授权码
        Properties properties = new Properties();// new properties对象， 用于配置

        properties.setProperty("mail.transport.protocol", protocal);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.ssl.enable", ssl);
        properties.setProperty("mail.smtp.debug", debug);
        //new 一个验证类
        Authenticator authe = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("2021533129@qq.com", "gtajytkeeawadgjd");
            }
        };

        //得到会话对象
        Session session = Session.getInstance(properties,authe);

        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发送人邮箱地址
        message.setFrom(new InternetAddress(username));
        // 设置收件人地址
        message.setRecipient(RecipientType.TO, new InternetAddress(email));
        //设置标题
        message.setSubject(name+"申请解除封禁");
        message.setText(emailMsg);
        //获取邮差对象
        Transport transport = session.getTransport();
        //连接服务器
        transport.connect(username, password);
        //发送
        Transport.send(message);
        transport.close();
    }
}


