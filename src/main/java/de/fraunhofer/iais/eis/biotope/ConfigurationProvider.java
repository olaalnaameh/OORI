package de.fraunhofer.iais.eis.biotope;

public class ConfigurationProvider {

    private final static String
        PROTOCOL = determineProtocol(),
        CALLBACK_PORT = determineCallbackPort(),
        SERVER_PORT = determineServerPort(),
        HOSTNAME = determineHostname();

    private static String determineProtocol() {
        String protocol = System.getenv("PROTOCOL");
        return protocol == null ? "http" : protocol;
    }

    private static String determineHostname() {
        String hostname = System.getenv("HOSTNAME");
        return hostname == null ? "localhost" : hostname;
    }

    private static String determineCallbackPort() {
        String port = System.getenv("CALLBACK_PORT");
        return port == null ? "9090" : port;
    }

    private static String determineServerPort() {
        String port = System.getenv("SERVER_PORT");
        return port == null ? "9090" : port;
    }

    public static String getCallbackUrl() {
        return PROTOCOL + "://" + HOSTNAME + ":" + CALLBACK_PORT + "/callback";
    }

    public static String getInfoItemBaseIri() {
        return PROTOCOL + "://" + HOSTNAME + ":" + SERVER_PORT + "/infoitem/";
    }

    public static String getObjectBaseIri() {
        return PROTOCOL + "://" + HOSTNAME + ":" + SERVER_PORT + "/obj/";
    }

}
