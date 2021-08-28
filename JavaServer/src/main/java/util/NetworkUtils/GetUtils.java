package util.NetworkUtils;

import java.nio.ByteBuffer;

public class GetUtils {
    public static byte[] getDirectionFromBuffer(ByteBuffer buffer) {
        byte horizontal = buffer.get();
        byte vertical = buffer.get();

        return new byte[]{horizontal, vertical};
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
