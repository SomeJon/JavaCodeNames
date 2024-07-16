package codenames.server.login.admin;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.constant.attribute.AttributeNames;
import codenames.constant.response.Responses;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.DtoResponse;
import exception.server.AdminOn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name="AdminLogin", urlPatterns = "/login/admin")
public class AdminLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        User user = SessionUtils.getUser(request);
        Map<String, Boolean> result = new HashMap<>();
        String errorMsg = "";

        if (user == null) {
            synchronized(this){
                ServerManager manager = ServerUtils.getServerManager(getServletContext());

                try {
                    User toAdd = manager.adminEntry();
                    request.getSession(true).setAttribute(AttributeNames.USER, toAdd);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    boolean noOpenGame = manager.numberOfSubServerState() == 0;
                    result.put(Responses.CREATED, true);
                    result.put(Responses.NO_SUB_SERVERS, noOpenGame);
                }
                catch(AdminOn error){
                    errorMsg = "Another admin is already online!";
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
        else{
            synchronized(this) {
                ServerManager manager = ServerUtils.getServerManager(getServletContext());

                boolean adminOn = manager.isAdminOn();
                if (adminOn) {
                    errorMsg = "Another admin went online while you were away!";
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
                else{
                    boolean noOpenGame = manager.numberOfSubServerState() == 0;
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    result.put(Responses.CREATED, true);
                    result.put(Responses.NO_SUB_SERVERS, noOpenGame);
                }
            }
        }

        ServerUtils.moveObjectIntoResponse(response , new DtoResponse<>(result, errorMsg));
    }
}
