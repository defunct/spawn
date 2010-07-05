package com.goodworkalan.spawn;

import java.util.List;

// TODO Document.
public class Slurp extends LineSink {
    // TODO Document.
    private final List<String> lines;
    
    // TODO Document.
    public Slurp(List<String> lines) {
        this.lines = lines;
    }
    
    // TODO Document.
    public void send(String line, String ending) {
        lines.add(line);
    }
}
