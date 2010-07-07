package com.goodworkalan.spawn;

/**
 * A runnable class that acts as a pump reading input and generating output.
 * 
 * @author Alan Gutierrez
 */
interface Spigot extends Runnable {
    /**
     * Get the exception caught during execution that terminated the pump, or
     * null if the pump terminated normally.
     * 
     * @return The terminal exception.
     */
    public SpawnException getCaught();
}
