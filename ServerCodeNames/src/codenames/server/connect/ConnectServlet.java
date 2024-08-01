package codenames.server.connect;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.Utils;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.in.response.ResponseJoin;
import exception.server.NoSpot;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Connecting Servlet connect", urlPatterns = "/connect")
public class ConnectServlet extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        String errorMessage = "";
        ResponseJoin received = null;

        try {
            received = Utils.fromJsonRequest(request, ResponseJoin.class);
        } catch(IOException ignore){}

        if (received.receivedResponse() && user != null) {
            try {
                manager.joinGame(user, received.getGameId(), received.getTeamId(), received.getRoleChoice());
                response.setStatus(HttpServletResponse.SC_OK);
            } catch(NoSpot error){
                response.setStatus(HttpServletResponse.SC_GONE);
                switch (error.getCase()){
                    case Role:
                        errorMessage = "Role is already full!";
                        break;
                    case Team:
                        errorMessage = "Team is already full!";
                        break;
                    case Game:
                        errorMessage = "Game is already full!";
                        break;
                }
            } catch(IndexOutOfBoundsException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMessage = "Bad info received! please recheck entered info!";
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMessage = "Unknown error received!";
                e.printStackTrace();
            }
        } else{
            if (!received.receivedResponse()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMessage = "Missing required parameters!";
            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorMessage = "User is not connected";
            }
        }

        response.getWriter().print(errorMessage);
    }
}
