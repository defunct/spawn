package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
    public final static OutputStream INSTANCE = new NullOutputStream();
    @Override
    public void write(int b) throws IOException {
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
    }
}
