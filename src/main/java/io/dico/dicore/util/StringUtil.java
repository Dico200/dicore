package io.dico.dicore.util;

import io.dico.dicore.command.Formatting;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
        char[] chars = input.toCharArray();
        long count = 0;
        int i = 0;
        while (i < input.length()) {
            int num = 0;
            char unit = '\0';
            boolean negate;
            if (negate = chars[i] == '-') {
                i++;
            }
            do {
                char c = chars[i];
                try {
                    num = Integer.parseInt(Character.toString(c)) + 10 * num;
                } catch (NumberFormatException e) {
                    unit = c;
                    i++;
                    break;
                }
                i++;
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
            case 'm':
                return 60000;
            case 'h':
                return 3600000;
            case 'd':
                return 86400000;
            default:
                return -1;
        }
    }

    /**
     * Computes a binary representation of the value.
     * The returned representation always displays 64 bits.
     * Every 8 bits, the digits are seperated by an _
     * The representation is prefixed by 0b.
     * <p>
     * Example: 0b00000000_11111111_00000001_11110000_00001111_11001100_00001111_10111010
     *
     * @param entry the value to represent in binary
     * @return A binary representation of the long value
     */
    public static String toBinaryString(long entry) {
        String binary = Long.toBinaryString(entry);
        String binary64 = String.valueOf(new char[64 - binary.length()]).replace('\0', '0') + binary;
        String withUnderscores = String.join("_", IntStream.range(0, 8).mapToObj(x -> binary64.substring(x * 8, x * 8 + 8)).toArray(String[]::new));
        return "0b" + withUnderscores;
    }

    /**
     * Turns a generic java classname into a name formatted properly to be an enum constant.
     *
     * @param name The string value I'd describe as a generic java classname (so we have CapitalCase)
     * @return An enum constant version of it (ENUM_FORMAT: CAPITAL_CASE)
     */
    public static String toEnumFormat(String name) {
        StringBuilder result = new StringBuilder(name.length() + 2);

        boolean capital = true;
        for (int i = 0, n = name.length(); i < n; i++) {
            char c = name.charAt(i);
            if (capital) {
                capital = Character.isUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                capital = true;
                result.append('_');
            }
            result.append(capital ? c : Character.toUpperCase(c));
        }

        return result.toString();
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
