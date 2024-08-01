package exception;

public abstract class CodeNameException extends RuntimeException {
    public enum ExceptionType {
        LOAD_FILE,
        LOAD_NAME,
        CHECK_PATH,
        TURN_EXCEPTION,
        CARD_FLIPPED,
        SERVER_NAME_TAKEN,
        SERVER_GAME_LOGIC,
        SERVER_ADMIN_ON,
        SERVER_NO_SPOT,
        UNAUTHORIZED,
        MISMATCH_UPDATE,
        MISMATCH_ROLE,
        MISMATCH_TURN_STAGE,
        MISMATCH_TEAM,
        ENGINE,
    }

    private ExceptionType Type;

    public ExceptionType getType() {
        return Type;
    }

    public void setType(ExceptionType i_Type) {
        Type = i_Type;
    }
}
