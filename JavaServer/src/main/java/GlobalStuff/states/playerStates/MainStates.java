package GlobalStuff.states.playerStates;

public enum MainStates {
    IDLE,
    WALKING,
    RUNNING;

    private static final MainStates[] values = values();

    public static MainStates[] getValues() {
        return values;
    }
}
