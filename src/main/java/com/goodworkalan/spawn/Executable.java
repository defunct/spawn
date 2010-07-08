package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.EXECUTE_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * An external program invoked with a command line that runs in a separate process.
 *
 * @author Alan Gutierrez
 */
public class Executable implements Program {
    /** The command line. */
    private final List<String> command;
    
    /** The external process. */
    private Process process;

    /**
     * Create a new <code>Executable</code>.
     * 
     * @param command
     *            The command line.
     */
    public Executable(List<String> command) {
        this.command = command;
    }

    /**
     * Run the external process using the environmental properties of the given
     * <code>Spawn</code>.
     * 
     * @param spawn
     *            The environment.
     */
    public void run(Spawn spawn) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        
        processBuilder.directory(spawn.getWorkingDirectory());
        processBuilder.environment().putAll(spawn.getEnvironment());
        processBuilder.redirectErrorStream(spawn.isRedirectErrorStream());
        
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new SpawnException(EXECUTE_FAILURE, e, processBuilder.command());
        }
    }

    /**
     * Get the error output stream of the external process as an input stream to
     * read the error output.
     * 
     * @return The input stream to read error output stream.
     */
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    /**
     * Get the standard output stream of the external process as an input stream
     * to read the output.
     * 
     * @return The input stream to read the standard output stream.
     */
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    /**
     * Get the standard input stream of the external process as an output stream
     * to write the input.
     * 
     * @return The output stream to write the standard input stream.
     */
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    /**
     * Wait for the termination of the external process and get the exit code.
     * 
     * @return The process exit code.
     * @throws InterruptedException
     *             If the current thread is interrupted by another thread while
     *             waiting.
     */
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }
}
