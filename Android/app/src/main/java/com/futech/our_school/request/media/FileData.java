package com.futech.our_school.request.media;

import org.joda.time.DateTime;

public class FileData {

    private int id;
    private String address;
    private int collectionId;
    private String type;
    private DateTime dateCreated;
    private DateTime daeModified;

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public String getType() {
        return type;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public DateTime getDaeModified() {
        return daeModified;
    }
}
