package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.REDIRECT_PROCESS_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class CharPump implements Pump {
    private final Reader reader;
    
    private final CharSink sink;

    private SpawnException caught;

    public CharPump(InputStream in, Charset encoding, CharSink sink) {
        this.reader = new InputStreamReader(in, encoding);
        this.sink = sink;
    }
    
    public SpawnException getCaught() {
        return caught;
    }
    
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
        sink.close(caught == null);
    }
}
