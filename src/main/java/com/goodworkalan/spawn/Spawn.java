package com.goodworkalan.spawn;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Specifies a process execution environment and builds pipelines.
 * 
 * @author Alan Gutierrez
 */
public class Spawn {
    /**
     * The map of environment variables used to the the environment of spawned
     * processes.
     */
    private final Map<String, String> environment = new HashMap<String, String>();
    
    /** Whether the error stream is redirected to standard output. */
    private boolean redirectErrorStream;
    
    /** The set of exit codes that are considered normal exit codes. */
    private final Set<Integer> unexceptionalExitCodes = new HashSet<Integer>();
    
    /** The working directory. . */
    private File workingDirectory;

    /**
     * Set the set of exit codes that are considered normal exit codes. Exit
     * codes not in the set cause an exception to be raised. An empty set
     * indicates that no exit code will raise an exception. This method is not
     * additive, each time it is called the set is reset.
     * 
     * @param code
     *            The set of normal exist codes.
     */
    public void setUnexceptionalExitCodes(Integer...codes) {
        unexceptionalExitCodes.clear();
        unexceptionalExitCodes.addAll(Arrays.asList(codes));
    }
    
    /**
     * Whether the error stream is redirected to standard output.
     * 
     * @return True if the error stream is redirected to standard output.
     */
    public boolean isRedirectErrorStream() {
        return redirectErrorStream;
    }

    /**
     * Set whether the error stream is redirected to standard output.
     * 
     * @param mergeErrorStream
     *            True if the error stream is redirected to standard output.
     */
    public void setRedirectErrorStream(boolean mergeErrorStream) {
        this.redirectErrorStream = mergeErrorStream;
    }

    /**
     * Get the working directory in which the spawned processes will be run.
     * 
     * @return The working directory.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Set the working directory in which the spawned processes will be run.
     * 
     * @param workingDirectory
     *            The working directory.
     */
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Get the map of environment variables used to the the environment of
     * spawned processes.
     * 
     * @return The environment.
     */
    public Map<String, String> getEnvironment() {
        return environment;
    }

    /**
     * Create an executor that will emit the contents of the given input stream
     * to its standard output stream.
     * 
     * @param in
     *            The input stream.
     * @return An executor that emits the given input stream.
     */
    public Executor $(InputStream in) {
        return new Executor(null, this, new Cat(in));
    }

    /**
     * Execute the given command and return the exit status.
     * 
     * @param command
     *            The command line.
     * @return The exit status.
     */
    public Exit $$(String...command) {
        return $$(Arrays.asList(command));
    }
    
    /**
     * Execute the given command and return the exit status.
     * 
     * @param command
     *            The command line.
     * @return The exit status.
     */
    public Exit $$(List<String> command) {
        return $(command).run();
    }

    // TODO Document.
    public Executor $() {
        return new Executor(null, this, new MissingProcess());   
    }
    
    /**
     * Create an executor that executes the given comment.
     * 
     * @param command
     *            The command line.
     * @return An executor.
     */
    public Executor $(String...command) {
        return $(Arrays.asList(command));
    }
    
    /**
     * Create an executor that executes the given comment.
     * 
     * @param command
     *            The command line.
     * @return An executor.
     */
    public Executor $(List<String> command) {
        return new Executor(null, this, new Executable(command));
    }
}
