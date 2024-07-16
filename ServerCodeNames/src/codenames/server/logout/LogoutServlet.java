package codenames.server.logout;


import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.constant.response.Responses;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.DtoResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = SessionUtils.getUser(request);
        Map<String, Boolean> result = new HashMap<>();
        String errorMsg = "";

        if (user != null) {
            ServerManager manager = ServerUtils.getServerManager(getServletContext());
            manager.removeUser(user);
            result.put(Responses.DELETED, true);
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            result.put(Responses.DELETED, false);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            errorMsg = "User not found";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(result, errorMsg));
    }
}
