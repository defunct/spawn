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

/**
 * A program in a pipeline with its associated standard error and standard
 * output handlers. The <code>Executor</code> is a participant in a builder
 * pattern. An <code>Executor</code> is used to specify a pipeline, possibly
 * creating new <code>Executor</code> instances that extend the pipeline.
 * <p>
 * The <code>Executor</code> also provides <code>run</code> methods which will
 * execute the pipeline. After execution, the <code>Executor</code> should be
 * discarded.
 * 
 * @author Alan Gutierrez
 */
public class Executor {
    /** The previous executor in the pipeline or null for the first executor. */
    private final Executor previous;
    
    /** The environment. */
    private final Spawn spawn;
    
    /** The program. */
    private final Program program;
    
    /** The pipes that will process output line by line. */
    private final List<Pipe> pipes = new ArrayList<Pipe>();
    
    /** The byte sinks for standard output. */
    private final List<ByteSink> outBytes = new ArrayList<ByteSink>();
    
    /** The character sinks for standard output. */
    private final List<CharSink> outChars = new ArrayList<CharSink>();
    
    /** The byte sinks for standard error. */
    private final List<ByteSink> errBytes = new ArrayList<ByteSink>();
    
    /** The character sinks for standard error. */
    private final List<CharSink> errChars = new ArrayList<CharSink>();
    
    /** Whether to collect lines in the exit status regardless of redirection. */
    private boolean tee;

    /**
     * Create an executor.
     * 
     * @param previous
     *            The previous executor in the pipeline or null for the first
     *            executor.
     * @param spawn
     *            The environment.
     * @param program
     *            The program.
     */
    Executor(Executor previous, Spawn spawn, Program program) {
        this.previous = previous;
        this.spawn = spawn;
        this.program = program;
    }
    
    /**
     * Extend the pipeline by piping the output of this executor to the program
     * specified by the command line.
     * 
     * @param command
     *            The command line.
     * @return A new executor for the given command line.
     */
    public Executor $(List<String> command) {
        return new Executor(this, spawn, new Executable(command));
    }
    
    /**
     * Extend the pipeline by piping the output of this executor to a do 
     * nothing process.
     * @return A new executor for the line.
     */
    public Executor $() {
        return new Executor(this, spawn, new MissingProcess());
    }

    /**
     * Extend the pipeline by piping the output of this executor to the program
     * specified by the command line.
     * 
     * @param command
     *            The command line.
     * @return A new executor for the given command line.
     */
    public Executor $(String... command) {
        return $(Arrays.asList(command));
    }

    /**
     * Redirect the standard output to the output stream.
     * 
     * @param out
     *            The output stream.
     * @return This executor to continue building the pipeline.
     */
    public Executor out(OutputStream out) {
        return out(new Redirect(out, false));
    }

    /**
     * Collect lines in the exit status regardless of redirection. By default,
     * if byte or character sinks have been assigned to an <code>Executor</code>
     * , or if the <code>Executor</code> pipes to a another
     * <code>Executor</code>, the gathering of lines in the <code>Exit</code>
     * status is disabled. Calling this method forces the gathering of lines in
     * the <code>Exit</code> status.
     * 
     * @return This executor to continue building the pipeline.
     */
    public Executor tee() {
        tee = true;
        return this;
    }
    
    /**
     * Redirect the error output to the output stream.
     * 
     * @param err
     *            The error output stream.
     * @return This executor to continue building the pipeline.
     */
    public Executor err(OutputStream err) {
        return err(new Redirect(err, false));
    }

    /**
     * Add a pipe to a chain of pipes that will process the output of the
     * program line by line and forward the output to the next pipe in the
     * chain, then onto the next program in pipeline.
     * <p>
     * The word pipe here is getting overloaded. A <code>Pipe</code> is a way to
     * inject some Java code into the pipeline. The first <code>Pipe</code> in
     * the chain of pipes in the <code>Executor</code> processes the output of
     * the <code>Program</code> line by line. The output of the last
     * <code>Pipe</code> in the pipeline is fed to the next program in the
     * pipeline and to any of the <code>ByteSink</code> or <code>CharSink</code>
     * instances attached to the <code>Executor</code>. If there is more than
     * one <code>Pipe</code> it is considered part of a chain of pipes, and the
     * output of one pipe is fed to the next pipe in the chain.
     * 
     * @param pipe
     *            The block of code to process the output line by line.
     * @return This executor to continue building the pipeline.
     */
    public Executor out(Pipe pipe) {
        pipes.add(pipe);
        return this;
    }

    /**
     * Process standard output with the given output sink.
     * 
     * @param sink
     *            The output sink.
     * @return This executor to continue building the pipeline.
     */
    public Executor out(ByteSink sink) {
        outBytes.add(sink);
        return this;
    }

    /**
     * Add the character sink to the list of character sinks that consume the
     * executor standard output.
     * 
     * @param sink
     *            The character sink.
     * @return This executor to continue building the pipeline.
     */
    public Executor out(CharSink sink) {
        outChars.add(sink);
        return this;
    }

    /**
     * Add the byte sink to the list of byte sinks that consume the executor
     * error output.
     * 
     * @param sink
     *            The byte sink.
     * @return This executor to continue building the pipeline.
     */
    public Executor err(ByteSink sink) {
        errBytes.add(sink);
        return this;
    }
    
    /**
     * Add the character sink to the list of character sinks that consume the
     * executor error output.
     * 
     * @param sink
     *            The character sink.
     * @return This executor to continue building the pipeline.
     */
    public Executor err(CharSink sink) {
        errChars.add(sink);
        return this;
    }
    
    /**
     * Execute the pipeline and return the exist status of the last executor.
     * 
     * @return The exit status of the last executor.
     */
    public Exit run() {
        LinkedList<Exit> exits = new LinkedList<Exit>();
        execute(null, exits);
        return exits.getLast();
    }

    /**
     * Run the program.
     * 
     * @param next
     *            The output stream of the next executor.
     * @param exit
     *            The list of exit statuses.
     */
    void execute(OutputStream next, final LinkedList<Exit> exit) {
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
                exit.add(new Exit(exit, outLines, errLines, result));
            }
        });
    }

    /**
     * Execute a block of code that calls methods that throw
     * <code>InterruptableException</code> rerunning the block of code if
     * <code>InterruptableException</code> is thrown.
     * 
     * @param interruptable
     *            The block of interruptable code.
     */
    void interruptable(Interruptable interruptable) {
        try {
            interruptable.run();
        } catch (InterruptedException e) {
            throw new SpawnException(0, e);
        }
    }
}
