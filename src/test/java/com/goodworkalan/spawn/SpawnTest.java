package com.goodworkalan.spawn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.testng.annotations.Test;

public class SpawnTest {
    @Test
    public void cat() {
        Spawn<Slurp, Slurp> spawn = Spawn.spawn();

        spawn.setUnexceptionalExitCodes(0);
        spawn.setWorkingDirectory(new File("."));

        Exit<Slurp, Slurp> exit = spawn.execute("cat", "src/test/resources/file.txt");
        for (String line : exit.getStdOut().getLines()) {
            System.out.println(line);
        }

        assertTrue(exit.isSuccess());
        assertEquals(exit.getCode(), 0);
    }
}
