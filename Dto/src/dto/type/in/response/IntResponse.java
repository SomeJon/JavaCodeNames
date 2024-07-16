package dto.type.in.response;

public class IntResponse implements Response {
    private Integer Int;

    public Integer getInt() {
        return Int;
    }

    public IntResponse() {
        Int = null;
    }

    public IntResponse(int cardId) {
        this.Int = cardId;
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
