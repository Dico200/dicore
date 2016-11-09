package io.dico.dicore.util;

public class StringUtil {

    /**
     * Capitalizes the first character of the string or the first character of each word
     * @param input the string to capitalize
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
     * @param input
     * @return a humanified version of @code input
     */
    public static String humanify(String input) {
        return input == null ? null : input.toLowerCase().replace('_', ' ');
    }

}
