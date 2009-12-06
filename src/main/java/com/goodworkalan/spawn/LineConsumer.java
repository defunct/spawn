package com.goodworkalan.spawn;

public abstract class LineConsumer implements Consumer {
    private final StringBuilder newString = new StringBuilder();
    
    public void consume(byte[] buffer, int offset, int length) {
        for (int i = offset; i < length; i++) {
            if (buffer[i] == '\n') {
                consume(newString.toString());
                newString.setLength(0);
            } else {
                newString.append((char) buffer[i]);
            }
        }
    }
    
    public void close(boolean failure) {
        if (!failure && newString.length() != 0) {
            consume(newString.toString());
            newString.setLength(0);
        }
    }
    
    protected abstract void consume(String line);
}
