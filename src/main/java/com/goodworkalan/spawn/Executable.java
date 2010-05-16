package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.EXECUTE_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Executable implements Program {
    private final List<String> command;
    
    private Process process;

    public Executable(List<String> command) {
        this.command = command;
    }

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
    
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }
    
    public InputStream getInputStream() {
        return process.getInputStream();
    }
    
    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }
    
    public int waitFor() throws InterruptedException {
        return process.waitFor();
    }
}
