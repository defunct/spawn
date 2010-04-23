package com.goodworkalan.spawn;

public abstract class LineSink implements CharSink {
    private final StringBuilder newString = new StringBuilder();
    
    public void send(char ch) {
        if (ch == '\n') {
            send(newString.toString(), "\n");
            newString.setLength(0);
        } else {
            newString.append(ch);
        }
    }
    
    public void close(boolean failure) {
        if (!failure && newString.length() != 0) {
            send(newString.toString(), "");
            newString.setLength(0);
        }
    }
    
    public abstract void send(String line, String ending);
}
