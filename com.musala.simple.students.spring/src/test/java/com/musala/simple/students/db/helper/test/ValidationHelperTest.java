package com.musala.simple.students.db.helper.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.musala.simple.students.spring.web.helper.ValidationHelper;

public class ValidationHelperTest {

    @Test
    public void testIsInputValidMissingArgs() {
        assertEquals(false, ValidationHelper.isInputValid(new String[] {}));
    }

    @Test
    public void testIsInputValidIsJson() {
        assertEquals(true, ValidationHelper.isInputValid(new String[] { "src/main/resources/test.json" }));
    }

    @Test
    public void testIsInputValidNotJson() {
        assertEquals(false, ValidationHelper.isInputValid(new String[] { "src/main/resources/test.txt" }));
    }

    @Test
    public void testIsInputValidNotFile() {
        assertEquals(false, ValidationHelper.isInputValid(new String[] { "src/main/resources" }));
    }

    @Test
    public void testIsInputValidMultipleArgsValidPath() {
        assertEquals(true,
                ValidationHelper.isInputValid(new String[] { "src/main/resources/test.json", "test", "notValid" }));
    }

    @Test
    public void testIsInputValidMultipleArgsInvalidPath() {
        assertEquals(false,
                ValidationHelper.isInputValid(new String[] { "src/main/resources/tes", "test", "notValid" }));
    }

    @Test
    public void testIsValidJsonInvalidString() {
        assertEquals(false, ValidationHelper.isValidJson("key:value"));
    }

    @Test
    public void testIsValidJsonValidString() {
        assertEquals(true, ValidationHelper.isValidJson("{'key': 'value'}"));
    }

    @Test
    public void testValidUserDetailsRequestLessArgs() {
        assertEquals(false, ValidationHelper.validUserDetailsRequest(new String[1]));
    }

    @Test
    public void testValidUserDetailsRequestInvalidArgument() {
        assertEquals(false, ValidationHelper.validUserDetailsRequest(new String[] { "test", "id" }));
    }

    @Test
    public void testValidUserDetailsRequestValidArguments() {
        assertEquals(true, ValidationHelper.validUserDetailsRequest(new String[] { "test", "2" }));
    }

    @Test
    public void testValidUserDetailsRequestNegativeId() {
        assertEquals(false, ValidationHelper.validUserDetailsRequest(new String[] { "test", "-2" }));
    }
}
