package com.goodworkalan.spawn;

public class SpawnError extends SpawnException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    
    private final Exit[] exits;

    public SpawnError(Exit[] exits) {
        super(0, null);
        this.exits = exits;
    }
    
    public Exit[] getExits() {
        return exits;
    }
}
