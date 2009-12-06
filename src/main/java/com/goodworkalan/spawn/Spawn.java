package com.goodworkalan.spawn;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Spawn<StdOut extends Consumer, StdErr extends Consumer> {
    private final ConsumerServer<StdOut> stdOutServer;
    
    private final ConsumerServer<StdErr> stdErrServer;
    
    private final Map<String, String> environment = new HashMap<String, String>();
    
    private boolean redirectErrorStream;
    
    private final Set<Integer> unexceptionalExitCodes = new HashSet<Integer>();
    
    private File workingDirectory;
    
    public static <O extends Consumer, E extends Consumer> Spawn<O, E> spawn(O stdOut, E stdErr) {
        return new Spawn<O, E>(stdOut, stdErr);
    }
    
    public static <O extends Consumer> Spawn<O, Slurp> spawn(O stdOut) {
        return new Spawn<O, Slurp>(stdOut, Slurp.class);
    }
    
    public static Spawn<Slurp, Slurp> spawn() {
        return new Spawn<Slurp, Slurp>(Slurp.class, Slurp.class);
    }
    
    public Spawn(StdOut stdOut, StdErr stdErr) {
        this.stdOutServer = new InstanceServer<StdOut>(stdOut);
        this.stdErrServer = new InstanceServer<StdErr>(stdErr);
    }
    
    public Spawn(Class<StdOut> stdOut, StdErr stdErr) {
        this.stdOutServer = new ConstructorServer<StdOut>(stdOut);
        this.stdErrServer = new InstanceServer<StdErr>(stdErr);
    }
    
    public Spawn(StdOut stdOut, Class<StdErr> stdErr) {
        this.stdOutServer = new InstanceServer<StdOut>(stdOut);
        this.stdErrServer = new ConstructorServer<StdErr>(stdErr);
    }
    
    public Spawn(Class<StdOut> stdOut, Class<StdErr> stdErr) {
        this.stdOutServer = new ConstructorServer<StdOut>(stdOut);
        this.stdErrServer = new ConstructorServer<StdErr>(stdErr);
    }
    
    public void setUnexceptionalExitCodes(Integer...codes) {
        unexceptionalExitCodes.clear();
        unexceptionalExitCodes.addAll(Arrays.asList(codes));
    }
    
    public boolean isRedirectErrorStream() {
        return redirectErrorStream;
    }
    
    public void setRedirectErrorStream(boolean mergeErrorStream) {
        this.redirectErrorStream = mergeErrorStream;
    }
    
    public File getWorkingDirectory() {
        return workingDirectory;
    }
    
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
    
    public Map<String, String> getEnvironment() {
        return environment;
    }
    
    public Exit<StdOut, StdErr> execute(String...command) {
        return execute(Arrays.asList(command));
    }

    public Exit<StdOut, StdErr> execute(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        
        processBuilder.directory(getWorkingDirectory());
        processBuilder.environment().putAll(getEnvironment());
        processBuilder.redirectErrorStream(isRedirectErrorStream());
        
        StdOut stdOut = stdOutServer.getConsumer();
        StdErr stdErr = stdErrServer.getConsumer();
        Process process;
        try {
            process = processBuilder.start();
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
        return new Exit<StdOut, StdErr>(stdOut, stdErr, result);
    }
}
