package com.Szmygt.app.vr.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by pengxinkai on 17-3-30.
 */
public class ObjectStore {
    private static ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

    public static <T> T get(String key, Class<T> cls) {
        Object obj = map.get(key);

        if (obj == null)
            return null;

        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        } else {
            return null;
        }
    }

    public static Object get(String key) {
        Object obj = map.get(key);

        return obj;
    }

    public static void put(String key, Object obj) {
        map.put(key, obj);
    }

    public static void clear() {
        synchronized (map) {
            for (String key : map.keySet()) {
                Object obj = map.get(key);
            }

            map.clear();
        }
    }
}
