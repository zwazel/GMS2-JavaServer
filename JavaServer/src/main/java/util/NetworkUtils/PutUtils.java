package util.NetworkUtils;

import Classes.Client;

import java.io.DataOutputStream;
import java.io.IOException;

public class PutUtils {
    public static void putStringInStream(DataOutputStream dOut, String s) throws IOException {
        char[] chars = s.toCharArray();
        dOut.writeInt(chars.length);
        for (char c : chars) {
            dOut.write(c);
        }
    }

    public static void putClientInStream(DataOutputStream dOut, Client c) {
        try {
            dOut.writeInt(c.getMyId());
            putStringInStream(dOut, c.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
