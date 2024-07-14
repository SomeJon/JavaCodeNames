package dto.type.in.response;

import java.io.File;

public class LoadXmlResponse implements Response{
    private File InputFile;

    public LoadXmlResponse() {
        InputFile = null;
    }

    public LoadXmlResponse(File inputFile) {
        InputFile = inputFile;
    }

    public File getInputFile() {
        return InputFile;
    }

    @Override
    public void loadResponse(Response i_Response) {
        InputFile = ((LoadXmlResponse)i_Response).InputFile;
    }

    @Override
    public boolean receivedResponse() {
        return InputFile != null;
    }
}
