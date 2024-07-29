package dto.type.in.response.load;

import dto.type.in.response.Response;

import java.io.File;

public class LoadFilesResponse implements Response {
    private File XmlFile;
    private String XmlFileName;
    private File TxtFile;
    private String TxtFileName;

    public LoadFilesResponse() {
        XmlFile = null;
        TxtFile = null;
    }

    public LoadFilesResponse(File i_XmlFile, File i_TxtFile) {
        XmlFile = i_XmlFile;
        XmlFileName = i_XmlFile.getName();
        TxtFile = i_TxtFile;
        TxtFileName = i_TxtFile.getName();
    }

    @Override
    public void loadResponse(Response i_Response) {
        XmlFile = ((LoadFilesResponse)i_Response).XmlFile;
        TxtFile = ((LoadFilesResponse)i_Response).TxtFile;
        XmlFileName = XmlFile.getName();
        TxtFileName = TxtFile.getName();
    }

    @Override
    public boolean receivedResponse() {
        return XmlFile != null && TxtFile != null;
    }

    public File getXmlFile() {
        return XmlFile;
    }

    public String getXmlFileName() {
        return XmlFileName;
    }

    public File getTxtFile() {
        return TxtFile;
    }

    public String getTxtFileName() {
        return TxtFileName;
    }
}
