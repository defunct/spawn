package com.goodworkalan.spawn;

// TODO Document.
public abstract class Pipe implements CharSink {
    // TODO Document.
    CharSink sink;
    
    // TODO Document.
    private final CharSink source;
    
    // TODO Document.
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
    
    // TODO Document.
    public void send(char ch) {
        source.send(ch);
    }
    
    // TODO Document.
    public void close(boolean failure) {
        source.close(failure);
    }
    
    // TODO Document.
    public abstract void in(String line, String ending);
    
    // TODO Document.
    protected void out(char ch) {
        sink.send(ch);
    }
    
    // TODO Document.
    protected void out(String line, String ending) {
        for (int i = 0; i < line.length(); i++) {
            sink.send(line.charAt(i));
        }
        for (int i = 0; i < ending.length(); i++) {
            sink.send(ending.charAt(i));
        }
    }
}
