package taf.api.rest;

import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import org.apache.commons.jexl3.JexlContext;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.support.EnvironmentsSetup;
import ui.auto.core.support.TestContext;
import ui.auto.core.support.UserProvider;
import ui.auto.core.testng.TestNGBase;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIObject extends DataPersistence {

    private void addToGlobalAliases(DataPersistence data) {
        DataAliases global = PageComponentContext.getGlobalAliases();
        DataAliases local = data.getDataAliases();
        if (local != null) {
            global.putAll(local);
        }
    }

    private void resolveGlobalAliases() {
        DataAliases global = PageComponentContext.getGlobalAliases();
        for (String aliasKey : global.keySet()) {
            String aliasValue = global.getAsString(aliasKey);
            if (aliasValue != null && aliasValue.matches(".*\\$\\{[\\w-]+}.*")) {
                Pattern pat = Pattern.compile("\\$\\{[\\w-]+}");
                Matcher mat = pat.matcher(aliasValue);
                while (mat.find()) {
                    String alias = mat.group();
                    String key = alias.replace("${", "").replace("}", "");
                    if (global.containsKey(key)) {
                        String value = global.getAsString(key);
                        if (value != null) {
                            aliasValue = aliasValue.replace(alias, value);
                        }
                    }
                }
                global.put(aliasKey, aliasValue);
            }
        }
    }

    @Override
    protected void initJexlContext(JexlContext jexlContext) {
        LocalDateTime now = LocalDateTime.now();
        jexlContext.set("year_month_day", DateTimeFormatter.ofPattern("yyyy-M-d").format(now));
        jexlContext.set("day_month_year", DateTimeFormatter.ofPattern("d-M-yyyy").format(now));
        jexlContext.set("month_day_year", DateTimeFormatter.ofPattern("M-d-yyyy").format(now));
        jexlContext.set("time_stamp", DateTimeFormatter.ofPattern("yyyyMMddkkmmss-S").format(now));
        String timeStampThreadId = DateTimeFormatter.ofPattern("yyMMddkkmmss").format(now) + Thread.currentThread().getId();
        jexlContext.set("time_stamp_thread", timeStampThreadId);

        EnvironmentsSetup.Environment env = TestContext.getTestProperties().getTestEnvironment();
        if (env != null) {
            jexlContext.set("env", env);
            jexlContext.set("userProvider", UserProvider.getInstance());
        }
    }

    private List<Label> getLabels(DataPersistence data, String labelAlias, LabelName labelName) {
        DataAliases aliases = data.getDataAliases();
        String label = aliases.getAsString(labelAlias);
        List<Label> labels = new ArrayList<>();
        if (label != null) {
            for (String value : label.split(",")) {
                labels.add(new Label().withName(labelName.value()).withValue(value.trim()));
            }
        }
        return labels;
    }

    private void overwriteTestParameters(DataPersistence data) {
        DataAliases aliases = data.getDataAliases();
        if (aliases == null) return;
        String name = aliases.getAsString("test-name");
        String description = aliases.getAsString("test-description");

        List<Label> labels = new ArrayList<>();
        labels.addAll(getLabels(data, "test-features", LabelName.FEATURE));
        labels.addAll(getLabels(data, "test-stories", LabelName.STORY));
        labels.addAll(getLabels(data, "test-issues", LabelName.ISSUE));
        labels.addAll(getLabels(data, "test-IDs", LabelName.TEST_ID));
        labels.addAll(getLabels(data, "test-severity", LabelName.SEVERITY));

        Allure.LIFECYCLE.fire((TestCaseEvent) context -> {
            context.getLabels().addAll(labels);
            if (name != null) {
                context.setName(name);
            }
            if (description != null) {
                context.setDescription(new Description().withType(DescriptionType.MARKDOWN).withValue(description));
            }
        });
    }

    @Override
    public <T extends DataPersistence> T fromXml(String xml, boolean resolveAliases) {
        T data = super.fromXml(xml, resolveAliases);
        addToGlobalAliases(data);
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromURL(URL url, boolean resolveAliases) {
        T data = super.fromURL(url, resolveAliases);
        addToGlobalAliases(data);
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromInputStream(InputStream inputStream, boolean resolveAliases) {
        T data = super.fromInputStream(inputStream, resolveAliases);
        addToGlobalAliases(data);
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromFile(String filePath, boolean resolveAliases) {
        T data = super.fromFile(filePath, resolveAliases);
        addToGlobalAliases(data);
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFilePath, boolean resolveAliases) {
        T data = super.fromResource(resourceFilePath, resolveAliases);
        addToGlobalAliases(data);
        resolveGlobalAliases();
        overwriteTestParameters(data);
        TestNGBase.attachDataSet(data, resourceFilePath);

        String aliases = getXstream().toXML(TestContext.getGlobalAliases());
        MakeAttachmentEvent ev = new MakeAttachmentEvent(aliases.getBytes(), "Global Aliases : " + resourceFilePath, "text/xml");
        Allure.LIFECYCLE.fire(ev);
        return data;
    }

}
