package dto.type.in.response;

public class GuesserResponse implements Response{
    private Integer CardId;

    public Integer getCardId() {
        return CardId;
    }

    public GuesserResponse() {
        CardId = null;
    }

    public GuesserResponse(int cardId) {
        this.CardId = cardId;
    }

    @Override
    public void loadResponse(Response i_Response) {
        CardId = ((GuesserResponse)i_Response).getCardId();
    }

    @Override
    public boolean receivedResponse() {
        return CardId != null;
    }
}
