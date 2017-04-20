package io.dico.dicore.util;

import io.dico.dicore.command.Formatting;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class StringUtil {
    
    /**
     * Capitalizes the first character of the string or the first character of each word
     *
     * @param input     the string to capitalize
     * @param spaceChar the character separating each word. If @code '\0' is passed, only the first character of
     *                  the input is capitalized.
     * @return the capitalized string
     */
    public static String capitalize(String input, char spaceChar) {
        char[] result = input.toCharArray();
        if (spaceChar == '\0') {
            if (result.length != 0) {
                result[0] = Character.toUpperCase(result[0]);
            }
            return new String(result);
        }
        boolean capitalize = true;
        for (int i = 0; i < result.length; i++) {
            char c = result[i];
            if (capitalize) {
                result[i] = Character.toUpperCase(c);
            }
            capitalize = c == spaceChar;
        }
        return new String(result);
    }
    
    /**
     * Returns a lowercase version of the input with _ replaces with a space. Mainly used for making
     * names of enum constants readable.
     *
     * @param input
     * @return a humanified version of @code input
     */
    public static String humanify(String input) {
        return input == null ? null : input.toLowerCase().replace('_', ' ');
    }
    
    public static String enumerate(String[] words) {
        StringBuilder result = new StringBuilder();
        int size = words.length;
        int secondLastIndex = size - 2;
        for (int i = 0; i < size; i++) {
            String word = words[i];
            if (word.isEmpty())
                continue;
            result.append(word);
            if (i < secondLastIndex)
                result.append(", ");
            else if (i == secondLastIndex)
                result.append(" and ");
        }
        return result.toString();
    }
    
    public static String enumerate(String list, String regex) {
        return enumerate(list.split(regex));
    }
    
    public static String getTimeLength(long length) {
        int minute = 60000; // in millis
        int hour = 60 * minute;
        int day = 24 * hour;
        
        int minutes = (int) ((length / minute) % 60);
        int hours = (int) ((length / hour) % 24);
        int days = (int) (length / day); //returns floor
        
        String result = ""; // It will be splitted at "|"
        if (days != 0)
            result += days + " days|";
        if (hours != 0)
            result += hours + " hours|";
        if (minutes != 0)
            result += minutes + " minutes|";
        return enumerate(result, "\\|");
    }
    
    public static String getTimeLength(long sourceAmount, TimeUnit sourceUnit, String ifEmpty, TimeUnit... displayedUnits) {
        if (displayedUnits.length == 0) {
            throw new IllegalArgumentException("No displayed units");
        }
        Arrays.sort(displayedUnits, Collections.reverseOrder(TimeUnit::compareTo)); // sort by opposite of enum declaration order
        List<String> segments = new ArrayList<>(displayedUnits.length);
        for (TimeUnit unit : displayedUnits) {
            long displayedAmount = unit.convert(sourceAmount, sourceUnit);
            sourceAmount -= sourceUnit.convert(displayedAmount, unit);
            if (displayedAmount > 0) {
                String unitWord = unit.name().toLowerCase();
                if (displayedAmount == 1) {
                    unitWord = unitWord.substring(0, unitWord.length() - 1);
                }
                segments.add(displayedAmount + " " + unitWord);
            }
        }
        return segments.isEmpty() ? ifEmpty : enumerate(segments.toArray(new String[segments.size()]));
    }
    
    public static long getTimeLength(String input) { //if -1: error
        long count = 0;
        int i = 0;
        while (i < input.length()) {
            int num = 0;
            char unit = '\0';
            boolean negate;
            if (negate = input.charAt(i) == '-') {
                i++;
            }
            do {
                char c = input.charAt(i);
                int digit = c - '0';
                if (0 <= digit && digit < 10) {
                    num = 10 * num + digit;
                } else {
                    unit = c;
                    break;
                }
            } while (i < input.length());
            
            long unitTime = getUnitTime(unit);
            if (unitTime == -1)
                throw new IllegalArgumentException();
            if (negate) {
                unitTime = -unitTime;
            }
            count += (num * unitTime);
        }
        return count;
    }
    
    public static long getUnitTime(char unit) { //if -1: no value found
        switch (Character.toLowerCase(unit)) {
            case 's':
                return 1000;
            case 'm':
                return 1000 * 60;
            case 'h':
                return 1000 * 60 * 60;
            case 'd':
                return 1000 * 60 * 60 * 24;
            case 'w':
                return 1000 * 60 * 60 * 24 * 7;
            default:
                return -1;
        }
    }
    
    public static String replaceKeepColours(String target, String toReplace, String with) {
        int index = -toReplace.length();
        while ((index = target.indexOf(toReplace, index + toReplace.length())) != -1) {
            String start = target.substring(0, index);
            Formatting coloursBefore = Formatting.getFormats(start);
            String after;
            try {
                after = target.substring(index + toReplace.length());
            } catch (IndexOutOfBoundsException e) {
                after = "";
            }
            target = start + with + coloursBefore + after;
        }
        return target;
    }
    
}
