package io.dico.dicore.command;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;

public class Formatting {
    public static final char FORMAT_CHAR = '\u00a7';
    private static final char UNDEFINED = '\0';
    private static final TCharObjectMap<Formatting> singleCharInstances = new TCharObjectHashMap<>(16, .5F, '\0');

    public static final Formatting
            BLACK 			= from('0'),
            DARK_BLUE 		= from('1'),
            DARL_GREEN		= from('2'),
            CYAN			= from('3'),
            DARK_RED 		= from('4'),
            PURPLE			= from('5'),
            ORANGE 			= from('6'),
            GRAY 			= from('7'),
            DARK_GRAY 		= from('8'),
            BLUE 			= from('9'),
            GREEN 			= from('a'),
            AQUA 			= from('b'),
            RED 			= from('c'),
            PINK 			= from('d'),
            YELLOW 			= from('e'),
            WHITE 			= from('f'),
            BOLD 			= from('l'),
            STRIKETHROUGH	= from('m'),
            UNDERLINE 		= from('n'),
            ITALIC 			= from('o'),
            MAGIC 			= from('k'),
            CLEAR			= from('r'),
            EMPTY			= from(UNDEFINED);

    public static String stripAll(char alternateChar, String value) {
        int index = value.indexOf(alternateChar);
        int max;
        if (index == -1 || index == (max = value.length() - 1)) {
            return value;
        }

        StringBuilder result = new StringBuilder();
        int from = 0;
        do {
            if (isRecognizedChar(value.charAt(index + 1))) {
                result.append(value, from, index + 2);
            } else {
                result.append(value, from, index);
            }

            from = index + 2;
            index = value.indexOf(alternateChar, index + 1);
        } while (index != -1 && index != max);

        result.append(value, from, max + 1);
        return result.toString();
    }

    public static Formatting from(char c) {
        if (isRecognizedChar(c)) {
            c = Character.toLowerCase(c);
            Formatting res = singleCharInstances.get(c);
            if (res == null) {
                singleCharInstances.put(c, res = new Formatting(c));
            }
            return res;
        }
        return new Formatting(UNDEFINED);
    }

    public static Formatting from(String chars) {
        return chars.length() == 1 ? from(chars.charAt(0)) : getFormats(chars, UNDEFINED);
    }

    public static Formatting getFormats(String input) {
        return getFormats(input, FORMAT_CHAR);
    }

    public static Formatting getFormats(String input, char formatChar) {
        boolean needsFormatChar = formatChar != UNDEFINED;

        char[] formats = new char[6];
        // just make sure it's not the same as formatChar
        char previous = (formatChar == 'a')? 'b' : 'a';

        for (char c : input.toLowerCase().toCharArray()) {
            if (previous == formatChar || !needsFormatChar) {
                if (isColourChar(c) || isResetChar(c)) {
                    formats = new char[6];
                    formats[0] = c;
                } else if (isFormatChar(c)) {
                    for (int i = 0; i < 6; i++) {
                        if (formats[i] == UNDEFINED) {
                            formats[i] = c;
                            break;
                        } else if (formats[i] == c) {
                            break;
                        }
                    }
                }
            }
            previous = c;
        }
        return new Formatting(formats);
    }

    public static String translateChars(char alternateChar, String input) {
        char[] result = new char[input.length()];
        char previous = UNDEFINED;
        int i = -1;
        for (char c : input.toCharArray()) {
            if (previous == alternateChar && isRecognizedChar(c)) {
                result[i] = FORMAT_CHAR;
            }
            result[i+=1] = c;
            previous = c;
        }
        return String.valueOf(result);
    }

    private static boolean isRecognizedChar(char c) {
        return isColourChar(c) || isFormatChar(c) || isResetChar(c);
    }

    private static boolean isColourChar(char c) {
        return "0123456789abcdefABCDEF".indexOf(c) > -1;
    }

    private static boolean isResetChar(char c) {
        return "rR".indexOf(c) > -1;
    }

    private static boolean isFormatChar(char c) {
        return "lmnokLMNOK".indexOf(c) > -1;
    }

    private final String format;

    private Formatting(char[] formats) {
        StringBuilder format = new StringBuilder();
        for (char c : formats) {
            if (c != UNDEFINED)
                format.append(FORMAT_CHAR).append(c);
            else break;
        }
        this.format = format.toString();
    }

    private Formatting(char c) {
        this.format = (c != UNDEFINED)? String.valueOf(FORMAT_CHAR) + c : "";
    }

    @Override
    public String toString() {
        return format;
    }
}