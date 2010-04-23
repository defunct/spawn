package com.goodworkalan.spawn;

import java.util.List;

class MultiplexedCharSink implements CharSink {
    private final CharSink[] sinks;

    public MultiplexedCharSink(List<CharSink> sinks) {
        this.sinks = sinks.toArray(new CharSink[sinks.size()]);
    }

    public void send(char ch) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].send(ch);
        }
    }

    public void close(boolean failure) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].close(failure);
        }
    }
}
