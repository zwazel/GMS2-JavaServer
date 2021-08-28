package GlobalStuff;

public enum NetworkCommands {
    client_connect,
    client_disconnect,
    send_client_its_id,
    send_ping,
    receive_username,
    update_username,
    send_all_clients;

    private static final NetworkCommands[] values = values();

    public static NetworkCommands[] getValues() {
        return values;
    }
}


