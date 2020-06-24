package com.accounts.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeselValidatorTest {

    PeselValidator peselValidator = new PeselValidator();

    @Test
    public void testValidAdultAndDate() {
        assertTrue(peselValidator.isValidAdult("820122"));
        assertTrue(peselValidator.isValidAdult("012122"));
    }

    @Test
    public void testNotValidAdult() {
        assertFalse(peselValidator.isValidAdult("102122"));
    }

    @Test
    public void testNotValidDate() {
        assertFalse(peselValidator.isValidAdult("822122"));
        assertFalse(peselValidator.isValidAdult("820132"));
    }

}