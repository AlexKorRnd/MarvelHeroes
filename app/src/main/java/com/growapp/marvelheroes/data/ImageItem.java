package com.growapp.marvelheroes.data;

public class ImageItem {
    String path;
    String extension;

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
        return extension + "." + path;
    }
}
