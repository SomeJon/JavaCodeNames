package codenames.server.state.status;

import codenames.ServerUtils;
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
import java.util.stream.Collectors;

@WebServlet(name = "Server Status", urlPatterns = "/state/status")
public class ServerStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerManager manager = ServerUtils.getServerManager(getServletContext());
        String requestedState = request.getParameter(AttributeNames.WANTED_STATUS);
        DtoServerStatus ret = new DtoServerStatus();
        String errorMsg = "";

        if (requestedState != null) {
            ret.setSubServerStatus(manager.getServerStatus().getSubServerStatus());
            ret.setSubServerStatus(ret.getSubServerStatus().stream()
                    .filter(T -> {
                        if (requestedState.equals(AttributeNames.ACTIVE))
                            return T.isActive();
                        else if (requestedState.equals(AttributeNames.PENDING))
                            return !T.isActive();
                        else return true;
                    }).collect(Collectors.toList()));
            if(ret.getSubServerStatus().isEmpty()){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                errorMsg = "There are no matching games";
            }else{
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorMsg = "No parameter was loaded";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(ret, errorMsg));
    }
}
