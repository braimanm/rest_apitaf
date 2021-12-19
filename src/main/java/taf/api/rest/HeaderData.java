package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("header")
public class HeaderData {
    @XStreamAsAttribute
    String name;
    @XStreamAsAttribute
    String value;
}
