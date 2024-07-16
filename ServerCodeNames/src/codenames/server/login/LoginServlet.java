package codenames.server.login;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.constant.attribute.AttributeNames;
import codenames.constant.response.Responses;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.DtoResponse;
import exception.server.NameTaken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name="Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        User user = SessionUtils.getUser(request);
        Map<String, Boolean> result = new HashMap<>();
        String errorMsg = "";

        if (user == null) {
            String username = request.getParameter(AttributeNames.USERNAME);
            if (username == null || username.isEmpty()) {
                errorMsg = "Not received a username to enter";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                username = username.trim();
                ServerManager manager = ServerUtils.getServerManager(getServletContext());
                try {
                    user = manager.userEntry(username);
                    request.getSession(true).setAttribute(AttributeNames.USER, user);
                    result.put(Responses.CREATED, true);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (NameTaken errorObj){
                    result.put("Username " + errorObj.getName() + " is already taken.", true);
                    errorMsg = "Username " + errorObj.getName() + " is already taken";
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        } else{
            response.setStatus(HttpServletResponse.SC_CONTINUE);
            errorMsg = "User name is already connected, you can continue";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(result, errorMsg));
    }
}
