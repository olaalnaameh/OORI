package de.fhg.ids.app.ldcontainer;

import de.fhg.ids.app.ldcontainer.domainobj.Dataset;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.RDFWriterFactory;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Optional;

@RestController
public class Controller  {

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    DatasetService service;

    @RequestMapping(value = "/meta", method = RequestMethod.GET)
    public void getAllMetadata(HttpServletResponse response) throws IOException {
        RepositoryConnection repCon = service.getRepository().getConnection();
        RDFWriter writer = new TurtleWriter(response.getOutputStream());
        writer.startRDF();

        RepositoryResult<Statement> allStatements = repCon.getStatements(null, null, null);
        while (allStatements.hasNext()) {
            writer.handleStatement(allStatements.next());
        }
        writer.endRDF();
        response.getOutputStream().close();
    }

    @RequestMapping(value = "/", produces = "application/json")
    @ResponseBody
    public Collection<Dataset> getDatasets() {
        return service.getDatasets();
    }

    @RequestMapping(value = "/dataset/{datasetId}", method = RequestMethod.GET, produces = "application/x-turtle")
    @ResponseBody
    public AbstractResource getDataset(@PathVariable long datasetId) {
        Optional<Dataset> datasetDescription = service.getDataset(datasetId);
        if (datasetDescription.isPresent()) {
            Dataset dataset = datasetDescription.get();
            return new FileSystemResource(dataset.getFilename());
        }

        throw new IllegalArgumentException("no such dataset id");
    }

    @RequestMapping(value = "/dataset/{datasetId}/meta", method = RequestMethod.GET)
    public AbstractResource getMetadata(HttpServletResponse response, @PathVariable long datasetId) throws IOException {
        Optional<Dataset> datasetDescription = service.getDataset(datasetId);
        if (datasetDescription.isPresent()) {
            Dataset dataset = datasetDescription.get();
            return new FileSystemResource(dataset.getMetadataFilename());
        }

        throw new IllegalArgumentException("no such dataset id");
    }

}
