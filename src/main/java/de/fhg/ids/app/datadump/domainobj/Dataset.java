package de.fhg.ids.app.datadump.domainobj;

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
