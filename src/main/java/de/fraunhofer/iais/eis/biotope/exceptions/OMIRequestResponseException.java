package de.fraunhofer.iais.eis.biotope.exceptions;

/**
 * Created by christian on 21.11.16.
 */
public class OMIRequestResponseException extends RuntimeException {

    public OMIRequestResponseException(String message) {
        super(message);
    }

    public OMIRequestResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
