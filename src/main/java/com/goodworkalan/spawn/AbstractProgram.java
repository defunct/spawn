package com.goodworkalan.spawn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

// TODO Document.
public abstract class AbstractProgram implements Program {
    // TODO Document.
    private final InputStream errorStream = new ByteArrayInputStream(new byte[0]);

    // TODO Document.
    public InputStream getErrorStream() {
        return errorStream;
    }

    // TODO Document.
    public OutputStream getOutputStream() {
        return NullOutputStream.INSTANCE;
    }
    
    // TODO Document.
    public int waitFor() throws InterruptedException {
        return 0;
    }

    // TODO Document.
    public void run(Spawn spawn) {
    }
}
