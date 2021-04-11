package org.example.model;

import java.io.File;

public class Picture {
    private int id;
    private String path;
    // private File image;
    private boolean toDelete = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Picture(String path){
        this.path = path;
    }

    /*public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }*/

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }
}
