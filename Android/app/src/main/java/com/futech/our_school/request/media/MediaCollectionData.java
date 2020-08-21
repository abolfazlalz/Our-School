package com.futech.our_school.request.media;

public class MediaCollectionData {

    private int id;
    private String name;
    private FileData[] files;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FileData[] getFiles() {
        return files;
    }
}
