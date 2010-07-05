package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

// TODO Document.
class MissingProcess extends AbstractProgram {
    // TODO Document.
    private final PipedInputStream in;
    // TODO Document.
    private final PipedOutputStream out;
    
    // TODO Document.
    public MissingProcess() {
        try {
            this.in = new PipedInputStream();
            this.out = new PipedOutputStream(this.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    // TODO Document.
    public InputStream getInputStream() {
        return in;
    }
    
    // TODO Document.
    public OutputStream getOutputStream() {
        return out;
    }
}
