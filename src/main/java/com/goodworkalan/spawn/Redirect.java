package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Redirects the output of a program to an output stream.
 *
 * @author Alan Gutierrez
 */
public class Redirect implements ByteSink {
    /** The output stream. */
    private final OutputStream out;
    
    /** Whether to close the output stream when the byte sink closes. */
    private final boolean close;

    /**
     * Create a new redirect.
     * 
     * @param out
     *            The output stream.
     * @param close
     *            Whether to close the output stream when the byte sink closes.
     */
    public Redirect(OutputStream out, boolean close) {
        this.out = out;
        this.close = close;
    }

    /**
     * Write the byte to the output stream.
     * 
     * @param b
     *            The byte.
     */
    public void send(byte b) {
        try {
            out.write(b);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }

    /**
     * Close the output stream if close output stream with sink was indicated at
     * construction.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure) {
        if (close) {
            try {
                out.close();
            } catch (IOException e) {
                throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
            }
        }
    }
}
