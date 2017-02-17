package de.fraunhofer.iais.eis.biotope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@RestController
public class Controller  {

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    NodeService service;

    @RequestMapping(value = "/nodes", method = RequestMethod.POST)
    public void addAndSubscribeAndSyncNode(@RequestBody OmiNode node) throws IOException {
        node.validate();

        service.addNode(node);
        service.subscribe(node);
        service.baselineSync(node);
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Collection<OmiNode> getNodes() {
        return service.getOmiNodes();
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest body) {
        String messageContent = body.getParameter("msg");
        if (messageContent == null) {
            logger.info("Received callback message with no content");
        }
        else {
            service.persistOmiMessageContent(body.getParameter("msg"));
        }
    }
}
