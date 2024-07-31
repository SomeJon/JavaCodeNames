package codenames.server.connect.check.update;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.server.health.HealthServlet;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.choice.DtoSubServerChoice;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Get data about a single game", urlPatterns = "/connect/check/update")
public class CheckUpdateServlet extends HealthServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer GameId;
        User user = SessionUtils.getUser(request);
        DtoSubServerChoice ret = null;

        try{
            GameId = Integer.parseInt(request.getParameter(AttributeNames.WANTED_GAME));
        } catch(NumberFormatException e){
            GameId = null;
        }

        if(GameId != null && user != null) {
            ServerManager manager = ServerUtils.getServerManager(request.getSession().getServletContext());
            if (user.getUpdates().checkBoardUpdate(manager.getGameUpdate(GameId))) {
                Integer update = 0;
                ret = manager.getGameChoice(GameId, update);
                user.getUpdates().setBoardUpdate(update);
                ServerUtils.moveObjectIntoResponse(response, ret);
                user.getUpdates().setBoardUpdate(manager.getGameUpdate(GameId));
                response.setStatus(HttpServletResponse.SC_OK);
            } else{
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
