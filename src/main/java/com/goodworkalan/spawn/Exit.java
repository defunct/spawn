package com.goodworkalan.spawn;

/**
 * The outcome of the execution of command by {@link Spawn}. This is an
 * immutable data structure that collects the standard output, error output and
 * exit code into one class.
 * 
 * @author Alan Gutierrez
 */
public class Exit<StdOut extends Consumer, StdErr extends Consumer> {
    /** The standard output. */
    private final StdOut stdOut;
    /** The error output. */
    private final StdErr stdErr;
    /** The exit code. */
    private final int code;

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
    public Exit(StdOut stdOut, StdErr stdErr, int code) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.code = code;
    }

    /**
     * Get the standard output.
     * 
     * @return The standard output.
     */
    public StdOut getStdOut() {
        return stdOut;
    }

    /**
     * Get the error output.
     * 
     * @return The error output.
     */
    public StdErr getStdErr() {
        return stdErr;
    }

    /**
     * Get the exit code.
     * 
     * @return The exit code.
     */
    public int getCode() {
        return code;
    }
}
