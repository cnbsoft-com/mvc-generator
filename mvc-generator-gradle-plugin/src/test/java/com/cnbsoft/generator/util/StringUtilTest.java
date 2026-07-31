package com.cnbsoft.generator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilTest {

    @Test
    public void capitalizeUppercasesFirstLetterOnly() {
        assertEquals("User", StringUtil.capitalize("user"));
        assertEquals("USER", StringUtil.capitalize("USER"));
    }

    @Test
    public void capitalizeHandlesNullAndEmpty() {
        assertNull(StringUtil.capitalize(null));
        assertEquals("", StringUtil.capitalize(""));
    }

    @Test
    public void toTitleCaseJoinsUnderscoreSeparatedWordsWithoutSeparator() {
        assertEquals("UserDetail", StringUtil.toTitleCase("USER_DETAIL"));
        assertEquals("User", StringUtil.toTitleCase("USER"));
    }

    @Test
    public void tableNameToJavaNameProducesPascalCase() {
        assertEquals("User", StringUtil.tableNameToJavaName("USER"));
        assertEquals("UserDetail", StringUtil.tableNameToJavaName("USER_DETAIL"));
        assertEquals("UserGroup", StringUtil.tableNameToJavaName("user_group"));
    }

    @Test
    public void toFirstLowerLowercasesFirstLetterOnly() {
        assertEquals("user", StringUtil.toFirstLower("User"));
        assertEquals("userDetail", StringUtil.toFirstLower("UserDetail"));
    }

    @Test
    public void toFirstLowerHandlesNullAndEmpty() {
        assertNull(StringUtil.toFirstLower(null));
        assertEquals("", StringUtil.toFirstLower(""));
    }
}
