package com.goodworkalan.spawn;

import java.io.InputStream;
import java.io.OutputStream;

public interface Program {
    public InputStream getInputStream();

    public InputStream getErrorStream();

    public OutputStream getOutputStream();

    public void run(Spawn spawn);
    
    public int waitFor() throws InterruptedException;
}
