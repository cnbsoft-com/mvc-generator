package com.cnbsoft.generator.engine;

import org.junit.Test;

import java.sql.Types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * ColumnInspector.normalizeClassName 의 DBMS 종속 JDBC 타입 → 표준 Java 타입 정규화 규칙을 검증한다.
 * DB 연결이 필요한 다른 메서드는 대상이 아니다.
 */
public class ColumnInspectorTest {

    @Test
    public void numericWithZeroScaleNormalizesToLong() {
        assertEquals("java.lang.Long",
                ColumnInspector.normalizeClassName("java.math.BigDecimal", Types.NUMERIC, 0));
    }

    @Test
    public void numericWithNonZeroScaleNormalizesToDouble() {
        assertEquals("java.lang.Double",
                ColumnInspector.normalizeClassName("java.math.BigDecimal", Types.NUMERIC, 2));
    }

    @Test
    public void decimalFollowsSameScaleRuleAsNumeric() {
        assertEquals("java.lang.Long",
                ColumnInspector.normalizeClassName("java.math.BigDecimal", Types.DECIMAL, 0));
        assertEquals("java.lang.Double",
                ColumnInspector.normalizeClassName("java.math.BigDecimal", Types.DECIMAL, 4));
    }

    @Test
    public void timestampTypesNormalizeToSqlTimestamp() {
        assertEquals("java.sql.Timestamp",
                ColumnInspector.normalizeClassName("oracle.sql.TIMESTAMP", Types.TIMESTAMP, 0));
        assertEquals("java.sql.Timestamp",
                ColumnInspector.normalizeClassName("oracle.sql.TIMESTAMP", Types.TIMESTAMP_WITH_TIMEZONE, 0));
    }

    @Test
    public void dateNormalizesToSqlDate() {
        assertEquals("java.sql.Date",
                ColumnInspector.normalizeClassName("oracle.sql.DATE", Types.DATE, 0));
    }

    @Test
    public void timeTypesNormalizeToSqlTime() {
        assertEquals("java.sql.Time",
                ColumnInspector.normalizeClassName("java.sql.Time", Types.TIME, 0));
        assertEquals("java.sql.Time",
                ColumnInspector.normalizeClassName("java.sql.Time", Types.TIME_WITH_TIMEZONE, 0));
    }

    @Test
    public void otherTypesPassThroughUnchanged() {
        assertEquals("java.lang.String",
                ColumnInspector.normalizeClassName("java.lang.String", Types.VARCHAR, 0));
    }

    @Test
    public void validateIdentifierAcceptsAlphanumericUnderscoreDollarHash() {
        ColumnInspector.validateIdentifier("USER");
        ColumnInspector.validateIdentifier("user_detail");
        ColumnInspector.validateIdentifier("T1$");
        ColumnInspector.validateIdentifier("MY_SCHEMA#1");
    }

    @Test
    public void validateIdentifierRejectsSqlInjectionAttempt() {
        assertThrows(IllegalArgumentException.class,
                () -> ColumnInspector.validateIdentifier("USER; DROP TABLE USER; --"));
    }

    @Test
    public void validateIdentifierRejectsWhitespaceAndQualifiedNames() {
        assertThrows(IllegalArgumentException.class,
                () -> ColumnInspector.validateIdentifier("USER TABLE"));
        assertThrows(IllegalArgumentException.class,
                () -> ColumnInspector.validateIdentifier("SCHEMA.USER"));
    }

    @Test
    public void validateIdentifierRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ColumnInspector.validateIdentifier(null));
    }
}
