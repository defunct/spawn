package com.goodworkalan.spawn;

// TODO Document.
public abstract class LineSink implements CharSink {
    // TODO Document.
    private final StringBuilder newString = new StringBuilder();
    
    // TODO Document.
    public void send(char ch) {
        if (ch == '\n') {
            send(newString.toString(), "\n");
            newString.setLength(0);
        } else {
            newString.append(ch);
        }
    }
    
    // TODO Document.
    public void close(boolean failure) {
        if (!failure && newString.length() != 0) {
            send(newString.toString(), "");
            newString.setLength(0);
        }
    }
    
    // TODO Document.
    public abstract void send(String line, String ending);
}
