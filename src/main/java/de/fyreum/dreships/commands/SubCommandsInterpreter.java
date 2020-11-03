package de.fyreum.dreships.commands;

import de.gleyder.umbrella.core.command.exceptions.InterpreterException;
import de.gleyder.umbrella.core.command.interfaces.Interpreter;

import java.util.Arrays;
import java.util.List;

public class SubCommandsInterpreter implements Interpreter<String> {

    @Override
    public String translate(String s) throws InterpreterException {
        if (!(s.equalsIgnoreCase("info") || s.equalsIgnoreCase("run") || s.equalsIgnoreCase("calculate"))) {
            throw new InterpreterException("Only 'info' or 'run' or 'calculate' available");
        }
        return s;
    }

    @Override
    public List<String> getTabCompletion() {
        return Arrays.asList("info", "run", "calculate");
    }

    @Override
    public Class<String> getClassType() {
        return String.class;
    }
}
