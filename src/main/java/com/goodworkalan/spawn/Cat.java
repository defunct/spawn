package com.goodworkalan.spawn;

import java.io.InputStream;

/**
 * A pseudo-program that returns a specific in-process input stream to initiate
 * a pipeline.
 * 
 * @author Alan Gutierrez
 */
public class Cat extends AbstractProgram {
    /** The input stream. */
    private final InputStream inputStream;

    /**
     * Create a cat program.
     * 
     * @param inputStream
     *            The input stream.
     */
    public Cat(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    /**
     * Get input stream.
     * 
     * @return The input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }
}
