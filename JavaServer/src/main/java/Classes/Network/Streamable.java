package Classes.Network;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Streamable {
    void putInStream(DataOutputStream dOut, Streamable object) throws IOException;
}
