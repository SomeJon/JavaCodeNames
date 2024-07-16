package dto.type.in.response;

public class IntResponse implements Response {
    private Integer Int;

    public Integer getInt() {
        return Int;
    }

    public IntResponse() {
        Int = null;
    }

    public IntResponse(int i_Int) {
        this.Int = i_Int;
    }

    @Override
    public void loadResponse(Response i_Response) {
        Int = ((IntResponse)i_Response).getInt();
    }

    @Override
    public boolean receivedResponse() {
        return Int != null;
    }
}
