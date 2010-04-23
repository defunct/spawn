package com.goodworkalan.spawn;

import java.util.List;

public class Slurp extends LineConsumer {
    private final List<String> lines;
    
    public Slurp(List<String> lines) {
        this.lines = lines;
    }
    
    public void consume(String line) {
        lines.add(line);
    }
}
