package com.goodworkalan.spawn;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// FIXME Document.
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
    
    public Exit $$(String...command) {
        return $$(Arrays.asList(command));
    }
    
    public Exit $$(List<String> command) {
        return $(command).run();
    }
    
    // TODO Maybe a $(InputStream) that can pump bytes to a utility?
    public Executor $(String...command) {
        return $(Arrays.asList(command));
    }
    
    public Executor $(List<String> command) {
        return new Executor(null, this, command);
    }
}
