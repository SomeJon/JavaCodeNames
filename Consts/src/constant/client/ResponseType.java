package constant.client;

import com.google.gson.reflect.TypeToken;
import dto.type.out.server.DtoResponse;

import java.lang.reflect.Type;
import java.util.Map;

public class ResponseType {
    public final static Type STRING_BOOLEAN = new TypeToken<DtoResponse<Map<String, Boolean>>>() {}.getType();
    public final static Type STRING_STRING = new TypeToken<DtoResponse<Map<String, String>>>() {}.getType();

}
