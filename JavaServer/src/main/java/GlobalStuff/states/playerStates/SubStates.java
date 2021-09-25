package GlobalStuff.states.playerStates;

public enum SubStates {
    NOTHING,
    SHOOTING;

    private static final SubStates[] values = values();

    public static SubStates[] getValues() {
        return values;
    }
}
