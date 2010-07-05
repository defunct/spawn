package com.goodworkalan.spawn;

import java.util.Locale;
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
    
    /** Unable to close the first input stream. */
    public final static int CLOSE_INPUT_FAILURE = 105;

    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** Unable to copy a file. */
    public static final int COPY_FAILURE = 101;

    /** Unable to slurp a file. */
    public static final int SLURP_FAILURE = 102;
    
    /** The error code. */
    private final int code;
    
    /** The error message. */
    private final String message;

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
        this.message = formatMessage(code, arguments);
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
     * Get the error message.
     * 
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Format the exception message using the message arguments to format the
     * message found with the message key in the message bundle found in the
     * package of the given context class.
     * 
     * @param contextClass
     *            The context class.
     * @param code
     *            The error code.
     * @param arguments
     *            The format message arguments.
     * @return The formatted message.
     */
    private String formatMessage(int code, Object...arguments) {
        String baseName = getClass().getPackage().getName() + ".exceptions";
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), Thread.currentThread().getContextClassLoader());
            return String.format((String) bundle.getObject(Integer.toString(code)), arguments);
        } catch (Exception e) {
            return String.format("Cannot load message key [%s] from bundle [%s] becuase [%s].", code, baseName, e.getMessage());
        }
    }
}
