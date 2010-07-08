package com.goodworkalan.spawn;

import java.util.List;

/**
 * A character sink that distributes the character output to multiple 
 * character sinks.
 *
 * @author Alan Gutierrez
 */
class MultiplexedCharSink implements CharSink {
    /** The array of character sinks. */
    private final CharSink[] sinks;

    /**
     * Create a multiplexed character sink.
     * 
     * @param sinks
     *            The list of character sinks.
     */
    public MultiplexedCharSink(List<CharSink> sinks) {
        this.sinks = sinks.toArray(new CharSink[sinks.size()]);
    }

    /**
     * Send a single character to each of the character sinks.
     * 
     * @param ch
     *            The single character.
     */
    public void send(char ch) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].send(ch);
        }
    }

    /**
     * Close the character sinks releasing any system resources.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure) {
        CharSink[] s = sinks;
        for (int i = 0, stop = s.length; i < stop; i++) {
            s[i].close(failure);
        }
    }
}
