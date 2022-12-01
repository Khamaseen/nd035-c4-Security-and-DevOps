package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object injectableInstance) throws NoSuchFieldException, IllegalAccessException {

        boolean fieldWasPrivate = false;

        Field f = target.getClass().getDeclaredField(fieldName);
        if (!f.isAccessible()) {
            f.setAccessible(true);
            fieldWasPrivate = true;
        }

        f.set(target, injectableInstance);

        if (fieldWasPrivate) {
            f.setAccessible(false);
        }
    }
}
