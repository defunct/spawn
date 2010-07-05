package com.goodworkalan.spawn.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Builds the project definition for Spawn.
 *
 * @author Alan Gutierrez
 */
public class SpawnProject implements ProjectModule {
    /**
     * Build the project definition for Spawn.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.spawn/spawn/0.1.1.5")
                .depends()
                    .production("com.github.bigeasy.danger/danger/0.+1")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
