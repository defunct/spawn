package com.goodworkalan.spawn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An abstract program definition used to defined pseudo-programs that don't
 * actually spawn external processes.
 *
 * @author Alan Gutierrez
 */
public abstract class AbstractProgram implements Program {
    /** The empty error output stream. */
    private final InputStream errorStream = new ByteArrayInputStream(new byte[0]);

    /**
     * Get the error output stream of the program as an input stream to read the
     * error output. This default implementation of the error output stream is
     * empty and will not contain any output.
     * 
     * @return The input stream to read error output stream.
     */
    public InputStream getErrorStream() {
        return errorStream;
    }

    /**
     * Get the standard input stream of the program as an output stream to write
     * the input. This default implementation returns a "null" output stream
     * that will ignore all input.
     * 
     * @return The output stream to write the standard input stream.
     */
    public OutputStream getOutputStream() {
        return NullOutputStream.INSTANCE;
    }

    /**
     * Returns a zero exit code to indicate success and returns immediately.
     * 
     * @return A successful exit code.
     */
    public int waitFor() {
        return 0;
    }

    /**
     * A no operation implementation.
     * 
     * @param spawn
     *            The environment.
     */
    public void run(Spawn spawn) {
    }
}
