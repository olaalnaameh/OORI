package de.fraunhofer.iais.eis.biotope;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OdfRdfRepository {

    private final Logger logger = LoggerFactory.getLogger(OdfRdfRepository.class);

    @Value("${endpoint.url:}")
    private String endpointUrl;

    private Repository repository;

    @PostConstruct
    private void postConstruct() {
        if (endpointUrl == null || endpointUrl.isEmpty()) {
            logger.info("Preparing memory repository");
            repository = new SailRepository(new MemoryStore());
        }
        else {
            logger.info("Setting HTTP repository at '" + endpointUrl + "'");
            repository = new SPARQLRepository(endpointUrl);
        }

        repository.initialize();
    }

    public void persist(Model model) {
        RepositoryConnection repCon = repository.getConnection();
        try {
            repCon.add(model);
        }
        finally {
            repCon.close();
        }
    }

}
