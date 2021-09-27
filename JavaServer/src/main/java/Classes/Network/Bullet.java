package Classes.Network;

import Classes.Client;
import GlobalStuff.NetworkCommands;
import util.Direction;
import util.NetworkUtils.PutUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public record Bullet(Client owner, Direction direction) implements Streamable {
    @Override
    public void putInStream(DataOutputStream dOut, Streamable object) throws IOException {
        dOut.write(NetworkCommands.place_bullet.ordinal());
        dOut.writeInt(owner.getMyId());
        PutUtils.putDirectionInStream(dOut, direction);
    }
}
