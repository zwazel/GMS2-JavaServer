package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NetworkUtils {
    public static void putStringInStream(DataOutputStream dOut, String s) throws IOException {
        char[] chars = s.toCharArray();
        dOut.writeInt(chars.length);
        for (char c : chars) {
            dOut.write(c);
        }
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
