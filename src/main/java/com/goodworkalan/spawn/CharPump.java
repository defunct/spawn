package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

// TODO Document.
class CharPump implements Pump {
    // TODO Document.
    private final Reader reader;
    
    // TODO Document.
    private final CharSink sink;

    // TODO Document.
    private SpawnException caught;

    // TODO Document.
    public CharPump(InputStream in, Charset encoding, CharSink sink) {
        this.reader = new InputStreamReader(in, encoding);
        this.sink = sink;
    }
    
    // TODO Document.
    public SpawnException getCaught() {
        return caught;
    }
    
    // TODO Document.
    public void run() {
        char[] buffer = new char[4098];
        int read;
        try {
            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    sink.send(buffer[i]);
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
        sink.close(caught != null);
    }
}
