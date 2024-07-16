package dto.type.in.response;

import java.io.File;

public class LoadXmlResponse implements Response{
    private File InputXmlFile;

    public LoadXmlResponse() {
        InputXmlFile = null;
    }

    public LoadXmlResponse(File inputFile) {
        InputXmlFile = inputFile;
    }

    public File getXmlFile() {
        return InputXmlFile;
    }

    @Override
    public void loadResponse(Response i_Response) {
        InputXmlFile = ((LoadXmlResponse)i_Response).InputXmlFile;
    }

    @Override
    public boolean receivedResponse() {
        return InputXmlFile != null;
    }
}
