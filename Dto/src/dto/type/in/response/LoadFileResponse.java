package dto.type.in.response;

import java.io.File;

public class LoadFileResponse implements Response{
    private File InputFile;
    private String FileName;

    public LoadFileResponse() {
        InputFile = null;
    }

    public LoadFileResponse(File inputFile) {
        InputFile = inputFile;
        FileName = inputFile.getName();
    }

    @Override
    public void loadResponse(Response i_Response) {
        InputFile = ((dto.type.in.response.LoadFileResponse)i_Response).InputFile;
        FileName = InputFile.getName();
    }

    @Override
    public boolean receivedResponse() {
        return InputFile != null;
    }

    public File getInputFile() {
        return InputFile;
    }

    public String getFileName() {
        return FileName;
    }
}
