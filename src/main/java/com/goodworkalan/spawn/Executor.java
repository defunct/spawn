package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.EXECUTE_FAILURE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Executor {
    private final Spawn spawn;
    
    private final Consumer stdOut;
    
    private final Consumer stdErr;
    
    private final List<String> outLines;
    
    private final List<String> errLines;
    
    public Executor(Spawn spawn, Consumer stdOut, Consumer stdErr, List<String> out, List<String> err ) {
        this.spawn = spawn;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
        this.outLines = out;
        this.errLines = err;
    }
    
    public Exit execute(String... command) {
        return execute(Arrays.asList(command));
    }
    
    public Exit execute(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        
        processBuilder.directory(spawn.getWorkingDirectory());
        processBuilder.environment().putAll(spawn.getEnvironment());
        processBuilder.redirectErrorStream(spawn.isRedirectErrorStream());
        
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new SpawnException(EXECUTE_FAILURE, e, processBuilder.command());
        }
        Thread out = new Thread(new Consume(process.getInputStream(), stdOut));
        Thread err = new Thread(new Consume(process.getErrorStream(), stdErr));
        out.start();
        err.start();
        int result;
        try {
            result = process.waitFor();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
        try {
            err.join();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
        try {
            out.join();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
        return new Exit(outLines, errLines, result);
    }
}
