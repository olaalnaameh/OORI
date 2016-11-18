package de.fhg.ids.app.datadump;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@RestController
public class Controller  {

    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public void addNode(@RequestParam URL url, @RequestParam String omiReadRequest) throws IOException {
        OmiNode newOmiNode = OmiNode.build(url, omiReadRequest);

        service.addNode(newOmiNode);
        service.subscribe(newOmiNode);
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<OmiNode> getNodes() {
        return service.getOmiNodes();
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void callback() {

    }
}
