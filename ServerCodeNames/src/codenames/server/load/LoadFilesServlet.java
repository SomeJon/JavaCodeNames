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
import exception.server.BadExtension;
import exception.server.NameTaken;
import exception.server.NotEnoughRole;
import exception.server.TxtFileNotMatch;
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
        String failedReason = "";
        String error = "";
        Map<String, String> result = new HashMap<>();
        User user = SessionUtils.getAdminUser(request);

        if (user != null) {
            try {
                Part xmlFilePart = request.getPart("xmlFile");
                Part txtFilePart = request.getPart("txtFile");

                if(!xmlFilePart.getSubmittedFileName().endsWith(".xml")) {
                    throw new BadExtension(".xml",
                            xmlFilePart.getSubmittedFileName());
                }
                if(!txtFilePart.getSubmittedFileName().endsWith(".txt")) {
                    throw new BadExtension(".txt",
                            txtFilePart.getSubmittedFileName());
                }

                InputStream xmlInputStream = xmlFilePart.getInputStream();
                InputStream txtInputStream = txtFilePart.getInputStream();

                ServerManager manager = ServerUtils.getServerManager(getServletContext());

                manager.loadSubServerData(new LoadFilesResponse(xmlInputStream, txtInputStream,
                        txtFilePart.getSubmittedFileName()));

                result.put(Responses.CREATED, "Files uploaded and processed successfully!");
                response.setStatus(HttpServletResponse.SC_CREATED);

            } catch (CodeNameException e) {
                CodeNameException.ExceptionType type = e.getType();
                error = "An error occurred while loading files!";
                if (e instanceof NotEnoughRole) {
                    NotEnoughRole errorObj = (NotEnoughRole) e;
                    failedReason = "Not Enough " + errorObj.getRole() + " for team " + errorObj.getTeamName();
                }
                if (e instanceof NameTaken) {
                    NameTaken errorObj = (NameTaken) e;
                    failedReason = "Game name taken by another opened game! opened game name- "
                            + ((NameTaken) e).getName();
                }
                if (e instanceof OutOfBoundLoad) {
                    OutOfBoundLoad errorObj = (OutOfBoundLoad) e;
                    failedReason = "Entered " + errorObj.getParameterName() + " with value " +
                            errorObj.getParameterValue() + " while expected value to be in range: (" +
                            errorObj.getMin() + " - " + errorObj.getMax() + ")";
                }
                if (e instanceof TeamNamesNotUnique) {
                    TeamNamesNotUnique errorObj = (TeamNamesNotUnique) e;
                    failedReason = "Error is the result of non unique team names, " +
                            "entered team names were: " + String.join(", ", errorObj.getNonUniqueNames());
                }
                if (e instanceof TxtFileNotMatch){
                    TxtFileNotMatch errorObj = (TxtFileNotMatch) e;
                    failedReason = "Name of the uploaded txt file does not match the name set in the xml file: " +
                            "File name - " + errorObj.getFileName() + " Expected Name - " + errorObj.getXmlName();
                }
                if (e instanceof BadExtension) {
                    BadExtension errorObj = (BadExtension) e;
                    failedReason = "Expected extension to be "
                            + errorObj.getExpected() + " recieved the file - " + errorObj.getReceived();
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
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            error = "User need to be an admin!";
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(result, error));
    }
}


