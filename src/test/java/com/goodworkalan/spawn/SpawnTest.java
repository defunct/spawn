package com.goodworkalan.spawn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Spawn} class.
 *
 * @author Alan Gutierrez
 */
public class SpawnTest {
    /** Test a simple invocation. */
    @Test
    public void invoke() {
        Spawn spawn = new Spawn();

        spawn.setWorkingDirectory(new File("."));

        Exit exit = spawn.$$("cat", "src/test/resources/file.txt");
        assertTrue(exit.isSuccess());
        assertEquals(exit.code, 0);
        assertEquals(exit.out.get(0), "Hello, World!");
    }

    /** Test a number of different pipeline properties. */
    @Test
    public void cat() {
        Spawn spawn = new Spawn();

        spawn.setWorkingDirectory(new File("."));

        Exit exit = spawn.$$("cat", "src/test/resources/file.txt");
        assertTrue(exit.isSuccess());
        assertEquals(exit.code, 0);
        for (String line : exit.out) {
            System.out.println(line);
        }

        spawn.setUnexceptionalExitCodes(0);
        try {
            for (String line :  spawn.$$("cat", "src/test/resources/file.txt").out) {
                System.out.println(line);
            }
        } catch (SpawnException e) {
            System.out.println(e.getCode());
        }

        for (String zy : spawn.$("cat", "/usr/share/dict/words").$("grep", "Zyz").run().out) {
            System.out.println(zy);
        }
        for (String zy : spawn.$("cat", "/usr/share/dict/words").out(new Pipe() {
            public void in(String line, String ending) {
                out(line.toLowerCase(), ending);
            }
        }).$("grep", "^zy").run().out) {
            System.out.println(zy);
        }
        exit = spawn.$("cat", "src/test/resources/file.txt").out(new LineSink() {
            public void send(String line, String ending) {
                System.out.println(line);
            }
        }).run();
        
        spawn.$().run();
        
        spawn.$("cat", "src/test/resources/file.txt").$().run();
        
    }
    
    /** Test the virtual cat. */
    @Test
    public void pseudoCat() {
        Spawn spawn = new Spawn();
        spawn.setWorkingDirectory(new File("."));
        assertEquals(spawn.$(new ByteArrayInputStream("a".getBytes())).$("cat").run().out.get(0), "a");
    }
}
