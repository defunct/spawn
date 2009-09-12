package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.OutputStream;

public class Redirect implements Consumer {
    private final OutputStream out;
    
    public Redirect(OutputStream out) {
        this.out = out;
    }
    
    public void consume(byte[] buffer, int offset, int length) {
        try {
            out.write(buffer, offset, length);
        } catch (IOException e) {
            throw new SpawnException(0, e);
        }
    }
    
    public void close(boolean failure) {
    }
}
