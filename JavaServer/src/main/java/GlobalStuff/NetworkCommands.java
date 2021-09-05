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
    update_clients,
    send_ping_other,
    end_of_packet;

    private static final NetworkCommands[] values = values();

    public static NetworkCommands[] getValues() {
        return values;
    }
}


