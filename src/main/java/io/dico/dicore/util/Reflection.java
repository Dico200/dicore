package io.dico.dicore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static io.dico.dicore.util.exceptions.Exceptions.runSafeIgnoreNullPointer;
import static io.dico.dicore.util.exceptions.Exceptions.supplySafeIgnoreNullPointer;

public class Reflection {

    public static <T> T getValueInField(Object fieldOwner, String fieldName) {
        return supplySafeIgnoreNullPointer(() -> (T) getFieldNoCatch(fieldOwner.getClass(), fieldName).get(fieldOwner));
    }

    public static <T> T getValueInField(Class<?> clazz, String fieldName, Object instance) {
        return supplySafeIgnoreNullPointer(() -> (T) getFieldNoCatch(clazz, fieldName).get(instance));
    }

    public static <T> T getValueInField(Field field, Object instance) {
        return supplySafeIgnoreNullPointer(() -> (T) field.get(instance));
    }

    public static Field getField(Object fieldOwner, String fieldName) {
        return fieldOwner == null ? null : getField(fieldOwner.getClass(), fieldName);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return supplySafeIgnoreNullPointer(() -> getFieldNoCatch(clazz, fieldName));
    }

    public static void setValueInFinalField(Field f, Object owner, Object newValue) {
        runSafeIgnoreNullPointer(() -> {
            Field modifiersField = getFieldNoCatch(Field.class, "modifiers");
            modifiersField.setAccessible(true);
            int modifiers = getValueInField(modifiersField, f);
            modifiers -= modifiers & Modifier.FINAL;
            setValueInField(modifiersField, f, modifiers);
        });
    }



    public static void setValueInField(Object fieldOwner, String fieldName, Object newValue) {
        runSafeIgnoreNullPointer(() -> getFieldNoCatch(fieldOwner.getClass(), fieldName).set(fieldOwner, newValue));
    }

    public static void setValueInField(Class<?> clazz, String fieldName, Object instance, Object newValue) {
        runSafeIgnoreNullPointer(() -> getFieldNoCatch(clazz, fieldName).set(instance, newValue));
    }

    public static void setValueInField(Field field, Object instance, Object newValue) {
        runSafeIgnoreNullPointer(() -> field.set(instance, newValue));
    }

    public static Object invokeMethod(Object methodOwner, String methodName, Object... args) {
        return supplySafeIgnoreNullPointer(() -> getMethodNoCatch(methodOwner.getClass(), methodName).invoke(methodOwner, args));
    }

    public static Object invokeMethod(Class<?> clazz, String methodName, Object instance, Object... args) {
        return supplySafeIgnoreNullPointer(() -> getMethodNoCatch(clazz, methodName).invoke(instance, args));
    }

    public static Object invokeMethod(Method method, Object instance, Object... args) {
        return supplySafeIgnoreNullPointer(() -> method.invoke(instance, args));
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        return supplySafeIgnoreNullPointer(() -> getMethodNoCatch(clazz, methodName));
    }

    public static Method getMethod(Object methodOwner, String methodName) {
        return getMethod(methodOwner.getClass(), methodName);
    }

    private static Field getFieldNoCatch(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        final Field result = clazz.getDeclaredField(fieldName);
        result.setAccessible(true);
        return result;
    }

    private static Method getMethodNoCatch(Class<?> clazz, String methodName) throws NoSuchMethodException {
        final Method result = clazz.getDeclaredMethod(methodName);
        result.setAccessible(true);
        return result;
    }

}
