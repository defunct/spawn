package com.goodworkalan.spawn;

import java.util.LinkedList;
import java.util.List;

/**
 * The outcome of the execution of command by {@link Spawn}. This is an
 * immutable data structure that collects the standard output, error output and
 * exit code into one class.
 * 
 * @author Alan Gutierrez
 */
public class Exit {
    /** The linked list of all exit statuses. */
    public final LinkedList<Exit> exits;
    
    /** The standard output. */
    public final List<String> out;

    /** The error output. */
    public final List<String> err;

    /** The exit code. */
    public final int code;

    /**
     * Create an exit structure with the given standard output, error output and
     * exit code.
     * 
     * @param exits
     *            The linked list of all exit statuses.
     * @param stdOut
     *            The standard output.
     * @param stdErr
     *            The error output.
     * @param code
     *            The exit code.
     */
    Exit(LinkedList<Exit> exits, List<String> out, List<String> err, int code) {
        this.exits = exits;
        this.out = out;
        this.err = err;
        this.code = code;
    }

    /**
     * Return true of the exit code is 0.
     * 
     * @return True if the exit code is 0.
     */
    public boolean isSuccess() {
        return code == 0;
    }
}
