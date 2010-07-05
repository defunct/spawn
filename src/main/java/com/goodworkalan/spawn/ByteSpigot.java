package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// TODO Document.
class ByteSpigot implements Spigot {
    // TODO Document.
    private final InputStream in;

    // TODO Document.
    private final List<ByteSink> sinks;

    // TODO Document.
    private SpawnException caught;

    // TODO Document.
    public ByteSpigot(InputStream in, List<ByteSink> sinks) {
        this.in = in;
        this.sinks = sinks;
    }

    // TODO Document.
    public SpawnException getCaught() {
        return caught;
    }

    // TODO Document.
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
