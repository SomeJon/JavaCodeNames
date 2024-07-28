package codenames.server.state.game;

import codenames.ServerUtils;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import dto.type.out.server.Choice.DtoServerGameChoice;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Games Info Servlet", urlPatterns = "/state/game")
public class GamesInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(getServletContext());
        String requestedState = request.getParameter(AttributeNames.WANTED_STATUS);

        DtoServerGameChoice ret = null;
    }

}
