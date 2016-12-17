package io.dico.dicore.saving.jsonadapter.getting;

import io.dico.dicore.saving.jsonadapter.AdapterException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;

public class ReflectiveGettingStrategy implements GettingStrategy {
    
    private final Class<?> ownerClass;
    private final Map<String, Field> fieldCache = new HashMap<>();
    
    public ReflectiveGettingStrategy(Class<?> ownerClass) {
        this.ownerClass = ownerClass;
    }
    
    private void initFields() {
        for (Field field : ownerClass.getFields()) {
            int modifiers = field.getModifiers();
            if (!isStatic(modifiers) && !isTransient(modifiers)) {
                fieldCache.put(field.getName(), field);
            }
        }
    }
    
    private Field getField(String name) throws AdapterException {
        Field result = fieldCache.get(name);
        if (result == null) {
            try {
                result = ownerClass.getField(name);
            } catch (NoSuchFieldException e) {
                throw new AdapterException(e);
            }
            fieldCache.put(name, result);
        }
        return result;
    }
    
    @Override
    public Object get(Object owner, String name) throws AdapterException {
        if (owner == null) {
                
        }
        
        Field field = getField(name);
        
        return null;
    }
}
