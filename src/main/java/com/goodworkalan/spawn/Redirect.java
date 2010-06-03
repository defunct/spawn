package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_OUTPUT_FAILURE;

import java.io.IOException;
import java.io.OutputStream;

public class Redirect implements ByteSink {
    private final OutputStream out;
    private final boolean close;
    public Redirect(OutputStream out, boolean close) {
        this.out = out;
        this.close = close;
    }

    public void close(boolean failure) {
        if (close) {
            try {
                out.close();
            } catch (IOException e) {
                throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
            }
        }
    }

    public void send(byte b) {
        try {
            out.write(b);
        } catch (IOException e) {
            throw new SpawnException(REDIRECT_OUTPUT_FAILURE, e);
        }
    }
}