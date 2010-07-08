package com.goodworkalan.spawn;

/**
 * Consume the output a program a byte at a time.
 *
 * @author Alan Gutierrez
 */
public interface ByteSink extends Sink {
    /**
     * Consume a single byte of output.
     * 
     * @param b
     *            The byte.
     */
    public void send(byte b);
}
