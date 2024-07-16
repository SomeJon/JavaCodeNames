package dto.type.in.response;

import dto.type.out.server.DtoServerInfo;

import java.io.File;
import java.io.InputStream;

public class LoadFilesResponse implements Response {
    private InputStream TxtInputStream;
    private InputStream XmlInputStream;
    private DtoServerInfo DtoToLoad;

    public LoadFilesResponse(InputStream i_XmlInputStream, InputStream i_TxtInputStream) {
        XmlInputStream = i_XmlInputStream;
        TxtInputStream = i_TxtInputStream;
        DtoToLoad = new DtoServerInfo();
    }

    public InputStream getTxtInputStream() {
        return TxtInputStream;
    }

    public InputStream getXmlInputStream() {
        return XmlInputStream;
    }

    @Override
    public void loadResponse(Response i_Response) {
        TxtInputStream = ((LoadFilesResponse)i_Response).TxtInputStream;
        XmlInputStream = ((LoadFilesResponse)i_Response).XmlInputStream;
    }

    @Override
    public boolean receivedResponse() {
        return XmlInputStream != null && TxtInputStream != null;
    }

    public DtoServerInfo getDtoToLoad() {
        return DtoToLoad;
    }
}
