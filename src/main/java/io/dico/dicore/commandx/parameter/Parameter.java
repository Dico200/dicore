package io.dico.dicore.commandx.parameter;

public interface Parameter {

    default ParameterPriority getPriority() {
        return ParameterPriority.forParameter(this);
    }





}
