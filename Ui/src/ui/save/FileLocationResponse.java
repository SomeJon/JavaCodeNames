package ui.save;

import dto.type.in.response.Response;

public class FileLocationResponse implements Response {
    private String FileLocation;

    public String getFileLocation() {
        return FileLocation;
    }

    public FileLocationResponse() {
    }

    public FileLocationResponse(String i_FileLocation) {
        this.FileLocation = i_FileLocation;
    }

    @Override
    public void loadResponse(Response i_Response) {
        this.FileLocation = ((FileLocationResponse)i_Response).FileLocation;
    }

    @Override
    public boolean receivedResponse() {
        return FileLocation != null;
    }
}
