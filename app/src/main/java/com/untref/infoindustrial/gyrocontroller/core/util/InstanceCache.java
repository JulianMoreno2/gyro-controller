package com.untref.infoindustrial.gyrocontroller.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class InstanceCache {
    private static final Map<Class, Object> instances = new ConcurrentHashMap<>();

    private static <T> T getInstanceFor(Class clazz) {
        return (T) instances.get(clazz);
    }

    public static <T> T getInstanceFor(Class clazz, Supplier<T> supplier) {
        if (!instances.containsKey(clazz)) {
            instances.put(clazz, supplier.get());
        }
        return getInstanceFor(clazz);
    }

    public static void flush() {
        instances.clear();
    }
}
