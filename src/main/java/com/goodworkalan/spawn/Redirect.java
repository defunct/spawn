package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.OutputStream;

// TODO Document.
public class Redirect implements ByteSink {
    // TODO Document.
    private final OutputStream out;
    // TODO Document.
    private final boolean close;
    // TODO Document.
    public Redirect(OutputStream out, boolean close) {
        this.out = out;
        this.close = close;
    }

    // TODO Document.
    public void close(boolean failure) {
        if (close) {
            try {
                out.close();
            } catch (IOException e) {
                throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
            }
        }
    }

    // TODO Document.
    public void send(byte b) {
        try {
            out.write(b);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }
}
