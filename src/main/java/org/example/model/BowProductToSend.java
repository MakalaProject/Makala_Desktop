package org.example.model;

import org.example.interfaces.IChangeable;

public class BowProductToSend implements IChangeable<Integer> {
    protected boolean toDelete;
    protected Bow bow;
    public boolean isToDelete() {
        return toDelete;
    }

    @Override
    public Integer getId() {
        return null;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public Bow getBow() {
        return bow;
    }

    public void setBow(Bow bow) {
        this.bow = bow;
    }
}
