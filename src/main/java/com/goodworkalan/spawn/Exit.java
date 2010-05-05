package com.goodworkalan.spawn;

import java.util.List;

/**
 * The outcome of the execution of command by {@link Spawn}. This is an
 * immutable data structure that collects the standard output, error output and
 * exit code into one class.
 * 
 * @author Alan Gutierrez
 */
public class Exit {
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
     * @param stdOut
     *            The standard output.
     * @param stdErr
     *            The error output.
     * @param code
     *            The exit code.
     */
    Exit(List<String> out, List<String> err, int code) {
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
