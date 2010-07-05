package com.goodworkalan.spawn;

import java.util.List;

// TODO Document.
class MultiplexedCharSink implements CharSink {
    // TODO Document.
    private final CharSink[] sinks;

    // TODO Document.
    public MultiplexedCharSink(List<CharSink> sinks) {
        this.sinks = sinks.toArray(new CharSink[sinks.size()]);
    }

    // TODO Document.
    public void send(char ch) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].send(ch);
        }
    }

    // TODO Document.
    public void close(boolean failure) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].close(failure);
        }
    }
}
