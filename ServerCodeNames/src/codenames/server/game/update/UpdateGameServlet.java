package codenames.server.game.update;

import codenames.ServerUtils;
import codenames.SessionUtils;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.Dto;
import dto.type.out.server.game.DtoGameUpdate;
import exception.server.Unauthorized;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Update game", urlPatterns = "/game/update")
public class UpdateGameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(getServletContext());
        User user = SessionUtils.getUser(request);

        if (user != null) {
            try {
                DtoGameUpdate update = manager.getUpdates(user);
                if (update != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    ServerUtils.moveObjectIntoResponse(response, update);
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } catch(Unauthorized e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Dto result = user.getEndResult();
                ServerUtils.moveObjectIntoResponse(response, result);
            }
        }else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
