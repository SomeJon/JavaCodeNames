package codenames.server.state.game.admin;

import codenames.ServerUtils;
import codenames.SessionUtils;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import data.server.data.ePermission;
import data.user.User;
import dto.type.out.data.DtoActiveGameStatus;
import exception.server.Unauthorized;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Games Admin Info Servlet state.game.admin", urlPatterns = "/state/game/admin")
public class ActiveGameServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        Integer GameId;
        String errorMessage = "";

        try{
            GameId = Integer.parseInt(request.getParameter(AttributeNames.WANTED_GAME));
        } catch(NumberFormatException e){
            GameId = null;
        }

        if (user != null && GameId != null) {
            if(user.getPermissionLevel() == ePermission.Admin) {
                try {
                    DtoActiveGameStatus choice = manager.getActiveGameStatus(GameId);
                    if (choice != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        ServerUtils.moveObjectIntoResponse(response, choice);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        errorMessage = "Game not found!";
                    }
                } catch (Unauthorized e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    errorMessage = "Game is not active!";
                } catch (IndexOutOfBoundsException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    errorMessage = "Bad info received! please recheck entered info!";

                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    errorMessage = "Unknown error received! " + e.getMessage();
                }
            } else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorMessage = "User has to be an admin to connect to a game";
            }
        } else{
            if (GameId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMessage = "Missing required parameters!";
            }
            else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                errorMessage = "Admin is not connected";
            }
        }

        response.getWriter().print(errorMessage);
    }
}
