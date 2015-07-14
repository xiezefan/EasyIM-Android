package me.xiezefan.easyim.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XieZeFan on 2015/3/22 0022.
 */
public class CollectionUtil {
    public static MapBuilder map() {
        return new MapBuilder();
    }


    public static class MapBuilder {
        private Map<String, Object> map;

        public MapBuilder() {
            this.map = new HashMap<>();
        }

        public MapBuilder put(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public Map<String, Object> build() {
            return map;
        }
    }

    private CollectionUtil() {}
}
