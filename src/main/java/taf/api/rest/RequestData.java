package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import datainstiller.data.DataAliases;
import ui.auto.core.context.PageComponentContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
@XStreamAlias("request")
public class RequestData  {
    private String endPoint;
    private String payloadBody;
    @XStreamImplicit
    private List<HeaderData> headers;
    @XStreamImplicit
    private List<QueryData> queries;

    private String resolveAliasesForData(String data) {
        String dat = data;
        Pattern pat = Pattern.compile("\\$\\{[\\w-]+}");
        Matcher mat = pat.matcher(dat);
        while (mat.find()) {
            String alias = mat.group();
            String key = alias.replace("${", "").replace("}", "");
            DataAliases aliases = PageComponentContext.getGlobalAliases();
            if (aliases.containsKey(key)) {
                String value = aliases.get(key).toString();
                if (value != null) {
                    dat = dat.replace(alias, value);
                }
            }
        }
        return dat;
    }

    private Map<String,String> getDefaultHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept", "*/*");
        return headers;
    }

    private Map<String,String> getHeadersAsMap() {
        Map<String,String> headersMap = new HashMap<>();
        for (HeaderData header : this.headers) {
            headersMap.put(header.name, header.value);
        }
        return headersMap;
    }

    public Map<String,Object> getQueries() {
        Map<String, Object> queriesMap = new HashMap<>();
        if (queries != null && !queries.isEmpty()) {
            for (QueryData query : this.queries) {
                queriesMap.put(query.name, query.value);
            }
        }
        return queriesMap;
    }

    public Map<String ,String> getHeaders() {
        if (headers != null && !headers.isEmpty()) {
            return getHeadersAsMap();
        } else {
            return getDefaultHeaders();
        }
    }

    public String getEndPoint(boolean resolveAliases) {
        return resolveAliases ? resolveAliasesForData(endPoint): endPoint;
    }

    public String getPayloadBody(boolean resolveAliases) {
        return resolveAliases ? resolveAliasesForData(payloadBody): payloadBody;
    }

}
