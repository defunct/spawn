package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.Writer;

/**
 * A characer sink that writes the characters to a <code>Writer</code>.
 *
 * @author Alan Gutierrez
 */
public class WriteChars implements CharSink {
    /** The writer. */
    private final Writer writer;
    
    /** Whether the writer should be closed when the sink is closed. */
    private final boolean close;

    /**
     * Create a character sink that writes the characters to a writer.
     * 
     * @param writer
     *            The writer.
     * @param close
     *            Whether the writer should be closed when the sink is closed.
     */
    public WriteChars(Writer writer, boolean close) {
        this.writer = writer;
        this.close = close;
    }

    /**
     * Write the character to the writer.
     * 
     * @param ch
     *            The character.
     */
    public void send(char ch) {
        try {
            writer.write(ch);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }

    /**
     * Close the writer if close writer with sink was indicated at construction.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure) {
        if (close) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
            }
        }
    }
}
