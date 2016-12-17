package io.dico.dicore.commandx.parameter;

import io.dico.dicore.commandx.CommandException;

import java.util.Map;

public interface ParameterizationStrategy {

    Map<String, String> parameterize(String input) throws CommandException;

    Map<String, Object> parse(Map<String, String> parameters) throws CommandException;

}
