package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Reads bytes from an <code>InputStream</code> and feeds them to a each
 * of a list of 
 * <code>ByteSink</code> instances.
 * 
 * @author Alan Gutierrez
 */
class BytePump implements Pump {
    /** The input stream. */
    private final InputStream in;

    /** The list of byte sinks. */
    private final List<ByteSink> sinks;

    /** The exception that terminated the pump, if any. */
    private SpawnException caught;

    /**
     * Create a byte pump that reads an input stream and writes the bytes
     * read to each in a list of byte sinks.
     * 
     * @param in
     *            The input stream.
     * @param sinks
     *            The list of byte sinks.
     */
    public BytePump(InputStream in, List<ByteSink> sinks) {
        this.in = in;
        this.sinks = sinks;
    }

    /**
     * Get the exception caught during execution that terminated the pump, or
     * null if the pump terminated normally.
     * 
     * @return The terminal exception.
     */
    public SpawnException getCaught() {
        return caught;
    }

    /**
     * Read the input stream and write the bytes to each in the list of byte
     * sinks.
     */
    public void run() {
        byte[] buffer = new byte[4098];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                for (int i = 0, stop = sinks.size(); i < stop; i++) {
                    ByteSink sink = sinks.get(i);
                    for (int j = 0; j < read; j++) {
                        sink.send(buffer[j]);
                    }
                }
            }
        } catch (SpawnException e) {
            caught = e;
        } catch (IOException ioe) {
            try {
                throw new SpawnException(REDIRECT_PROCESS_FAILURE, ioe);
            } catch (SpawnException e) {
                caught = e;
            }
        }
        for (int i = 0, stop = sinks.size(); i < stop; i++) {
            sinks.get(i).close(caught != null);
        }
    }
}
