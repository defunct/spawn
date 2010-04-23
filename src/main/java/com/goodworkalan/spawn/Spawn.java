package com.goodworkalan.spawn;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Spawn {
    private final Map<String, String> environment = new HashMap<String, String>();
    
    private boolean redirectErrorStream;
    
    private final Set<Integer> unexceptionalExitCodes = new HashSet<Integer>();
    
    private File workingDirectory;
    
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
    
    public Executor out(Consumer consumer) {
        List<String> out = new ArrayList<String>();
        List<String> err = new ArrayList<String>();
        return new Executor(this, consumer, new Slurp(err), out, err);
    }
    
    public Executor err(Consumer consumer) {
        List<String> out = new ArrayList<String>();
        List<String> err = new ArrayList<String>();
        return new Executor(this, new Slurp(out), consumer, out, err);
    }
    
    public Executor both(Consumer stdOut, Consumer stdErr) {
        List<String> out = new ArrayList<String>();
        List<String> err = new ArrayList<String>();
        return new Executor(this, stdOut, stdErr, out, err);
    }
    
    public Exit execute(String...command) {
        List<String> out = new ArrayList<String>();
        List<String> err = new ArrayList<String>();
        Executor executor = new Executor(this, new Slurp(out), new Slurp(err), out, err);
        return executor.execute(Arrays.asList(command));
    }
}
