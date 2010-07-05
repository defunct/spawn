package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.EXECUTE_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

// TODO Document.
public class Executable implements Program {
    // TODO Document.
    private final List<String> command;
    
    // TODO Document.
    private Process process;

    // TODO Document.
    public Executable(List<String> command) {
        this.command = command;
    }

    // TODO Document.
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
    
    // TODO Document.
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }
    
    // TODO Document.
    public InputStream getInputStream() {
        return process.getInputStream();
    }
    
    // TODO Document.
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }
    
    // TODO Document.
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }
}
