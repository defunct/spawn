package com.goodworkalan.spawn;

/**
 * Consume the output of a program as character output, one character at a time.
 *
 * @author Alan Gutierrez
 */
public interface CharSink extends Sink {
    /**
     * Consume a single character of program output.
     * 
     * @param ch
     *            The character.
     */
    public void send(char ch);
}
