package utils;

import com.google.gson.Gson;

public class JsonMapper {
    private static Gson mapper;

    private JsonMapper() {}

    public static Gson getMapper(){
        if(mapper == null){
            mapper = new Gson();
        }
        return mapper;
    }
}
