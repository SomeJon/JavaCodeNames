package dto.type.in.roles;

import dto.Dto;

public class DtoRole implements Dto {
    public enum eDtoRole {
        GUESSER,
        DEFINER
    }

    private final eDtoRole eRolesResponse;

    public DtoRole(eDtoRole eRolesResponse) {
        this.eRolesResponse = eRolesResponse;
    }

    public eDtoRole geteRolesResponse() {
        return eRolesResponse;
    }
}
