package codenames.server.game.play.identifier;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.Utils;
import data.server.controllers.ServerManager;
import data.server.controllers.SubServer;
import data.user.User;
import dto.type.in.response.ingame.IdentificationResponse;
import exception.CodeNameException;
import exception.OutOfBoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Identifier Play", urlPatterns = "/game/play/identifier")
public class PlayIdentifierServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        String errorMessage = "";

        if(user != null) {
            synchronized (this) {
                int gameId = user.getGameId();
                if(gameId != 0) {
                    try {
                        IdentificationResponse rec = Utils.fromJsonRequest(request, IdentificationResponse.class);
                        manager.playIdentification(user, rec);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } catch(IOException | IndexOutOfBoundsException e){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        errorMessage = "Bad Request!";
                        response.getWriter().print(errorMessage);
                    } catch(CodeNameException error){
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        switch(error.getType()){
                            case MISMATCH_TURN_STAGE:
                                errorMessage = "Identification already logged in!";
                                break;
                            case MISMATCH_ROLE:
                                errorMessage = "User is not of the correct role!";
                                break;
                            case MISMATCH_UPDATE:
                                errorMessage = "Old turn state detected!";
                                break;
                            case TURN_EXCEPTION:
                                OutOfBoundException Received = (OutOfBoundException) error;
                                errorMessage = "Tried to give more cards to flip then is possible for team!\nEntered "
                                        + Received.getParameterName() + " with value "
                                        + Received.getParameterValue() + " while expected value to be in range: ("
                                        + Received.getMin() + " - " + Received.getMax() + ")";
                                break;
                        }

                        response.getWriter().print(errorMessage);
                    }
                } else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    errorMessage = "User not in game.";
                    response.getWriter().print(errorMessage);
                }
            }
        } else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorMessage = "Bad Request!";
            response.getWriter().print(errorMessage);
        }
    }
}
