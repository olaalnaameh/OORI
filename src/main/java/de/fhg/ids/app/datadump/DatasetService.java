package de.fhg.ids.app.datadump;

import de.fhg.ids.app.datadump.domainobj.Dataset;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.memory.model.MemValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Component
class DatasetService {

    private final Logger logger = LoggerFactory.getLogger(DatasetService.class);

    public final static String DATASET_DEFAULT_PATH = "/tmp/datasets";
    public final static String METADATA_DEFAULT_PATH = "/tmp/metadata";

    private Repository repository;
    private Collection<Dataset> datasets;
    private String datasetPath = DATASET_DEFAULT_PATH;
    private String metadataPath = METADATA_DEFAULT_PATH;

    public void init() throws IOException {
        datasets = new ArrayList<>();

        readDatasets();
        initMetadataRepository();
    }

    private void readDatasets() throws IOException {
        File datasetsFolder = new File(datasetPath);
        File[] allDatasets = datasetsFolder.listFiles();

        for (File dataset : allDatasets) {
            if (dataset.isFile()) {
                datasets.add(new Dataset(dataset.getAbsolutePath(), getMetadataFileForDataset(dataset)));
            }
        }
    }

    private String getMetadataFileForDataset(File file) throws IOException {
        String baseName = file.getName().substring(0, file.getName().indexOf("."));

        File metadataFolder = new File(metadataPath);
        File[] allMetadata = metadataFolder.listFiles();

        for (File metadata : allMetadata) {
            if (metadata.isFile() && metadata.getName().startsWith(baseName + "Metadata.")) {
                return metadata.getAbsolutePath();
            }
        }

        logger.info("No metadata definitions found for dataset '" +file.getName()+ "'");
        return "";
    }

    private Repository initMetadataRepository() throws IOException {
        logger.info("initializing memory repository");

        repository = new SailRepository(new MemoryStore());
        repository.initialize();
        RepositoryConnection repCon = repository.getConnection();
        try {
            for (Dataset dataset : datasets) {
                addDatasetMetadataToNamedGraph(dataset, repCon);
            }
        }
        finally {
            repCon.close();
        }

        return repository;
    }

    private void addDatasetMetadataToNamedGraph(Dataset dataset, RepositoryConnection repCon) throws IOException {
        logger.info("adding metadata of dataset '" + dataset.getFilename()+ "' (from '" +dataset.getMetadataFilename()+ "') to repository");

        Resource datasetContext = new MemValueFactory().createBNode(new Long(dataset.getId()).toString());
        repCon.add(new File(dataset.getMetadataFilename()), "", null, datasetContext);
        addDownloadUrl(repCon, dataset, datasetContext);
    }

    private void addDownloadUrl(RepositoryConnection repCon, Dataset dataset, Resource context) {
        Optional<Resource> datasetSubject = getDatasetSubject(repCon, context);
        if (datasetSubject.isPresent()) {
            IRI datasetDownloadIRI = repCon.getValueFactory().createIRI(createDownloadUrl(dataset.getId()));
            IRI downloadIRI = repCon.getValueFactory().createIRI(IDSV.downloadURL);

            repCon.add(repCon.getValueFactory().createStatement(datasetSubject.get(), downloadIRI, datasetDownloadIRI));
        }
    }

    private Optional<Resource> getDatasetSubject(RepositoryConnection repCon, Resource context) {
        IRI publisedDataset = repCon.getValueFactory().createIRI(IDSV.publishedDataset);
        RepositoryResult<Statement> datasetSubject = repCon.getStatements(null, RDF.TYPE, publisedDataset, context);
        while (datasetSubject.hasNext()) {
            return Optional.of(datasetSubject.next().getSubject());
        }

        return Optional.empty();
    }

    private String createDownloadUrl(long datasetId) {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            hostname = "localhost";
            logger.warn("Unable to get hostname. Using '" +hostname+ "'");
        }
        String port = System.getenv("SERVER_PORT");
        if (port == null) port = "8080";

        return "http://" + hostname + ":" + port + "/dataset/" +datasetId;
    }

    public Optional<Dataset> getDataset(long datasetId) {
        if (datasetId == 0l) {
            return datasets.stream().findFirst();
        }

        return datasets.stream().filter(description -> description.getId() == datasetId).findFirst();
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        try {
            init();
        }
        catch (Exception e) {
            logger.error("Error initializing dataset repository", e);
        }
    }

    public Collection<Dataset> getDatasets() {
        return datasets;
    }

    public Repository getRepository() {
        return repository;
    }

    // methods for testing only
    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }

    public void setMetadataPath(String metadataPath) {
        this.metadataPath = metadataPath;
    }

}
