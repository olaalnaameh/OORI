package de.fhg.ids.app.datadump;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Collection;

@RestController
public class Controller  {

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes/{url}", method = RequestMethod.POST)
    public void addNode(HttpServletRequest request, @PathVariable URL url) throws IOException {
        BufferedReader omiReadRequestReader = request.getReader();

        // todo:
        // 1. add node to list of "managed" nodes
        // 2. subscribe at the node for the objects passed in the request body

        addNode();
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public Collection<Node> getNodes() {
        return service.getNodes();
    }

}
