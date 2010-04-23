package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

class MissingProcess extends OutputStream {
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
    
    public PipedInputStream getInputStream() {
        return in;
    }
    
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
    
    @Override
    public void close() throws IOException {
        out.close();
    }
}
