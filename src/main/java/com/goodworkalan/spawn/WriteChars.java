package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.Writer;

public class WriteChars implements CharSink {
    private final Writer writer;
    private final boolean close;
    public WriteChars(Writer writer, boolean close) {
        this.writer = writer;
        this.close = close;
    }
    
    public void send(char ch) {
        try {
            writer.write(ch);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }

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
