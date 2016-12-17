package io.dico.dicore.saving.jsonadapter.getting;

import io.dico.dicore.saving.jsonadapter.AdapterException;

public interface GettingStrategy {
    
    Object get(Object owner, String name) throws AdapterException;
    
}
