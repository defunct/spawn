package com.goodworkalan.spawn;

import java.util.ResourceBundle;

/**
 * An exception raised when errors occur while spawning a child process.
 * 
 * @author Alan Gutierrez
 */
public class SpawnException extends RuntimeException {
    /** Unable to write to redirected output or error stream destination. */
    public final static int REDIRECT_OUTPUT_FAILURE = 101;
    
    /** Unable to execute command line. */
    public final static int EXECUTE_FAILURE = 102;
    
    /** Unable to create output consumer class. */
    public final static int CONSUMER_CREATE_FAILURE = 103;
    
    /** Unable to read from process output or error stream. */
    public final static int REDIRECT_PROCESS_FAILURE = 104;

    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** Unable to copy a file. */
    public static final int COPY_FAILURE = 101;

    /** Unable to slurp a file. */
    public static final int SLURP_FAILURE = 102;

    /** The error code. */
    private final int code;

    /** The detail message format arguments. */
    private final Object[] arguments;

    /**
     * Create a glob exception with the given error code and the given cause.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause.
     */
    public SpawnException(int code, Throwable cause, Object... arguments) {
        super(null, cause);
        this.code = code;
        this.arguments = arguments;
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the detail message string of this error.
     * 
     * @return The detail message string of this error.
     */
    @Override
    public String getMessage() {
        String bundleName = getClass().getPackage().getName() + ".exceptions";
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        String format = bundle.getString(Integer.toString(code));
        return String.format(format, arguments);
    }
}
