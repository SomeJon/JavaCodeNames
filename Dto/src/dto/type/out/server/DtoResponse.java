package dto.type.out.server;

import dto.Dto;

public class DtoResponse<T> implements Dto {
    private final T result;
    private final String errorMessage;

    public DtoResponse(T result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}