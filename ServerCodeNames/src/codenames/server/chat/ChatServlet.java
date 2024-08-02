package codenames.server.chat;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.Utils;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.chat.DtoServerChat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Chat CodeNames", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        String errorMessage = "";

        if (user != null) {
            try {
                if(user.getGameId() == 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    errorMessage = "User not in the game! could not upload message";
                    response.getWriter().print(errorMessage);
                }else {
                    String message = Utils.fromJsonRequest(request, String.class);
                    manager.addUserMessage(user, message);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMessage = "Bad Request! could not upload message";
                response.getWriter().print(errorMessage);
            } catch (IndexOutOfBoundsException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                errorMessage = "Bad Request! could not upload message";
                response.getWriter().print(errorMessage);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorMessage = "User not authorized!";
            response.getWriter().print(errorMessage);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        String errorMessage = "";

        if (user != null) {
            try {
                DtoServerChat chatMessages = manager.getNewMessages(user);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().print(Utils.toJson(chatMessages));
            } catch (IndexOutOfBoundsException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                errorMessage = "Game not found!";
                response.getWriter().print(errorMessage);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorMessage = "User not authorized!";
            response.getWriter().print(errorMessage);
        }
    }
}
