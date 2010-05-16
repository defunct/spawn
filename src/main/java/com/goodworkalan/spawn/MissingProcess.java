package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

class MissingProcess extends AbstractProgram {
    private final PipedInputStream in;
    private final PipedOutputStream out;
    
    public MissingProcess() {
        try {
            this.in = new PipedInputStream();
            this.out = new PipedOutputStream(this.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public InputStream getInputStream() {
        return in;
    }
    
    public OutputStream getOutputStream() {
        return out;
    }
}
