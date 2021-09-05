package util.NetworkUtils;

import Classes.Client;
import util.Direction;
import util.Position;

import java.io.DataOutputStream;
import java.io.IOException;

public class PutUtils {
    public static void putDirectionInStream(DataOutputStream dOut, Direction direction) throws IOException {
        putDirectionInStream(dOut, new byte[]{direction.getHorizontal(), direction.getVertical()});
    }

    public static void putDirectionInStream(DataOutputStream dOut, byte[] directions) throws IOException {
        dOut.write(directions[0]);
        dOut.write(directions[1]);
    }

    public static void putPositionInStream(DataOutputStream dOut, Position position) throws IOException {
        dOut.writeDouble(position.getX());
        dOut.writeDouble(position.getY());
    }

    public static void putStringInStream(DataOutputStream dOut, String s) throws IOException {
        if (s == null) {
            s = "undefinedFromServer";
        }
        char[] chars = s.toCharArray();
        dOut.writeInt(chars.length);
        for (char c : chars) {
            dOut.write(c);
        }
    }

    public static void putClientInStream(DataOutputStream dOut, Client c, boolean init) {
        try {
            dOut.writeInt(c.getMyId()); // ID
            boolean withUsername = ((c.getUsername() != null) && init);
            dOut.writeBoolean(withUsername);
            if (withUsername) {
                putStringInStream(dOut, c.getUsername());
            }
            dOut.writeInt(c.getHealth()); // Health
            if (init) {
                dOut.writeInt(c.getSpeed());
            } else {
                dOut.writeInt(c.getPing());
            }
            putPositionInStream(dOut, c.getPosition()); // Position
            putDirectionInStream(dOut, c.getDirection()); // Direction
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
