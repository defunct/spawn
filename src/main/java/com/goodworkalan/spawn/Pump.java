package com.goodworkalan.spawn;

import java.io.IOException;
import java.io.OutputStream;

public interface Pump {
    public void input(OutputStream out) throws IOException;
}
