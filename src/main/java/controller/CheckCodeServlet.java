package controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet("/CheckCodeServlet")
public class CheckCodeServlet extends HttpServlet {

    // 定义验证码的长度
    private static final int CODE_LENGTH = 4;
    // 定义验证码图片的宽度和高度
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    // 定义验证码的字符集
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应内容类型为图片
        response.setContentType("image/jpeg");

        // 创建一个图片缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 获取图片的绘图对象
        Graphics2D graphics = image.createGraphics();

        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        Random random = new Random();
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            graphics.drawLine(x1, y1, x2, y2);
        }
        // 生成随机验证码
        String checkCode = generateCheckCode(CODE_CHARACTERS, CODE_LENGTH);

        // 将验证码保存在会话中
        HttpSession session = request.getSession();
        session.setAttribute("checkcode", checkCode);

        // 设置字体样式
        graphics.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        // 绘制验证码字符
        for (int i = 0; i < CODE_LENGTH; i++) {
            graphics.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
            graphics.drawString(String.valueOf(checkCode.charAt(i)), (i * (WIDTH / CODE_LENGTH)) + 5, HEIGHT - 5);
        }

        // 释放绘图对象
        graphics.dispose();

        // 将图片输出到客户端
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        outputStream.close();
    }

    // 生成指定长度的随机验证码
    private String generateCheckCode(String codeCharacters, int length) {
        Random random = new Random();
        StringBuilder checkCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            checkCode.append(codeCharacters.charAt(random.nextInt(codeCharacters.length())));
        }
        return checkCode.toString();
    }
}

