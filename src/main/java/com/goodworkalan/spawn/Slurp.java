package com.goodworkalan.spawn;

import java.util.List;

/**
 * A line sink that reads the lines into a list of lines.
 *
 * @author Alan Gutierrez
 */
public class Slurp extends LineSink {
    /** The list of lines. */
    private final List<String> lines;
    
    /**
     * Create a line sink.
     * 
     * @param lines
     *            The list of lines.
     */
    public Slurp(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Add the line to the list of lines.
     * 
     * @param line
     *            The line.
     * @param line
     *            The line ending.
     */
    public void send(String line, String ending) {
        lines.add(line);
    }
}
