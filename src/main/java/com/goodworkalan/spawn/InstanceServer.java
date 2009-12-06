package com.goodworkalan.spawn;

/**
 * A consumer server that serves a specific instance of a consumer.
 * 
 * @author Alan Gutierrez
 * 
 * @param <C>
 *            The consumer type.
 */
class InstanceServer<C extends Consumer> implements ConsumerServer<C> {
    /** The consumer. */
    private final C consumer;

    /**
     * Create an consumer server that will serve the given consumer.
     * 
     * @param consumer
     *            The consumer.
     */
    public InstanceServer(C consumer) {
        this.consumer = consumer;
    }

    /**
     * Get the already constructed consumer instance.
     * 
     * @return The consumer.
     */
    public C getConsumer() {
        return consumer;
    }
}
