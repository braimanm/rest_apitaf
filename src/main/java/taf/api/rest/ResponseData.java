package taf.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataAliases;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.support.TestContext;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused")
@XStreamAlias("response")
public class ResponseData {
    ResponseStatus status;
    List<FieldToValidate> fieldsToValidate;
    @XStreamOmitField
    HttpResponse<JsonNode> response;
    @XStreamOmitField
    private com.fasterxml.jackson.databind.JsonNode jsonNode;
    @XStreamOmitField
    private SoftAssertions soft;


    public void validateStatus() {
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.getStatus()).isEqualTo(status.code);
        soft.assertThat(response.getStatusText()).isEqualTo(status.text);
        soft.assertAll();
    }

    public String getJson() {
        JsonNode node = response.getBody();
        if (node == null) {
            return "";
        }
        return node.toPrettyString();
    }

    public com.fasterxml.jackson.databind.JsonNode getJsonNode() {
        if (jsonNode != null) return jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(response.getBody().toString());
            return jsonNode;
        } catch (IOException e) {
            throw new RuntimeException("API Service Error!");
        }
    }

    @Step("Validate field with path \"{0}\" and expected value \"{1}\"")
    public void validate(String path, String expected) {
        String actual = getValueAsString(path);
        Assertions.assertThat(actual)
                .as("Field Path: " + path)
                .isEqualTo(expected);
    }

    @Step("Validate field \"{0}\" with expected value {3} \"{2}\"")
    private void validate(String name, String path, String expected, String assertionType) {
        String actual = getValueAsString(path);
        String as = "Field Name: " + name + " , Field Path: " + path;
        if (assertionType != null && !assertionType.isEmpty() && !actual.equals("null")) {
            AssertionType.valueOf(assertionType).doAssert(soft, as, actual, expected);
        } else {
            AssertionType.EQUALS.doAssert(soft, as, actual, expected);
        }
    }

    public void validateAll() {
        if (fieldsToValidate == null || fieldsToValidate.isEmpty()) return;
        soft = new SoftAssertions();
        for (FieldToValidate fieldToValidate : fieldsToValidate) {
                String active = fieldToValidate.active;
                if (active != null && active.trim().matches("\\$\\{[\\w-]+}")) {
                    active = active.replace("${", "").replace("}", "");
                    active = TestContext.getGlobalAliases().getAsString(active.trim());
                }
                if (active == null || active.equalsIgnoreCase("true")) {
                    String assertion = (fieldToValidate.assertion == null) ? AssertionType.EQUALS.name() : fieldToValidate.assertion;
                    validate(fieldToValidate.name, fieldToValidate.path, fieldToValidate.getData(true), assertion);
                }
        }
        soft.assertAll();
        soft = null;
    }

    public com.fasterxml.jackson.databind.JsonNode getValueAsNode(String path) {
        com.fasterxml.jackson.databind.JsonNode jsonNode = getJsonNode();
        JmesPath<com.fasterxml.jackson.databind.JsonNode> jmesPath = new JacksonRuntime();
        Expression<com.fasterxml.jackson.databind.JsonNode> exp = jmesPath.compile(path);
        return exp.search(jsonNode);
    }

    public String getValueAsString(String path) {
        com.fasterxml.jackson.databind.JsonNode jsonNode = getValueAsNode(path);
        if (jsonNode.isContainerNode()) {
            String out = null;
            try {
                out = new ObjectMapper().writeValueAsString(jsonNode);
            } catch (JsonProcessingException ignore) {
            }
            return out;
        }
        return jsonNode.asText();
    }

    public String getValueAsJsonString(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        String out = null;
        try {
            out = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(getValueAsNode(path));
        } catch (JsonProcessingException ignore) {
        }
        return out;
    }

    public Number getValueAsNumber(String path) {
        return getValueAsNode(path).numberValue();
    }

    public Boolean getValueAsBoolean(String path) {
        return getValueAsNode(path).booleanValue();
    }

    public void setAlias(String name, String value) {
        DataAliases globalAliases = TestContext.getGlobalAliases();
        globalAliases.put(name, value);
    }

    public void setResponseFiledAsAlias(String name, String path) {
        setAlias(name, getValueAsString(path));
    }

}
