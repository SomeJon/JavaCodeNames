package codenames.server.connect.check;

import codenames.ServerUtils;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Did game start", urlPatterns = "/connect/check")
public class CheckStartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer GameId;

        try{
            GameId = Integer.parseInt(request.getParameter(AttributeNames.WANTED_GAME));
        } catch(NumberFormatException e){
            GameId = null;
        }

        if(GameId != null) {
            try {
                ServerManager manager = ServerUtils.getServerManager(request.getSession().getServletContext());
                boolean check = manager.didGameStart(GameId);
                if (check) {
                    response.setStatus(HttpServletResponse.SC_CREATED);

                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
