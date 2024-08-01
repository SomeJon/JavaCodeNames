package codenames.server.game.play.guesser;

import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.Utils;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.in.response.ingame.GuesserResponse;
import dto.type.out.data.DtoGuessResult;
import exception.CodeNameException;
import exception.OutOfBoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Play Guesser game.play.guesser", urlPatterns = "/game/play/guesser")
public class PlayGuesserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(request.getServletContext());
        User user = SessionUtils.getUser(request);
        String errorMessage = "";

        if(user != null) {
            synchronized (this) {
                int gameId = user.getGameId();
                if(gameId != 0) {
                    try {
                        GuesserResponse rec = Utils.fromJsonRequest(request, GuesserResponse.class);
                        DtoGuessResult result = manager.playGuess(user, rec);
                        if(result != null) {
                            response.setStatus(HttpServletResponse.SC_OK);
                            ServerUtils.moveObjectIntoResponse(response, result);
                        } else {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            errorMessage = "In case of logic error, Unexpected error!";
                            response.getWriter().print(errorMessage);
                        }
                    } catch(IOException | IndexOutOfBoundsException e){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        errorMessage = "Bad Request!";
                        response.getWriter().print(errorMessage);
                    } catch(CodeNameException error){
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        switch(error.getType()){
                            case MISMATCH_TEAM:
                                errorMessage = "This is not your team turn!";
                                break;
                            case MISMATCH_TURN_STAGE:
                                errorMessage = "Identification already logged in the server!";
                                break;
                            case MISMATCH_ROLE:
                                errorMessage = "User is not of the correct role!";
                                break;
                            case MISMATCH_UPDATE:
                                errorMessage = "Old turn state detected!";
                                break;
                            case TURN_EXCEPTION:
                                OutOfBoundException Received = (OutOfBoundException) error;
                                errorMessage = "Received card id is out of range!\nEntered "
                                        + Received.getParameterName() + " with value "
                                        + Received.getParameterValue() + " while expected value to be in range: ("
                                        + Received.getMin() + " - " + Received.getMax() + ")";
                                break;
                            case CARD_FLIPPED:
                                errorMessage = "Card id is already flipped!";
                                break;
                            case ENGINE:
                                errorMessage = "Internal turn logic error!";
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
