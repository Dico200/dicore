package io.dico.dicore.commandx.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An immutable wrapper for a string array that represents the path of the command being executed
 */
public class CommandPath {

    private final String[] path;

    public CommandPath(String[] path) {
        assert path != null;
        assert path.length > 0;
        this.path = path;
    }

    /**
     * Gets the element of the path at the specified index
     */
    public String get(int index) {
        return path[index];
    }

    /**
     * @return a copy of the underlying string array
     */
    public String[] raw() {
        return Arrays.copyOf(path, length());
    }

    /**
     * @return the first element of the path
     */
    public String root() {
        return path[0];
    }

    /**
     * @return the last element of the path
     */
    public String head() {
        return path[length() - 1];
    }

    public int length() {
        return path.length;
    }

    public void applyCompounds(String delimiter, int indexFrom, CompoundType... types) {
        String path = String.join(delimiter, (CharSequence[]) this.path);
        char[] chars = path.toCharArray();
        List<String> newPath = new ArrayList<>();




    }

}
