package exception.loadxml;

import exception.CodeNameException;

import java.util.List;

public class TeamNamesNotUnique extends CodeNameException {
    private final List<String> NonUniqueNames;

    public TeamNamesNotUnique(List<String> i_NunUniqueNames) {
        NonUniqueNames = i_NunUniqueNames;
        setType(ExceptionType.LOAD_NAME);
    }

    public List<String> getNonUniqueNames() {
        return NonUniqueNames;
    }
}
