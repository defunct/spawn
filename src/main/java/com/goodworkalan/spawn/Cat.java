package com.goodworkalan.spawn;

import java.io.InputStream;

// TODO Document.
public class Cat extends AbstractProgram {
    // TODO Document.
    private final InputStream inputStream;

    // TODO Document.
    public Cat(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    // TODO Document.
    public InputStream getInputStream() {
        return inputStream;
    }
}
