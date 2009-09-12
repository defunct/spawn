package com.goodworkalan.spawn;

import java.util.ArrayList;
import java.util.List;

public class Slurp extends LineConsumer {
    private final List<String> lines = new ArrayList<String>();
    
    public Slurp() {
    }
    
    public void consume(String line) {
        lines.add(line);
    }
    
    public List<String> getLines() {
        return lines;
    }
}
