package com.goodworkalan.spawn;

public interface Consumer {
    public void consume(byte[] buffer, int offset, int length);
    
    public void close(boolean failure);
}
