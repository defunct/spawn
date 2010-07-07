package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Used when an output stream needs to be translated into an input stream to be
 * read by code expecting an input stream of a pipline output. This is
 * implemented using <code>PipedInputStream</code> and
 * <code>PipedOutputStream</code> so the reader needs to be in a separate thread
 * from the writer.
 * 
 * @author Alan Gutierrez
 * 
 */
class MissingProcess extends AbstractProgram {
    /** The piped input stream. */
    private final PipedInputStream in;

    /** The piped output stream. */
    private final PipedOutputStream out;
    
    /** Create a missing process. */
    public MissingProcess() {
        try {
            this.in = new PipedInputStream();
            this.out = new PipedOutputStream(this.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get the piped input stream.
     * 
     * @return The piped input stream.
     */
    public InputStream getInputStream() {
        return in;
    }
    
    /**
     * Get the piped output stream.
     * 
     * @return The piped output stream.
     */
    public OutputStream getOutputStream() {
        return out;
    }
}
