package io.dico.dicore.util.translation;

public interface Message/*<T extends Enum<T> & Message<T>>*/ {
    
    String name();
    
    void setTo(String message);
    
    String getValue();
    
    String getDefault();
    
}
