package io.dico.dicore.saving.jsonadapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.jsonadapter.getting.GettingStrategy;

import java.io.IOException;

public class JsonAdapter<T> {
    
    private InjectionStrategy injectionStrategy;
    private GettingStrategy gettingStrategy;
    private Class<T> adaptedClass;
    
    public void write(JsonWriter writer, T object) throws IOException {
        
    }
    
    public T read(JsonReader reader) throws IOException {
        
        
        return null;
    }
    
}
