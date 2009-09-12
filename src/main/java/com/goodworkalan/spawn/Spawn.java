package com.goodworkalan.spawn;

import java.io.IOException;

public class Spawn<StdOut extends Consumer, StdErr extends Consumer> {
    private final ProcessBuilder processBuilder = new ProcessBuilder();
    
    private final StdOut stdOut;
    
    private final StdErr stdErr;
    
    public static <O extends Consumer, E extends Consumer> Spawn<O, E> spawn(O stdOut, E stdErr) {
        return new Spawn<O, E>(stdOut, stdErr);
    }
    
    public static <O extends Consumer> Spawn<O, Slurp> spawn(O stdOut) {
        return new Spawn<O, Slurp>(stdOut, new Slurp());
    }
    
    public Spawn(StdOut stdOut, StdErr stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }
    
    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }
    
    public StdOut getOutput() {
        return stdOut;
    }
    
    public StdErr getError() {
        return stdErr;
    }
    
    public int execute() {
        Process process;
        try {
            process = getProcessBuilder().start();
        } catch (IOException e) {
            throw new SpawnException(0, e);
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
        return result;
    }
}
