package Chat_room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import dao.Impl.UserEnterpriseDaoImpl;
import jakarta.servlet.http.HttpServlet;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
    public class ChatServer extends HttpServlet {

        // 用于存储企业群组和对应聊天室的映射关系
        private static final Map<String, Set<Session>> groupChatRooms = new HashMap<>();

        @OnOpen
        public void onOpen(Session session,EndpointConfig config) throws SQLException{
            /*
            无法获取前端用户名而找到企业群组，进而舍弃这个方法
             */

            /*
              UserEnterpriseDaoImpl userEnterpriseDao = new UserEnterpriseDaoImpl();
              String userGroup=userEnterpriseDao.getEnterprise_name(username);
                if (username != null && userGroup != null) {
                    // 将用户添加到相应的企业群组聊天室中
                    groupChatRooms.computeIfAbsent(userGroup, k -> new HashSet<>()).add(session);
                }
*/
        }
        @OnMessage
        public void onMessage(String message, Session session) throws SQLException {
            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                    String username = parts[0].trim();
                    UserEnterpriseDaoImpl userEnterpriseDao = new UserEnterpriseDaoImpl();
                    String userGroup = userEnterpriseDao.getEnterprise_name(username);
                    if (userGroup != null) {
                        // 将用户添加到相应的企业群组聊天室中
                        groupChatRooms.computeIfAbsent(userGroup, k -> new HashSet<>()).add(session);
                    }
                // 从会话中找到用户所在的群组
                for (Map.Entry<String, Set<Session>> entry : groupChatRooms.entrySet()) {
                    if (entry.getValue().contains(session)) {
                        userGroup = entry.getKey();
                        break;
                    }
                }

                if (userGroup != null) {
                    // 将消息广播给同一群组中的所有会话
                    broadcast(userGroup, message);
                } else {
                    System.err.println("无法找到用户所在的群组: " + session.getId());
                }
            } else {
                System.err.println("接收到的消息格式无效: " + message);
            }
        }


    @OnClose
        public void onClose(Session session) {
            // 处理用户断开连接
            for (Map.Entry<String, Set<Session>> entry : groupChatRooms.entrySet()) {
                Set<Session> sessions = entry.getValue();
                sessions.remove(session);
            }
        }

        private void broadcast(String userGroup, String message) {
            Set<Session> sessions = groupChatRooms.get(userGroup);
            if (sessions != null) {
                for (Session session : sessions) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
