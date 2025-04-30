package codenames.server.load;


import codenames.ServerUtils;
import codenames.SessionUtils;
import constant.attribute.AttributeNames;
import constant.response.Responses;
import data.server.controllers.ServerManager;
import data.user.User;
import dto.type.in.response.load.LoadInputStreamsResponse;
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
import java.util.stream.Collectors;

@WebServlet(name = "Loading load", urlPatterns = "/load")
@MultipartConfig
public class LoadFilesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String failedReason = "";
        Map<String, String> result = new HashMap<>();
        User user = SessionUtils.getAdminUser(request);
        InputStream xmlInputStream = null;
        InputStream txtInputStream = null;

        if (user != null) {
            try {
                Part xmlFilePart = request.getPart(AttributeNames.XML_FILE);
                Part txtFilePart = request.getPart(AttributeNames.TXT_File);

                if (!xmlFilePart.getSubmittedFileName().endsWith(".xml")) {
                    throw new BadExtension(".xml", xmlFilePart.getSubmittedFileName());
                }
                if (!txtFilePart.getSubmittedFileName().endsWith(".txt")) {
                    throw new BadExtension(".txt", txtFilePart.getSubmittedFileName());
                }

                try {
                    xmlInputStream = xmlFilePart.getInputStream();
                    txtInputStream = txtFilePart.getInputStream();
                    ServerManager manager = ServerUtils.getServerManager(getServletContext());
                    manager.loadSubServerData(new LoadInputStreamsResponse(xmlInputStream, txtInputStream, txtFilePart.getSubmittedFileName()));

                    result.put(Responses.CREATED, "Files uploaded and processed successfully into the server!");
                    response.setStatus(HttpServletResponse.SC_CREATED);

                } finally {
                    if (xmlInputStream != null)
                        xmlInputStream.close();
                    if (txtInputStream != null)
                        txtInputStream.close();
                }
            } catch (CodeNameException e) {
                handleCodeNameException(e, result);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (IOException | JAXBException e) {
                failedReason = "File upload error: " + e.getMessage();
                result.put(Responses.ERROR_ENCOUNTERED, failedReason);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            failedReason = "User needs to be an admin!";
            result.put(Responses.ERROR_ENCOUNTERED, failedReason);
        }

        ServerUtils.moveObjectIntoResponse(response, new DtoResponse<>(result, failedReason));
    }

    private void handleCodeNameException(CodeNameException e, Map<String, String> result) {
        String failedReason = "";
        if (e instanceof NotEnoughRole) {
            NotEnoughRole errorObj = (NotEnoughRole) e;
            failedReason = "Not Enough " + errorObj.getRole() + " for team " + errorObj.getTeamName();
        } else if (e instanceof NameTaken) {
            NameTaken errorObj = (NameTaken) e;
            failedReason = "Game name taken by another opened game! Opened game name - " + errorObj.getName();
        } else if (e instanceof OutOfBoundLoad) {
            OutOfBoundLoad errorObj = (OutOfBoundLoad) e;
            failedReason = "Entered " + errorObj.getParameterName() + " with value " + errorObj.getParameterValue() +
                    " while expected value to be in range: (" + errorObj.getMin() + " - " + errorObj.getMax() + ")";
        } else if (e instanceof TeamNamesNotUnique) {
            TeamNamesNotUnique errorObj = (TeamNamesNotUnique) e;
            failedReason = "Error is the result of non-unique team names, entered team names were: " +
                    String.join(", ", errorObj.getNonUniqueNames());
        } else if (e instanceof TxtFileNotMatch) {
            TxtFileNotMatch errorObj = (TxtFileNotMatch) e;
            failedReason = "Name of the uploaded txt file does not match the name set in the xml file\n" +
                    "File name - " + errorObj.getFileName() + "\nExpected Name - " + errorObj.getXmlName();
        } else if (e instanceof BadExtension) {
            BadExtension errorObj = (BadExtension) e;
            failedReason = "Expected extension to be " + errorObj.getExpected() + " received the file - " + errorObj.getReceived();
        }
        result.put(Responses.ERROR_ENCOUNTERED, failedReason);
    }
}


