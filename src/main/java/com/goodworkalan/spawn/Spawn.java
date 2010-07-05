package com.goodworkalan.spawn;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO Document.
public class Spawn {
    // TODO Document.
    private final Map<String, String> environment = new HashMap<String, String>();
    
    // TODO Document.
    private boolean redirectErrorStream;
    
    // TODO Document.
    private final Set<Integer> unexceptionalExitCodes = new HashSet<Integer>();
    
    // TODO Document.
    private File workingDirectory;
    
    // TODO Document.
    public void setUnexceptionalExitCodes(Integer...codes) {
        unexceptionalExitCodes.clear();
        unexceptionalExitCodes.addAll(Arrays.asList(codes));
    }
    
    // TODO Document.
    public boolean isRedirectErrorStream() {
        return redirectErrorStream;
    }
    
    // TODO Document.
    public void setRedirectErrorStream(boolean mergeErrorStream) {
        this.redirectErrorStream = mergeErrorStream;
    }
    
    // TODO Document.
    public File getWorkingDirectory() {
        return workingDirectory;
    }
    
    // TODO Document.
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
    
    // TODO Document.
    public Map<String, String> getEnvironment() {
        return environment;
    }
    
    // TODO Document.
    public Executor $(InputStream in) {
        return new Executor(null, this, new Cat(in));
    }
    
    // TODO Document.
    public Exit $$(String...command) {
        return $$(Arrays.asList(command));
    }
    
    // TODO Document.
    public Exit $$(List<String> command) {
        return $(command).run();
    }
    
    // TODO Document.
    public Executor $() {
        return new Executor(null, this, new MissingProcess());   
    }
    
    // TODO Maybe a $(InputStream) that can pump bytes to a utility?
    public Executor $(String...command) {
        return $(Arrays.asList(command));
    }
    
    // TODO Document.
    public Executor $(List<String> command) {
        return new Executor(null, this, new Executable(command));
    }
}
