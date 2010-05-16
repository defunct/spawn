package com.goodworkalan.spawn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractProgram implements Program {
    private final InputStream errorStream = new ByteArrayInputStream(new byte[0]);

    public InputStream getErrorStream() {
        return errorStream;
    }

    public OutputStream getOutputStream() {
        return NullOutputStream.INSTANCE;
    }
    
    public int waitFor() throws InterruptedException {
        return 0;
    }

    public void run(Spawn spawn) {
    }
}
