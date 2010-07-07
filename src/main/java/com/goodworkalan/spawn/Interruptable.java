package com.goodworkalan.spawn;

/**
 * Wraps a block of code that calls a method that throws
 * <code>InterruptedException</code> and propagates the exception. This is used
 * with the {@link Executor#interruptable(Interruptable) Executor.interruptable}
 * method to isolate it for testing and package retry logic for reused.
 * 
 * @author Alan Gutierrez
 */
interface Interruptable {
    /**
     * Run a block of code that calls a method that throws
     * <code>InterruptedException</code>.
     * 
     * @throws InterruptedException
     *             If a wait is interrupted.
     */
    public void run() throws InterruptedException;
}
