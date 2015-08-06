package com.growapp.marvelheroes.model;

public class ImageItem {
    private String path;
    private String extension;

    public ImageItem() {
    }

    public ImageItem(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return path + "." + extension;
    }
}
