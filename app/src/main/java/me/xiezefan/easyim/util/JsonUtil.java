package me.xiezefan.easyim.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Json Util By Gosn
 * Created by XieZeFan on 2015/4/12 0012.
 */
public final class JsonUtil {
    private static Gson gson = new Gson();
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static Gson getGson() {
        return gson;
    }

    public static <T> T format(String data, Class<T> cls) {
        if (StringUtil.isEmpty(data)) {
            throw new JsonSyntaxException("Invalid JSON string.");
        }
        return gson.fromJson(data, cls);
    }

    public static <T> List<T> formatToList(String data, Class<T> cls) {
        return gson.fromJson(data, new TypeToken<List<T>>() {}.getType());
    }

    public static <T> Set<T> formatToSet(String data, Class<T> cls) {
        return gson.fromJson(data, new TypeToken<Set<T>>() {}.getType());
    }

    public static List<?> formatToList(String data) {
        return gson.fromJson(data, new TypeToken<List<?>>() {}.getType());
    }


    public static Map<String, Object> formatToMap(String data) throws JsonSyntaxException {
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
        return map;
    }

    public static List<Map<String, String>> formatToArrayMap(String data) throws IOException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        JsonReader reader = null;
        reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        reader.beginArray();
        while(reader.hasNext()) {
            reader.beginObject();
            Map<String, String> mapObj = new HashMap<String, String>();
            while (reader.hasNext()) {
                mapObj.put(reader.nextName(), reader.nextString());
            }
            result.add(mapObj);
            reader.endObject();
        }
        reader.endArray();
        return result;
    }


    public static String toJson(Object data) {
        return JsonUtil.gson.toJson(data);
    }

    public static JsonBuilder newBuilder() {
        return new JsonBuilder();
    }

    public static class JsonBuilder {
        Map<String, Object> data = new HashMap<String, Object>();

        public JsonBuilder addItem(String key, Object value) {
            data.put(key, value);
            return this;
        }

        public String build() {
            return JsonUtil.toJson(data);
        }
    }



    private JsonUtil() {}



}
