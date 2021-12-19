import com.thoughtworks.xstream.annotations.XStreamAlias;
import kong.unirest.Config;
import taf.api.rest.AbstractRESTTestCase;

@XStreamAlias("rest-test")
public class RESTTestCase extends AbstractRESTTestCase {

    @Override
    protected Config getConfig() {
        Config config = new Config();
        config.defaultBaseUrl(getBaseUrlFromEnvironment("api_url"));
        return config;
    }

}
