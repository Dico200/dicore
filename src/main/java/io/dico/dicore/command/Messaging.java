package io.dico.dicore.command;

import org.bukkit.command.CommandSender;

public class Messaging {
    
    static void print(String s) {
        System.out.println(s);
    }
    
    public static void main(String[] args) {
        print(Formatting.translateChars('&', "&4&c&f&h"));
    }
    
    public static final Formatting SUCCESS = Formatting.GREEN;
    public static final Formatting EXCEPT = Formatting.YELLOW;
    
    public static final String PREFIX_FORMAT = Formatting.translateChars('&', "&4[&c%s&4] ");
    
    public static void send(CommandSender recipient, String prefix, Formatting format, String message) {
        send(recipient, PREFIX_FORMAT, prefix, format, message);
    }
    
    public static void send(CommandSender recipient, String prefixFormat, String prefix, Formatting format, String message) {
        String msg = format + Formatting.translateChars('&', message);
        if (prefix != null) {
            if (prefixFormat != null) {
                prefix = String.format(Formatting.translateChars('&', prefixFormat), prefix);
            }
            msg = prefix + msg;
        }
        recipient.sendMessage(msg);
    }
    
    public static void send(CommandSender recipient, Formatting format, String message) {
        send(recipient, null, format, message);
    }
    
}

