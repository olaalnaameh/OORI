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

    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public void addNode(HttpServletRequest request, @RequestParam URL url) throws IOException {
        String message = org.apache.commons.io.IOUtils.toString(request.getReader());
        Node newNode = Node.build(url, message);
        service.addNode(newNode);
        service.subscribe(newNode);
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<Node> getNodes() {
        return service.getNodes();
    }

}
