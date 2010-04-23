package com.goodworkalan.spawn;

public abstract class Pipe implements CharSink {
    CharSink sink;
    
    private final CharSink source;
    
    public Pipe() {
        source = new LineSink() {
            public void send(String line, String ending) {
                in(line, ending);
            }

            public void close(boolean failure) {
                sink.close(failure);
            }
        };
    }
    
    public void send(char ch) {
        source.send(ch);
    }
    
    public void close(boolean failure) {
        source.close(failure);
    }
    
    public abstract void in(String line, String ending);
    
    protected void out(char ch) {
        sink.send(ch);
    }
    
    protected void out(String line, String ending) {
        for (int i = 0; i < line.length(); i++) {
            sink.send(line.charAt(i));
        }
        for (int i = 0; i < ending.length(); i++) {
            sink.send(ending.charAt(i));
        }
    }
}
