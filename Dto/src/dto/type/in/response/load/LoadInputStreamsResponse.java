package dto.type.in.response.load;

import dto.type.in.response.Response;
import dto.type.out.server.DtoServerInfo;

import java.io.InputStream;

public class LoadInputStreamsResponse implements Response {
    private InputStream TxtInputStream;
    private InputStream XmlInputStream;
    private DtoServerInfo DtoToLoad;
    private String TxtFileName;

    public LoadInputStreamsResponse(InputStream i_XmlInputStream, InputStream i_TxtInputStream, String i_TxtFileName) {
        XmlInputStream = i_XmlInputStream;
        TxtInputStream = i_TxtInputStream;
        TxtFileName = i_TxtFileName;
        DtoToLoad = new DtoServerInfo();
    }

    public String getTxtFileName() {
        return TxtFileName;
    }

    public InputStream getTxtInputStream() {
        return TxtInputStream;
    }

    public InputStream getXmlInputStream() {
        return XmlInputStream;
    }

    @Override
    public void loadResponse(Response i_Response) {
        TxtInputStream = ((LoadInputStreamsResponse)i_Response).TxtInputStream;
        XmlInputStream = ((LoadInputStreamsResponse)i_Response).XmlInputStream;
    }

    @Override
    public boolean receivedResponse() {
        return XmlInputStream != null && TxtInputStream != null;
    }

    public DtoServerInfo getDtoToLoad() {
        return DtoToLoad;
    }
}
