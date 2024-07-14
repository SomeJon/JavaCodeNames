package dto.type.in.response;

import dto.Dto;

public interface Response extends Dto {
    public void loadResponse(Response i_Response);
    public boolean receivedResponse();
}
