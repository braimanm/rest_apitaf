package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import datainstiller.data.DataAliases;
import ui.auto.core.context.PageComponentContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XStreamConverter(FieldToValidateConverter.class)
@XStreamAlias("field")
public class FieldToValidate {
    @XStreamAsAttribute
    String name;
    @XStreamAsAttribute
    String path;
    @XStreamAsAttribute
    String assertion;
    @XStreamAsAttribute
    String active;
    private String data;

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

    public String getData(boolean resolveAliases) {
        return resolveAliases ? resolveAliasesForData(data) : data;
    }

    void setData(String data) {
        this.data = data;
    }

}
