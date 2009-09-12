package com.goodworkalan.spawn;

import com.goodworkalan.cassandra.CassandraException;
import com.goodworkalan.cassandra.Report;

/**
 * An exception raised when errors occur while spawning a child process. 
 *
 * @author Alan Gutierrez
 */
public class SpawnException extends CassandraException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a spawn exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public SpawnException(int code) {
        super(code, new Report());
    }
    
    public SpawnException(int code, Throwable cause) {
        super(code, new Report(), cause);
    }
}
