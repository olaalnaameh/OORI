package de.fhg.ids.app.datadump;

import java.net.URL;

/**
 * Created by christian on 17.11.16.
 */
public class Node {

    private URL url;
    private String subscriptionXMLRequest;

    public static Node build(URL url, String subscriptionXMLRequest) {
        validateUrl(url);
        validateXml(subscriptionXMLRequest);
        return new Node(url, subscriptionXMLRequest);
    }

    private Node(URL url, String subscriptionXMLRequest) {
        this.url = url;
        this.subscriptionXMLRequest = subscriptionXMLRequest;
    }

    private static void validateUrl(URL url) {

    }

    private static void validateXml(String subscriptionXMLRequest) {

    }

    @Override
    public String toString() {
        return "Node: '" +url.toString()+ "', request: '" +subscriptionXMLRequest+ "'";
    }
}
