package com.goodworkalan.spawn;

/**
 * Constructor for a consumer.
 * 
 * @author Alan Gutierrez
 * 
 * @param <C>
 *            The consumer type.
 */
public interface ConsumerServer<C extends Consumer> {
    /**
     * Get an instance of the consumer.
     * 
     * @return An instance of the consumer.
     */
    public C getConsumer();
}
