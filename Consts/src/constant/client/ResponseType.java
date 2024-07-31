package constant.client;

import com.google.gson.reflect.TypeToken;
import dto.type.out.server.choice.DtoServerGameChoice;
import dto.type.out.server.DtoResponse;
import dto.type.out.server.DtoServerStatus;

import java.lang.reflect.Type;
import java.util.Map;

public class ResponseType {
    public final static Type STRING_BOOLEAN = new TypeToken<DtoResponse<Map<String, Boolean>>>() {}.getType();
    public final static Type STRING_STRING = new TypeToken<DtoResponse<Map<String, String>>>() {}.getType();
    public final static Type DTO_RESPONSE_STATUS = new TypeToken<DtoResponse<DtoServerStatus>>() {}.getType();
    public final static Type DTO_RESPONSE_CHOICE = new TypeToken<DtoResponse<DtoServerGameChoice>>() {}.getType();
}
