package com.goodworkalan.spawn;

/**
 * A block of <code>Java</code> code that processes program output line by line.
 * 
 * @author Alan Gutierrez
 */
public abstract class Pipe implements CharSink {
    /**
     * The character sink, set by the <code>Executor</code> while building the
     * virtual pipeline.
     */
    CharSink sink;
    
    /** Wrapper around a line sink. */
    private final CharSink source;
    
    /** Create a virtual pipe. */
    public Pipe() {
        source = new LineSink() {
            public void send(String line, String ending) {
                in(line, ending);
            }

            public void close(boolean failure) {
                sink.close(failure);
            }
        };
    }
    
    /**
     * Consume a single character of program output and forward it to the
     * <code>in</code> method of the virtual pipe.
     * 
     * @param ch
     *            The character.
     */
    public void send(char ch) {
        source.send(ch);
    }

    /**
     * Close the sink releasing any system resources.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure) {
        source.close(failure);
    }

    /**
     * Process the line of output and emit filtered or new output to the virtual
     * pipeline using one of the <code>out</code> methods.
     * 
     * @param line
     *            The line.
     * @param ending
     *            The line ending.
     */
    public abstract void in(String line, String ending);

    /**
     * Write a character to the virtual pipeline.
     * 
     * @param ch
     *            The character.
     */
    protected void out(char ch) {
        sink.send(ch);
    }

    /**
     * Write the line and line ending to the virtual pipeline.
     * 
     * @param line
     *            The line.
     * @param ending
     *            The line ending.
     */
    protected void out(String line, String ending) {
        for (int i = 0; i < line.length(); i++) {
            sink.send(line.charAt(i));
        }
        for (int i = 0; i < ending.length(); i++) {
            sink.send(ending.charAt(i));
        }
    }
}
