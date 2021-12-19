package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("query")
public class QueryData {
    @XStreamAsAttribute
    String name;
    @XStreamAsAttribute
    String value;
}
