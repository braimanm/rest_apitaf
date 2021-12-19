package taf.api.rest;

import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.StringAssert;

public enum AssertionType {
    EQUALS,
    NOT_EQUALS,
    MATCHES,
    CONTAINS,
    NOT_CONTAINS,
    STARTS_WITH,
    NOT_STARTS_WITH,
    ENDS_WITH,
    NOT_ENDS_WITH,
    LONG_GRATER_THAN,
    LONG_GRATER_THAN_OR_EQUAL,
    LONG_LESS_THAN,
    LONG_LESS_THAN_OR_EQUAL,
    LONG_IN_RANGE,
    DOUBLE_GRATER_THAN,
    DOUBLE_GRATER_THAN_OR_EQUAL,
    DOUBLE_LESS_THAN,
    DOUBLE_LESS_THAN_OR_EQUAL,
    DOUBLE_IN_RANGE;


    public void doAssert(SoftAssertions soft, String as, String actual, String expected) {
        StringAssert stringAssert = soft.assertThat(actual).as(as);
        String[] range;
        Long longAct;
        Long longExp;
        Double doubleAct;
        Double doubleExp;
        switch (this) {
            case EQUALS:
                stringAssert.isEqualTo(expected.trim());
                break;
            case NOT_EQUALS:
                stringAssert.isNotEqualTo(expected.trim());
                break;
            case MATCHES:
                stringAssert.matches(expected.trim());
                break;
            case CONTAINS:
                stringAssert.contains(expected.trim());
                break;
            case NOT_CONTAINS:
                stringAssert.doesNotContain(expected.trim());
                break;
            case STARTS_WITH:
                stringAssert.startsWith(expected.trim());
                break;
            case NOT_STARTS_WITH:
                stringAssert.doesNotStartWith(expected.trim());
                break;
            case ENDS_WITH:
                stringAssert.endsWith(expected.trim());
                break;
            case NOT_ENDS_WITH:
                stringAssert.doesNotEndWith(expected.trim());
                break;
            case LONG_GRATER_THAN:
                longAct = NumberUtils.createLong(actual.trim());
                longExp = NumberUtils.createLong(expected.trim());
                soft.assertThat(longAct).as(as).isGreaterThan(longExp);
                break;
            case LONG_GRATER_THAN_OR_EQUAL:
                longAct = NumberUtils.createLong(actual.trim());
                longExp = NumberUtils.createLong(expected.trim());
                soft.assertThat(longAct).as(as).isGreaterThanOrEqualTo(longExp);
                break;
            case LONG_LESS_THAN:
                longAct = NumberUtils.createLong(actual.trim());
                longExp = NumberUtils.createLong(expected.trim());
                soft.assertThat(longAct).as(as).isLessThan(longExp);
                break;
            case LONG_LESS_THAN_OR_EQUAL:
                longAct = NumberUtils.createLong(actual.trim());
                longExp = NumberUtils.createLong(expected.trim());
                soft.assertThat(longAct).as(as).isLessThanOrEqualTo(longExp);
                break;
            case LONG_IN_RANGE:
                longAct = NumberUtils.createLong(actual.trim());
                range = expected.trim().split("to");
                Long longMin = NumberUtils.createLong(range[0].trim());
                Long longMax = NumberUtils.createLong(range[1].trim());
                soft.assertThat(longAct).isBetween(longMin, longMax);
                break;
            case DOUBLE_GRATER_THAN:
                doubleAct = NumberUtils.createDouble(actual.trim());
                doubleExp = NumberUtils.createDouble(expected.trim());
                soft.assertThat(doubleAct).as(as).isGreaterThan(doubleExp);
                break;
            case DOUBLE_GRATER_THAN_OR_EQUAL:
                doubleAct = NumberUtils.createDouble(actual.trim());
                doubleExp = NumberUtils.createDouble(expected.trim());
                soft.assertThat(doubleAct).as(as).isGreaterThanOrEqualTo(doubleExp);
                break;
            case DOUBLE_LESS_THAN:
                doubleAct = NumberUtils.createDouble(actual.trim());
                doubleExp = NumberUtils.createDouble(expected.trim());
                soft.assertThat(doubleAct).as(as).isLessThan(doubleExp);
                break;
            case DOUBLE_LESS_THAN_OR_EQUAL:
                doubleAct = NumberUtils.createDouble(actual.trim());
                doubleExp = NumberUtils.createDouble(expected.trim());
                soft.assertThat(doubleAct).as(as).isLessThanOrEqualTo(doubleExp);
                break;
            case DOUBLE_IN_RANGE:
                doubleAct = NumberUtils.createDouble(actual.trim());
                range = expected.trim().split("to");
                Double doubleMin = NumberUtils.createDouble(range[0].trim());
                Double doubleMax = NumberUtils.createDouble(range[1].trim());
                soft.assertThat(doubleAct).isBetween(doubleMin, doubleMax);
                break;
        }
    }
}
