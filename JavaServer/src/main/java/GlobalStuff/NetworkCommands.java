package GlobalStuff;

public enum NetworkCommands {
    test,
    client_connect,
    client_disconnect,
    send_client_its_id,
    send_ping,
    receive_username,
    update_username,
    send_all_clients,
    get_move_direction,
    send_move_direction,
    send_ping_other;

    private static final NetworkCommands[] values = values();

    public static NetworkCommands[] getValues() {
        return values;
    }
}


