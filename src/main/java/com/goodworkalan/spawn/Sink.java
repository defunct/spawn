package com.goodworkalan.spawn;

/**
 * Output for a program in a pipeline. Descendants define methods specific to
 * byte, character or line output.
 * 
 * @author Alan Gutierrez
 */
public interface Sink {
    /**
     * Close the sink releasing any system resources.
     * 
     * @param failure
     *            Whether the sink is being closed after a write failure.
     */
    public void close(boolean failure);
}
