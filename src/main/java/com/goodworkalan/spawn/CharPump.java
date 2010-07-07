package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Reads characters from an <code>InputStream</code> and feeds them to a
 * <code>CharSink</code>.
 * 
 * @author Alan Gutierrez
 */
class CharPump implements Pump {
    /** The reader. */
    private final Reader reader;
    
    /** The character sink. */
    private final CharSink sink;

    /** The exception that terminated the pump, if any. */
    private SpawnException caught;

    /**
     * Create a character pump that reads an input stream and writes to a
     * character sink.
     * 
     * @param in
     *            The input stream.
     * @param encoding
     *            The character encoding of the input stream.
     * @param sink
     *            The character sink.
     */
    public CharPump(InputStream in, Charset encoding, CharSink sink) {
        this.reader = new InputStreamReader(in, encoding);
        this.sink = sink;
    }

    /**
     * Get the exception caught during execution that terminated the pump, or
     * null if the pump terminated normally.
     * 
     * @return The terminal exception or null.
     */
    public SpawnException getCaught() {
        return caught;
    }

    /**
     * Read characters from the input stream and feed them to the character
     * sink.
     */
    public void run() {
        char[] buffer = new char[4098];
        int read;
        try {
            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    sink.send(buffer[i]);
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
        sink.close(caught != null);
    }
}
