package io.dico.dicore.commandx.parameter;

public enum ParameterPriority {

    FIRST(0),
    SECOND(1),
    THIRD(2);

    public static ParameterPriority forParameter(Parameter parameter) {
        if (parameter instanceof IndexedParameter) {
            return THIRD;
        } else if (parameter instanceof FlaggedParameter) {
            return SECOND;
        }

        return FIRST;
    }

    private final int priorityValue;

    ParameterPriority(int priorityValue) {
        this.priorityValue = priorityValue;


    }

    //public int compareTo(ParameterPriority other) {
    //    super.compareTo(other);
    //}

}
