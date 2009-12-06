package com.goodworkalan.spawn;

import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.reflective.ReflectiveFactory;

public class ConstructorServer<C extends Consumer> implements ConsumerServer<C> {
    private final ReflectiveFactory reflectiveFactory = new ReflectiveFactory();
    
    private final Class<C> consumerClass;
    
    public ConstructorServer(Class<C> consumerClass) {
        this.consumerClass = consumerClass;
    }
    
    public C getConsumer() {
        try {
            return reflectiveFactory.getConstructor(consumerClass).newInstance();
        } catch (ReflectiveException e) {
            throw new SpawnException(0, e);
        }
    }
}
