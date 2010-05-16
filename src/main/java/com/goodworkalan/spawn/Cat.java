package com.goodworkalan.spawn;

import java.io.InputStream;

public class Cat extends AbstractProgram {
    private final InputStream inputStream;

    public Cat(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
}
