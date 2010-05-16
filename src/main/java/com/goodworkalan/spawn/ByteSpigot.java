package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

class ByteSpigot implements Spigot {
    private final InputStream in;

    private final List<ByteSink> sinks;

    private SpawnException caught;

    public ByteSpigot(InputStream in, List<ByteSink> sinks) {
        this.in = in;
        this.sinks = sinks;
    }

    public SpawnException getCaught() {
        return caught;
    }

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
