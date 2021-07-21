package org.example.model;

import org.example.interfaces.IChangeable;

public class BowProductToSend implements IChangeable<Integer> {
    protected boolean toDelete;
    protected Decoration decoration;
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

    public Decoration getBow() {
        return decoration;
    }

    public void setBow(Decoration decoration) {
        this.decoration = decoration;
    }
}
