package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.EXECUTE_FAILURE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Executor {
    private final Executor previous;
    
    private final Spawn spawn;
    
    private final List<String> command;
    
    private final List<Pipe> pipes = new ArrayList<Pipe>();
    
    private final List<ByteSink> outBytes = new ArrayList<ByteSink>();
    
    private final List<CharSink> outChars = new ArrayList<CharSink>();
    
    private final List<ByteSink> errBytes = new ArrayList<ByteSink>();
    
    private final List<CharSink> errChars = new ArrayList<CharSink>();
    
    private boolean tee;

    public Executor(Executor previous, Spawn spawn, List<String> command) {
        this.previous = previous;
        this.spawn = spawn;
        this.command = command;
    }
    
    public Executor $(List<String> command) {
        return new Executor(this, spawn, command);
    }
    
    public Executor $(String... command) {
        return $(Arrays.asList(command));
    }
    
    // FIXME Need some way to say tee? To say, do not neglect to collect lines?
    public Executor out(OutputStream out) {
        return out(new Redirect(out, false));
    }
    
    public Executor tee() {
        tee = true;
        return this;
    }
    
    public Executor err(OutputStream err) {
        return err(new Redirect(err, false));
    }
    
    public Executor out(Pipe pipe) {
        pipes.add(pipe);
        return this;
    }

    public Executor out(ByteSink sink) {
        outBytes.add(sink);
        return this;
    }
    
    public Executor out(CharSink sink) {
        outChars.add(sink);
        return this;
    }
    
    public Executor err(ByteSink sink) {
        errBytes.add(sink);
        return this;
    }
    
    public Executor err(CharSink chars) {
        errChars.add(chars);
        return this;
    }
    
    public Exit run() {
        LinkedList<Exit> exits = new LinkedList<Exit>();
        execute(null, exits);
        return exits.getLast();
    }
    
    public Exit[] pipe() {
        List<Exit> exits = new ArrayList<Exit>();
        execute(null, exits);
        return exits.toArray(new Exit[exits.size()]); 
    }
    
    void execute(OutputStream next, final List<Exit> exit) {
        final Process process;
        OutputStream outputStream;
        InputStream inputStream;
        InputStream errorStream = null;
        if (command.size() == 0) {
            process = null;
            MissingProcess missing = new MissingProcess();
            outputStream = missing;
            inputStream = missing.getInputStream();
            if (previous == null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new SpawnException(0, e);
                }
            }
        } else {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            
            processBuilder.directory(spawn.getWorkingDirectory());
            processBuilder.environment().putAll(spawn.getEnvironment());
            processBuilder.redirectErrorStream(spawn.isRedirectErrorStream());
            
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                throw new SpawnException(EXECUTE_FAILURE, e, processBuilder.command());
            }
            outputStream = process.getOutputStream();
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
        }
        final List<String> outLines = new ArrayList<String>();
        
        Charset cs = Charset.defaultCharset();
        
        final List<Thread> threads = new ArrayList<Thread>();
        
        if (tee || (outChars.isEmpty() && outBytes.isEmpty() && next == null)) {
            outChars.add(new Slurp(outLines));
        }
        
        if (!pipes.isEmpty() || (next == null && outBytes.isEmpty() && !outChars.isEmpty())) {
            for (int i = 1, stop = pipes.size(); i < stop; i++) {
                pipes.get(i - 1).sink = pipes.get(i);
            }
            if (next != null) {
                outChars.add(new WriteChars(new OutputStreamWriter(next, cs), true));
            }
            if (!outBytes.isEmpty()) {
                MissingProcess missing = new MissingProcess();
                threads.add(new Thread(new BytePump(missing.getInputStream(), outBytes)));
                outChars.add(new WriteChars(new OutputStreamWriter(missing, cs), true));
            }
            CharSink head = null;
            CharSink tail = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
            if (!pipes.isEmpty()) {
                head = pipes.get(0);
                pipes.get(pipes.size() - 1).sink = tail;
            } else {
                head = tail;
            }
            threads.add(new Thread(new CharPump(inputStream, cs, head)));
        } else {
            if (!outChars.isEmpty()) {
                CharSink head = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
                MissingProcess missing = new MissingProcess();
                threads.add(new Thread(new CharPump(missing.getInputStream(), cs, head)));
                outBytes.add(new Redirect(missing, true));
            }
            if (next != null) {
                outBytes.add(new Redirect(next, true));
            }
            threads.add(new Thread(new BytePump(inputStream, outBytes)));
        }
        
        final List<String> errLines = new ArrayList<String>();
        
        if (errorStream != null) {
            if (tee || (errBytes.isEmpty() && errChars.isEmpty())) {
                errChars.add(new Slurp(errLines));
            }

            if (errBytes.isEmpty()) {
                CharSink head = errChars.size() == 1 ? errChars.get(0) : new MultiplexedCharSink(errChars);
                threads.add(new Thread(new CharPump(process.getErrorStream(), cs, head)));
            } else {
                if (!errChars.isEmpty()) {
                    CharSink head = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
                    MissingProcess missing = new MissingProcess();
                    threads.add(new Thread(new CharPump(missing.getInputStream(), cs, head)));
                    errBytes.add(new Redirect(missing, true));
                }
                threads.add(new Thread(new BytePump(errorStream, outBytes)));
            }
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        if (previous != null) {
            previous.execute(outputStream, exit);
        }
        interruptable(new Interruptable() {
            public void run() throws InterruptedException {
                int result = process == null ? 0 : process.waitFor();
                for (Thread thread : threads) {
                    thread.join();
                }
                exit.add(new Exit(outLines, errLines, result));
            }
        });
    }
    
    void interruptable(Interruptable interruptable) {
        try {
            interruptable.run();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
    }
}
