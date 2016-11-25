package de.fraunhofer.iais.eis.biotope;

import org.apache.http.config.SocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@RestController
public class Controller  {

    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public void addAndSubscribeNode(@RequestBody OmiNode node) throws IOException {
        node.validate();

        service.addNode(node);
        service.subscribe(node);
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<OmiNode> getNodes() {
        return service.getOmiNodes();
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest body) {
        service.valueChanged(body.getParameter("msg"));
    }
}
