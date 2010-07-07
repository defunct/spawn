package com.goodworkalan.spawn;

import static com.goodworkalan.spawn.SpawnException.CLOSE_INPUT_FAILURE;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// TODO Document.
public class Executor {
    // TODO Document.
    private final Executor previous;
    
    // TODO Document.
    private final Spawn spawn;
    
    // TODO Document.
    private final Program program;
    
    // TODO Document.
    private final List<Pipe> pipes = new ArrayList<Pipe>();
    
    // TODO Document.
    private final List<ByteSink> outBytes = new ArrayList<ByteSink>();
    
    // TODO Document.
    private final List<CharSink> outChars = new ArrayList<CharSink>();
    
    // TODO Document.
    private final List<ByteSink> errBytes = new ArrayList<ByteSink>();
    
    // TODO Document.
    private final List<CharSink> errChars = new ArrayList<CharSink>();
    
    // TODO Document.
    private boolean tee;

    // TODO Document.
    Executor(Executor previous, Spawn spawn, Program program) {
        this.previous = previous;
        this.spawn = spawn;
        this.program = program;
    }
    
    // TODO Document.
    public Executor $(List<String> command) {
        return new Executor(this, spawn, new Executable(command));
    }
    
    // TODO Document.
    public Executor $() {
        return new Executor(this, spawn, new MissingProcess());
    }

    // TODO Document.
    public Executor $(String... command) {
        return $(Arrays.asList(command));
    }
    
    // FIXME Need some way to say tee? To say, do not neglect to collect lines?
    public Executor out(OutputStream out) {
        return out(new Redirect(out, false));
    }
    
    // TODO Document.
    public Executor tee() {
        tee = true;
        return this;
    }
    
    // TODO Document.
    public Executor err(OutputStream err) {
        return err(new Redirect(err, false));
    }
    
    // TODO Document.
    public Executor out(Pipe pipe) {
        pipes.add(pipe);
        return this;
    }

    // TODO Document.
    public Executor out(ByteSink sink) {
        outBytes.add(sink);
        return this;
    }
    
    // TODO Document.
    public Executor out(CharSink sink) {
        outChars.add(sink);
        return this;
    }
    
    // TODO Document.
    public Executor err(ByteSink sink) {
        errBytes.add(sink);
        return this;
    }
    
    // TODO Document.
    public Executor err(CharSink chars) {
        errChars.add(chars);
        return this;
    }
    
    // TODO Document.
    public Exit run() {
        LinkedList<Exit> exits = new LinkedList<Exit>();
        execute(null, exits);
        return exits.getLast();
    }
    
    // TODO Document.
    public Exit[] pipe() {
        List<Exit> exits = new ArrayList<Exit>();
        execute(null, exits);
        return exits.toArray(new Exit[exits.size()]); 
    }
    
    // TODO Document.
    void execute(OutputStream next, final List<Exit> exit) {
        program.run(spawn);
        
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
                outChars.add(new WriteChars(new OutputStreamWriter(missing.getOutputStream(), cs), true));
            }
            CharSink head = null;
            CharSink tail = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
            if (!pipes.isEmpty()) {
                head = pipes.get(0);
                pipes.get(pipes.size() - 1).sink = tail;
            } else {
                head = tail;
            }
            threads.add(new Thread(new CharPump(program.getInputStream(), cs, head)));
        } else {
            if (!outChars.isEmpty()) {
                CharSink head = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
                MissingProcess missing = new MissingProcess();
                threads.add(new Thread(new CharPump(missing.getInputStream(), cs, head)));
                outBytes.add(new Redirect(missing.getOutputStream(), true));
            }
            if (next != null) {
                outBytes.add(new Redirect(next, true));
            }
            threads.add(new Thread(new BytePump(program.getInputStream(), outBytes)));
        }
        
        final List<String> errLines = new ArrayList<String>();
        
        if (tee || (errBytes.isEmpty() && errChars.isEmpty())) {
            errChars.add(new Slurp(errLines));
        }

        if (errBytes.isEmpty()) {
            CharSink head = errChars.size() == 1 ? errChars.get(0) : new MultiplexedCharSink(errChars);
            threads.add(new Thread(new CharPump(program.getErrorStream(), cs, head)));
        } else {
            if (!errChars.isEmpty()) {
                CharSink head = outChars.size() == 1 ? outChars.get(0) : new MultiplexedCharSink(outChars);
                MissingProcess missing = new MissingProcess();
                threads.add(new Thread(new CharPump(missing.getInputStream(), cs, head)));
                errBytes.add(new Redirect(missing.getOutputStream(), true));
            }
            threads.add(new Thread(new BytePump(program.getErrorStream(), outBytes)));
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        if (previous == null) {
            try {
                program.getOutputStream().close();
            } catch (IOException e) {
                throw new SpawnException(CLOSE_INPUT_FAILURE, e);
            }
        } else {
            previous.execute(program.getOutputStream(), exit);
        }

        interruptable(new Interruptable() {
            public void run() throws InterruptedException {
                int result = program.waitFor();
                for (Thread thread : threads) {
                    thread.join();
                }
                exit.add(new Exit(outLines, errLines, result));
            }
        });
    }
    
    // TODO Document.
    void interruptable(Interruptable interruptable) {
        try {
            interruptable.run();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
    }
}
