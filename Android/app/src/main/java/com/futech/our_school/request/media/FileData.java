package com.futech.our_school.request.media;

import org.joda.time.DateTime;

import java.util.Date;

public class FileData {

    private int id;
    private String address;
    private int collectionId;
    private String type;
    private Date dateCreated;
    private Date daeModified;

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDaeModified() {
        return daeModified;
    }
}
