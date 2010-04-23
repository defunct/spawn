package com.goodworkalan.spawn;

import java.util.List;

public class Slurp extends LineSink {
    private final List<String> lines;
    
    public Slurp(List<String> lines) {
        this.lines = lines;
    }
    
    public void send(String line, String ending) {
        lines.add(line);
    }
}
