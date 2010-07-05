package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.Writer;

// TODO Document.
public class WriteChars implements CharSink {
    // TODO Document.
    private final Writer writer;
    
    // TODO Document.
    private final boolean close;

    // TODO Document.
    public WriteChars(Writer writer, boolean close) {
        this.writer = writer;
        this.close = close;
    }
    
    // TODO Document.
    public void send(char ch) {
        try {
            writer.write(ch);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }

    // TODO Document.
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
