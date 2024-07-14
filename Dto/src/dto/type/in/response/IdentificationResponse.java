package dto.type.in.response;

public class IdentificationResponse implements Response{
    private String Identification;
    private int Related;


    public String getIdentification() {
        return Identification;
    }

    public int getRelated() {
        return Related;
    }

    public IdentificationResponse() {
        Identification = null;
        Related = 0;
    }

    public IdentificationResponse(String identification, int related) {
        Identification = identification;
        Related = related;
    }

    @Override
    public void loadResponse(Response i_Response) {
        IdentificationResponse response = (IdentificationResponse) i_Response;
        Related = response.Related;
        Identification = response.Identification;
    }

    @Override
    public boolean receivedResponse() {
        return Identification != null;
    }
}
