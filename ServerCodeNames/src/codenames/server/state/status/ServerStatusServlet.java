package codenames.server.state.status;

import codenames.ServerUtils;
import codenames.Utils;
import constant.attribute.AttributeNames;
import data.server.controllers.ServerManager;
import dto.type.out.server.DtoResponse;
import dto.type.out.server.DtoServerStatus;
import dto.type.out.server.DtoSubServerStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Server Status state.status", urlPatterns = "/state/status")
public class ServerStatusServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to fetch the server status.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(getServletContext());
        String requestedState = request.getParameter(AttributeNames.WANTED_STATUS);
        DtoServerStatus serverStatus = new DtoServerStatus();
        String errorMsg = "";

        if (requestedState != null) {
            List<DtoSubServerStatus> subServerStatuses = manager.getServerStatus().getSubServerStatus();
            List<DtoSubServerStatus> filteredStatuses = Utils
                    .filterByState(subServerStatuses.stream(), requestedState)
                    .collect(Collectors.toList());

            serverStatus.setSubServerStatus(filteredStatuses);

            if (filteredStatuses.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                errorMsg = manager.hasGame() ? "There are no matching games" : "There are no loaded games";
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorMsg = "No parameter was loaded";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(serverStatus, errorMsg));
    }
}
