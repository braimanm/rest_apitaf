import org.testng.annotations.Test;
import taf.api.rest.ResponseData;
import ui.auto.core.testng.TestNGBase;

public class TestApi extends TestNGBase {

    @Test
    public void testGetUsers() {
        RESTTestCase testCase = new RESTTestCase().fromResource("data/dataset1.xml");
        ResponseData res = testCase.executeGet();
        System.out.println(res.getJson());
        res.validateStatus();
        res.validateAll();
    }

    @Test
    public void testPostUsers() {
        RESTTestCase testCase = new RESTTestCase().fromResource("data/dataset2.xml");
        ResponseData res = testCase.executePost();
        System.out.println(res.getJson());
        res.validateStatus();
        res.validateAll();
        res.setResponseFiledAsAlias("idOut","id");

    }



}
