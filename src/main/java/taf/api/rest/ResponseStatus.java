package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class ResponseStatus {
    @XStreamAsAttribute
    int code;
    @XStreamAsAttribute
    String text;
}
