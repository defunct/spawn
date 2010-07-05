package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.OutputStream;

// TODO Document.
public class NullOutputStream extends OutputStream {
    // TODO Document.
    public final static OutputStream INSTANCE = new NullOutputStream();
    // TODO Document.
    @Override
    public void write(int b) throws IOException {
    }
    
    // TODO Document.
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
    }
}
