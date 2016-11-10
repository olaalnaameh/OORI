package de.fhg.ids.app.ldcontainer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DatasetInstaller {

    public void installTestResources() throws IOException {
        removeExistingDirs();
        createDirs();
        copyFiles();
    }

    private void removeExistingDirs() throws IOException {
        FileUtils.deleteDirectory(new File(DatasetService.DATASET_DEFAULT_PATH));
        FileUtils.deleteDirectory(new File(DatasetService.METADATA_DEFAULT_PATH));
    }

    private void createDirs() {
        new File(DatasetService.DATASET_DEFAULT_PATH).mkdir();
        new File(DatasetService.METADATA_DEFAULT_PATH).mkdir();
    }

    private void copyFiles() throws IOException {
        File dataset = new File("src/test/resources/CyclewayThing.nt");
        File metadata = new File("src/test/resources/CyclewayThingMetadata.ttl");

        FileUtils.copyFileToDirectory(dataset, new File(DatasetService.DATASET_DEFAULT_PATH));
        FileUtils.copyFileToDirectory(metadata, new File(DatasetService.METADATA_DEFAULT_PATH));
    }

}
