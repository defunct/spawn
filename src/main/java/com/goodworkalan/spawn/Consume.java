package com.goodworkalan.spawn;
import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;

public class Consume implements Runnable {
    private final InputStream in;

    private final Consumer consumer;

    private SpawnException caught;

    public Consume(InputStream in, Consumer consumer) {
        this.in = in;
        this.consumer = consumer;
    }

    public SpawnException getCaught() {
        return caught;
    }

    public void run() {
        byte[] buffer = new byte[4098];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                consumer.consume(buffer, 0, read);
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
        consumer.close(caught == null);
    }
}
