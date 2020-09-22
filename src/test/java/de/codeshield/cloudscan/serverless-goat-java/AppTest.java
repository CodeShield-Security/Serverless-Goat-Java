package de.codeshield.cloudscan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void handleRequest_shouldReturnConstantValue() {
        de.codeshield.cloudscan.App function = new de.codeshield.cloudscan.App();
        Object result = function.handleRequest("echo", null);
        assertEquals("echo", result);
    }
}
