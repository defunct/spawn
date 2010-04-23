package com.goodworkalan.spawn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

public class SpawnTest {
    @Test
    public void cat() {
        Spawn spawn = new Spawn();

        spawn.setWorkingDirectory(new File("."));

        Exit exit = spawn.execute("cat", "src/test/resources/file.txt");
        assertTrue(exit.isSuccess());
        assertEquals(exit.code, 0);
        for (String line : exit.out) {
            System.out.println(line);
        }

        spawn.setUnexceptionalExitCodes(0);
        try {
            for (String line :  spawn.execute("cat", "src/test/resources/file.txt").out) {
                System.out.println(line);
            }
        } catch (SpawnException e) {
            System.out.println(e.getCode());
        }

        exit = spawn.out(new LineConsumer() {
            protected void consume(String line) {
                System.out.println(line);
            }
        }).execute("cat", "src/test/resources/file.txt");
    }
}
