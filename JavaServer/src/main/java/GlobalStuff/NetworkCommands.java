package GlobalStuff;

public enum NetworkCommands {
    client_connect,
    client_disconnect,
    send_id,
    send_ping,
    receive_id,
    receive_username;

    private static final NetworkCommands[] values = values();

    public static NetworkCommands[] getValues() {
        return values;
    }
}


