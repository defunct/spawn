package com.goodworkalan.spawn;

import java.io.OutputStream;

/**
 * An output stream that does noting.
 *
 * @author Alan Gutierrez
 */
public class NullOutputStream extends OutputStream {
    /** The singleton instance. */
    public final static OutputStream INSTANCE = new NullOutputStream();

    /**
     * Does nothing.
     * 
     * @param b
     *            The byte to write.
     */
    @Override
    public void write(int b) {
    }

    /**
     * Does nothing.
     * 
     * @param b
     *            The byte buffer.
     * @param off
     *            The offset of the range in the buffer to write.
     * @param len
     *            The length of the range in the buffer to write.
     */
    @Override
    public void write(byte[] b, int off, int len) {
    }
}
