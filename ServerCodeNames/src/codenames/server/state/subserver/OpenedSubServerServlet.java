package codenames.server.state.subserver;

import codenames.ServerUtils;
import data.server.controllers.ServerManager;
import dto.type.out.server.DtoResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="Sub servers checker state.subserver", urlPatterns = "/state/subserver")
public class OpenedSubServerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        ServerManager serverManager = ServerUtils.getServerManager(getServletContext());
        String successMsg = "";
        String errorMsg = "";

        int subServerNum = serverManager.numberOfSubServerState();
        if (subServerNum > 0) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            successMsg = "Currently - " + subServerNum + " game rooms in the server";
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            errorMsg = "No sub server found.";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<String>(successMsg, errorMsg));
    }
}
