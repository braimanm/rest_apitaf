package taf.api.rest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import kong.unirest.*;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.support.TestContext;

@SuppressWarnings("unused")
@XStreamAlias("rest-test-case")
public class RESTTestCase extends APIObject {
    private RequestData request;
    private ResponseData response;

    private String getBaseUrl() {
        String apiUrl = TestContext.getTestProperties().getTestEnvironment().getCustom("api_url");
        return (request.getEndPoint(true).startsWith("http")) ? null : apiUrl;
    }

    private Config getConfig() {
        Config config = new Config();
        config.verifySsl(false).defaultBaseUrl(getBaseUrl());
        return config;
    }

    public RequestData getRequest() {
        return request;
    }

    @Step("Execute GET Request")
    public ResponseData executeGet() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        GetRequest req = uni.get(request.getEndPoint(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

    @Step("Execute POST Request")
    public ResponseData executePost() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        RequestBodyEntity req = uni.post(request.getEndPoint(true)).body(request.getPayloadBody(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

    @Step("Execute PUT Request")
    public ResponseData executePut() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        RequestBodyEntity req = uni.put(request.getEndPoint(true)).body(request.getPayloadBody(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

    @Step("Execute PATCH Request")
    public ResponseData executePatch() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        RequestBodyEntity req = uni.patch(request.getEndPoint(true)).body(request.getPayloadBody(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

    @Step("Execute DELETE Request")
    public ResponseData executeDelete() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        RequestBodyEntity req = uni.delete(request.getEndPoint(true)).body(request.getPayloadBody(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

    @Step("Execute OPTIONS Request")
    public ResponseData executeOptions() {
        UnirestInstance uni = new UnirestInstance(getConfig());
        GetRequest req = uni.options(request.getEndPoint(true));
        req.headers(request.getHeaders());
        req.queryString(request.getQueries());
        response.response = req.asJson();
        uni.close();
        return response;
    }

}
