package codenames.server.state.game;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.Utils;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.out.server.Choice.DtoServerGameChoice;
import dto.type.out.server.Choice.DtoSubServerChoice;
import dto.type.out.server.DtoResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name="Games Info Servlet", urlPatterns = "/state/game")
public class GamesInfoServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to fetch game information.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(getServletContext());
        String requestedState = request.getParameter(AttributeNames.WANTED_STATUS);
        User user = SessionUtils.getUser(request);
        DtoServerGameChoice gameChoices = new DtoServerGameChoice();
        String errorMsg = "";

        if (user != null) {
            if (requestedState != null) {
                if (user.getServerUpdate() < manager.getUpdateCount()) {
                    List<DtoSubServerChoice> filteredChoices = Utils.getFilteredChoices(manager, requestedState);

                    gameChoices.setSubServerChoices(filteredChoices);

                    if (filteredChoices.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        errorMsg = manager.hasGame() ? "There are no matching games" : "There are no loaded games";
                    } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                } else{
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMsg = "No parameter was loaded";
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorMsg = "User is not logged in!";
        }
        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(gameChoices, errorMsg));
    }
}
