package util.NetworkUtils;

import util.Direction;
import util.Position;

import java.nio.ByteBuffer;

public class GetUtils {
    public static Position getPositionFromBuffer(ByteBuffer buffer) {
        double x = buffer.getDouble();
        double y = buffer.getDouble();

        x = (double) Math.round(x*100)/100;
        y = (double) Math.round(y*100)/100;

        return new Position(x, y);
    }

    public static Direction getDirectionFromBuffer(ByteBuffer buffer) {
        byte horizontal = buffer.get();
        byte vertical = buffer.get();
        return new Direction(horizontal, vertical);
    }

    public static String getStringFromBuffer(ByteBuffer buffer, int stringLength) {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < stringLength; i++) {
            int ii = buffer.get();
            char c = (char) ii;
            s.append(c);
        }
        return s.toString();
    }
}
