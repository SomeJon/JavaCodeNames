package codenames.server.load;


import codenames.ServerUtils;
import codenames.SessionUtils;
import codenames.constant.response.Responses;
import data.server.controllers.ServerManager;
import data.server.data.ePermission;
import data.user.User;
import dto.type.in.response.LoadFilesResponse;
import dto.type.out.server.DtoResponse;
import exception.CodeNameException;
import exception.loadxml.OutOfBoundLoad;
import exception.loadxml.TeamNamesNotUnique;
import exception.server.NameTaken;
import exception.server.NotEnoughRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Loading", urlPatterns = "/load")
@MultipartConfig
public class LoadFilesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String success = "";
        String failedReason = "";
        String error = "";
        Map<String, String> result = new HashMap<>();
        User user = SessionUtils.getUser(request);

        if(user != null) {
            if(user.getPermissionLevel() == ePermission.Admin) {
                try {
                    Part xmlFilePart = request.getPart("xmlFile");
                    Part txtFilePart = request.getPart("txtFile");

                    InputStream xmlInputStream = xmlFilePart.getInputStream();
                    InputStream txtInputStream = txtFilePart.getInputStream();

                    ServerManager manager = ServerUtils.getServerManager(getServletContext());

                    manager.loadSubServerData(new LoadFilesResponse(xmlInputStream, txtInputStream));

                    success = "Files uploaded and processed successfully!";
                    result.put(Responses.CREATED, success);
                    response.setStatus(HttpServletResponse.SC_CREATED);

                } catch (CodeNameException e) {
                    CodeNameException.ExceptionType type = e.getType();
                    error = "An error occurred while loading files!";
                    if(e instanceof NotEnoughRole) {
                        NotEnoughRole errorObj = (NotEnoughRole) e;
                        failedReason = "Not Enough " + errorObj.getRole() + " for team " + errorObj.getTeamName();
                    }
                    if(e instanceof NameTaken) {
                        NameTaken errorObj = (NameTaken) e;
                        failedReason = "Game name taken by another opened game! opened game name- "
                                + ((NameTaken) e).getName();
                    }
                    if(e instanceof OutOfBoundLoad) {
                        OutOfBoundLoad errorObj = (OutOfBoundLoad) e;
                        failedReason = "Entered " + errorObj.getParameterName() + " with value " +
                                errorObj.getParameterValue() + " while expected value to be in range: (" +
                                errorObj.getMin() + " - " + errorObj.getMax() + ")";
                    }
                    if(e instanceof TeamNamesNotUnique) {
                        TeamNamesNotUnique errorObj = (TeamNamesNotUnique) e;
                        failedReason = "Error is the result of non unique team names, " +
                                "entered team names were: " + String.join(", ", errorObj.getNonUniqueNames());
                    }

                    result.put(Responses.ERROR_ENCOUNTERED, failedReason);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } catch (IOException e) {
                    error = "File upload error: " + e.getMessage();
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } catch (JAXBException e) {
                    error = "Processing error: " + e.getMessage();
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else
                error = "Access denied! only an administrator can load files";
        }
        else
            error = "User not logged in!";

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(result, error));
    }
}


