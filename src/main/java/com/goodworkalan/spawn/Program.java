package com.goodworkalan.spawn;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An external or virtual program that consumes byte stream input and
 * emits byte stream output.
 *
 * @author Alan Gutierrez
 */
public interface Program {
    /**
     * Get the standard output stream of the program as an input stream to read
     * the output.
     * 
     * @return The input stream to read the standard output stream.
     */
    public InputStream getInputStream();

    /**
     * Get the error output stream of the program as an input stream to read the
     * error output.
     * 
     * @return The input stream to read error output stream.
     */
    public InputStream getErrorStream();

    /**
     * Get the standard input stream of the program as an output stream to write
     * the input.
     * 
     * @return The output stream to write the standard input stream.
     */
    public OutputStream getOutputStream();

    /**
     * Run the program using the environmental properties of the given
     * <code>Spawn</code>.
     * 
     * @param spawn
     *            The environment.
     */
    public void run(Spawn spawn);

    /**
     * Wait for the termination of the program and get the exit code.
     * 
     * @return The process exit code.
     * @throws InterruptedException
     *             If the current thread is interrupted by another thread while
     *             waiting.
     */
    public int waitFor() throws InterruptedException;
}
