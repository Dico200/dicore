package io.dico.dicore.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ParameterType<T> {
    
    public static final ParameterType<String> STRING;
    public static final ParameterType<Integer> INTEGER;
    public static final ParameterType<Float> FLOAT;
    public static final ParameterType<Player> PLAYER;
    public static final ParameterType<OfflinePlayer> OFFLINE_PLAYER;
    public static final ParameterType<Boolean> BOOLEAN;
    public static final ParameterType<ItemType> ITEM_TYPE;
    public static final ParameterType<Integer> DURATION;
    
    private static final String EXC_MSG_FORMAT = "Argument '$ARG$' must be $DESC$, %s.";
    
    private String message;
    private String typeName;
    
    public ParameterType(String typeName, String requirement) {
        this.typeName = typeName;
        this.message = String.format(EXC_MSG_FORMAT, requirement);
    }
    
    public ParameterType(String typeName) {
        this.typeName = typeName;
        this.message = null;
    }
    
    protected String exceptionMessage() {
        checkArgument(this.message != null, new ConfigException("No exception message can be thrown if there is no requirement"));
        return message;
    }
    
    protected String typeName() {
        return typeName;
    }
    
    protected abstract T handle(String input);
    
    protected List<String> complete(String input) {
        return new ArrayList<>();
    }
    
    static {
        
        STRING = new ParameterType<String>("") {
            
            @Override
            protected String handle(String input) {
                return input;
            }
        };
        
        INTEGER = new ParameterType<Integer>("Number", "a round number") {
            
            @Override
            protected Integer handle(String input) {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    throw new CommandException(exceptionMessage());
                }
            }
            
            @Override
            protected List<String> complete(String input) {
                List<Character> list = new ArrayList<>();
                char[] chars = input.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (Character.isDigit(chars[i])) {
                        list.add(chars[i]);
                    }
                }
                char[] result = new char[list.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = list.get(i);
                }
                return Arrays.asList(new String[]{String.valueOf(result)});
            }
        };
        
        FLOAT = new ParameterType<Float>("Amount", "a numeric value") {
            
            @Override
            protected Float handle(String input) {
                try {
                    return Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    throw new CommandException(exceptionMessage());
                }
            }
            
            @Override
            protected List<String> complete(String input) {
                List<Character> list = new ArrayList<>();
                char[] chars = input.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    if (Character.isDigit(c) || c == '.' || c == ',' || c == 'e' || c == 'E') {
                        list.add(chars[i]);
                    }
                }
                char[] result = new char[list.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = list.get(i);
                }
                return Arrays.asList(new String[]{String.valueOf(result)});
            }
        };
        
        PLAYER = new ParameterType<Player>("Player", "the name of an online player") {
            
            @Override
            protected Player handle(String input) {
                Player user = Bukkit.getPlayer(input);
                Validate.notNull(user, exceptionMessage());
                return user;
            }
            
            @Override
            protected List<String> complete(String input) {
                return Arrays.asList(Bukkit.matchPlayer(input).stream().map(player -> player.getName()).toArray(size -> new String[size]));
            }
        };
        
        OFFLINE_PLAYER = new ParameterType<OfflinePlayer>("Player", "the name of a known player") {
            
            @Override
            protected OfflinePlayer handle(String input) {
                input = input.toLowerCase();
                
                Player onlineUser = Bukkit.getPlayer(input);
                if (onlineUser != null) {
                    return onlineUser;
                }
                
                if (input.length() > 3) {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(input)) {
                            return player;
                        }
                    }
                }
                
                throw new CommandException(exceptionMessage());
            }
            
            @Override
            protected List<String> complete(String input) {
                input = input.toLowerCase();
                Set<String> result = new HashSet<>();
                
                Player onlineUser = Bukkit.getPlayer(input);
                if (onlineUser != null) {
                    result.add(onlineUser.getName());
                }
                
                if (input.length() > 3) {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        if (player.getName().toLowerCase().startsWith(input)) {
                            result.add(player.getName());
                        }
                    }
                }
                
                return new ArrayList<>(result);
            }
        };
        
        BOOLEAN = new ParameterType<Boolean>("Bool", "true/yes/false/no") {
            
            private final String[] inputs = {"true", "false", "yes", "no"};
            private final boolean[] outputs = {true, false, true, false};
            
            @Override
            protected Boolean handle(String input) {
                if (input == null)
                    return null;
                input = input.toLowerCase();
                for (int i = 0; i < inputs.length; i++)
                    if (input.equals(inputs[i]))
                        return outputs[i];
                throw new CommandException(exceptionMessage());
            }
            
            @Override
            protected List<String> complete(String input) {
                String starts = input.toLowerCase();
                return Arrays.stream(inputs).filter(string -> string.startsWith(starts)).collect(Collectors.toList());
            }
        };
        
        ITEM_TYPE = new ParameterType<ItemType>("ItemType", "the format is id:data or name:data, where data can be left out, or 0 <= data < 16") {
            
            @SuppressWarnings("deprecation")
            @Override
            protected ItemType handle(String input) {
                String exc = exceptionMessage();
                Validate.notNull(input, exc);
                input = input.toUpperCase();
                String[] split = input.split(":");
                Validate.isTrue(!input.isEmpty() && 1 <= split.length && split.length <= 2, exc);
                
                Material material = null;
                String typeStr = split[0];
                try {
                    material = Material.getMaterial(Integer.parseInt(typeStr));
                } catch (NumberFormatException e) {
                    material = Material.matchMaterial(typeStr);
                }
                Validate.notNull(material, exc);
                
                int data;
                if (split.length == 2) {
                    try {
                        data = Integer.parseInt(split[1]);
                    } catch (NumberFormatException e) {
                        throw new CommandException(exc);
                    }
                } else {
                    data = 0;
                }
                
                Validate.isTrue(0 <= data && data < 16, exc);
                return new ItemType(material, data);
            }
            
            @Override
            protected List<String> complete(String input) {
                List<String> result = new ArrayList<>();
                
                if (input == null)
                    return result;
                String[] split = input.split(":");
                if (split.length < 1)
                    return result;
                
                String data;
                if (split.length > 1) {
                    String givenData = split[1];
                    try {
                        Integer.parseInt(givenData);
                        data = ":" + givenData;
                    } catch (NumberFormatException e) {
                        return result;
                    }
                } else {
                    data = "";
                }
                
                String typeStr = split[0].toUpperCase();
                try {
                    Integer.parseInt(typeStr);
                    return result;
                } catch (NumberFormatException e) {
                    for (Material mat : Material.values()) {
                        String matName = mat.toString();
                        if (matName.startsWith(typeStr) || matName.replace("_", "").startsWith(typeStr)) {
                            result.add(mat.toString().toLowerCase() + data);
                        }
                    }
                }
                
                return result;
            }
            
        };
        
        DURATION = new ParameterType<Integer>("Time", "a duration, for example 3d5h2m6s") {
            
            Map<Character, Integer> durationUnits = new HashMap<>();
            
            {
                durationUnits.put('y', 31557600);
                durationUnits.put('w', 604800);
                durationUnits.put('d', 86400);
                durationUnits.put('h', 3600);
                durationUnits.put('m', 60);
                durationUnits.put('s', 1);
                durationUnits = Collections.unmodifiableMap(durationUnits);
            }
            
            /**
             * Calculate the duration from the input
             *
             * @return the given duration in seconds
             */
            @Override
            protected Integer handle(String input) {
                String exc = exceptionMessage();
                Integer unit = null;
                boolean amountSpecified = false;
                int amount = 0;
                int result = 0;
                for (char c : input.toCharArray()) {
                    try {
                        int digit = Integer.parseInt(String.valueOf(c));
                        amountSpecified = true;
                        amount = amount * 10 + digit;
                    } catch (NumberFormatException e) {
                        Validate.isTrue(amountSpecified, "In durations, you must specify an amount before each unit");
                        unit = durationUnits.get(c);
                        Validate.notNull(unit, exc);
                        Validate.isTrue(amount < 10000, "For durations, amounts above 9999 are not permitted");
                        result = result + amount * unit;
                        amount = 0;
                        amountSpecified = false;
                    }
                }
                
                if (result < 0)
                    return Integer.MAX_VALUE;
                return result;
            }
            
        };
    }
    
    public static class ItemType {
        
        private final Material material;
        private final int data;
        
        ItemType(Material material, int data) {
            checkNotNull(material);
            this.material = material;
            this.data = data;
        }
        
        public Material getMaterial() {
            return material;
        }
        
        public int getData() {
            return data;
        }
        
        @SuppressWarnings("deprecation")
        public int getId() {
            return material.getId();
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + material.hashCode();
            result = prime * result + data;
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof ItemType))
                return false;
            ItemType other = (ItemType) obj;
            if (data != other.data)
                return false;
            if (material != other.material)
                return false;
            return true;
        }
        
        @Override
        public String toString() {
            String string = material.toString().replace("_", " ").toLowerCase();
            return data != 0 ? string + ":" + data : string;
        }
        
    }
    
    /**
     * Calculates a string representation of the given duration
     *
     * @param duration The total duration in seconds
     * @return A string representation of the duration
     */
    public static String durationToString(int duration) {
        int years = duration / 31557600;
        duration %= 31557600;
        int weeks = duration / 604800;
        duration %= 604800;
        int days = duration / 86400;
        duration %= 86400;
        int hours = duration / 3600;
        duration %= 3600;
        int minutes = duration / 60;
        duration %= 60;
        int seconds = duration;
        
        List<String> sections = new ArrayList<String>();
        if (years > 0)
            sections.add(years + " years");
        if (weeks > 0)
            sections.add(weeks + " weeks");
        if (days > 0)
            sections.add(days + " days");
        if (hours > 0)
            sections.add(hours + " hours");
        if (minutes > 0)
            sections.add(minutes + " minutes");
        if (seconds > 0)
            sections.add(seconds + " seconds");
        
        String result = String.join(", ", sections);
        int lastCommaIndex = result.lastIndexOf(',');
        if (lastCommaIndex > -1) {
            result = new StringBuilder(result).replace(lastCommaIndex, lastCommaIndex + 1, " and").toString();
        }
        
        return result;
        
    }
}
