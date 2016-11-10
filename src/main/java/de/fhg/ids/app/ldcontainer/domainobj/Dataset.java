package de.fhg.ids.app.ldcontainer.domainobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class Dataset {

    private long id;
    private String metadataFilename;
    private String dataFilename;

    public Dataset(String filename, String metadataFilename)
    {
        this.id = System.currentTimeMillis();
        this.dataFilename = filename;
        this.metadataFilename = metadataFilename;
    }

    public long getId() {
        return id;
    }

    public String getFilename() {
        return dataFilename;
    }

    public String getMetadataFilename() {
        return metadataFilename;
    }
}
