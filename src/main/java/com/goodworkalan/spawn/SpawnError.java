package com.goodworkalan.spawn;

// TODO Document.
public class SpawnError extends SpawnException {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;
    
    // TODO Document.
    private final Exit[] exits;

    // TODO Document.
    public SpawnError(Exit[] exits) {
        super(0, null);
        this.exits = exits;
    }
    
    // TODO Document.
    public Exit[] getExits() {
        return exits;
    }
}
