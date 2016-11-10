package de.fhg.ids.app.ldcontainer;

import de.fhg.ids.app.ldcontainer.domainobj.Dataset;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.util.iterators.Iterators;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Collection;

public class DatasetServiceTest {

    private DatasetService service;

    @BeforeClass
    public static void prepareDatasets() throws IOException {
        DatasetInstaller datasetInstaller = new DatasetInstaller();
        datasetInstaller.installTestResources();
    }

    @Before
    public void setUp() throws IOException {
        service = new DatasetService();
        service.init();
    }

    @Test
    public void initRepository() throws IOException {
        RepositoryConnection repCon = service.getRepository().getConnection();
        try {
            Assert.assertTrue(repCon.size() > 0);
        }
        finally {
            repCon.close();
        }
    }

    @Test
    public void containsDatasetMetadata() throws IOException {
        RepositoryConnection repCon = service.getRepository().getConnection();
        RepositoryResult<Statement> datasets = repCon.getStatements(null, RDF.TYPE, repCon.getValueFactory().createIRI(IDSV.publishedDataset));

        Assert.assertEquals(1, Iterations.asList(datasets).size());
    }

    @Test
    public void datasetMetadataContainsDownloadUrl() throws IOException {
        RepositoryConnection repCon = service.getRepository().getConnection();
        RepositoryResult<Statement> datasets = repCon.getStatements(null, repCon.getValueFactory().createIRI(IDSV.downloadURL), null);

        Assert.assertEquals(1, Iterations.asList(datasets).size());
    }

}
