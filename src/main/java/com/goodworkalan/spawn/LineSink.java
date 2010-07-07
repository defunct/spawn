package com.goodworkalan.spawn;

/**
 * A character sink that groups characters into lines. Derived classes define
 * the line by line processing by implementeing the
 * {@link #send(String, String) send} method.
 * 
 * @author Alan Gutierrez
 */
public abstract class LineSink implements CharSink {
    /** The line buffer. */
    private final StringBuilder line = new StringBuilder();

    /**
     * Consume a character of program output.
     * 
     * @param ch
     *            The character.
     */
    public void send(char ch) {
        if (ch == '\n') {
            send(line.toString(), "\n");
            line.setLength(0);
        } else {
            line.append(ch);
        }
    }
    
    /**
     * Close the sink by flushing the last line if any.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure) {
        if (!failure && line.length() != 0) {
            send(line.toString(), "");
            line.setLength(0);
        }
    }

    /**
     * Consume a line of program output.
     * 
     * @param line
     *            The line.
     * @param ending
     *            The line ending.
     */
    public abstract void send(String line, String ending);
}
