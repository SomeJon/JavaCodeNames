package dto.type.out.server;

import dto.Dto;

import java.util.List;

public class DtoServerStatus implements Dto {
    private List<DtoSubServerStatus> subServerStatus;

    public DtoServerStatus() {
    }

    public DtoServerStatus(List<DtoSubServerStatus> subServerStatus) {
        this.subServerStatus = subServerStatus;
    }

    public List<DtoSubServerStatus> getSubServerStatus() {
        return subServerStatus;
    }

    public void setSubServerStatus(List<DtoSubServerStatus> i_subServerStatus) {
        subServerStatus = i_subServerStatus;
    }
}
