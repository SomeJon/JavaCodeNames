package dto.type.in.response;

public class StringResponse implements Response {
    private String str;

    public String getStr() {
        return str;
    }

    public StringResponse() {
        str = null;
    }

    public StringResponse(String i_str) {
        this.str = i_str;
    }

    @Override
    public void loadResponse(Response i_Response) {
        str = ((StringResponse)i_Response).getStr();
    }

    @Override
    public boolean receivedResponse() {
        return str != null;
    }
}
