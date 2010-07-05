package com.goodworkalan.spawn;

import java.io.InputStream;
import java.io.OutputStream;

// TODO Document.
public interface Program {
    // TODO Document.
    public InputStream getInputStream();

    // TODO Document.
    public InputStream getErrorStream();

    // TODO Document.
    public OutputStream getOutputStream();

    // TODO Document.
    public void run(Spawn spawn);
    
    // TODO Document.
    public int waitFor() throws InterruptedException;
}
