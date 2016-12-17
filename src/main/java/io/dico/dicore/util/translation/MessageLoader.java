package io.dico.dicore.util.translation;

import org.bukkit.configuration.ConfigurationSection;

public class MessageLoader {
    
    private MessageLoader() {
    }
    
    public static void loadMessages(Message[] messages, ConfigurationSection config) {
        for (Message message : messages) {
            String set = config.getString(message.name());
            if (set != null) {
                message.setTo(set);
            }
        }
    }
    
    public static void setDefaultsInto(Message[] messages, ConfigurationSection section) {
        for (Message message : messages) {
            section.set(message.name(), message.getDefault());
        }
    }
    
}
