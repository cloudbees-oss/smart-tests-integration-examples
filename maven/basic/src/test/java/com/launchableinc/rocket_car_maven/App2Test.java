package com.launchableinc.rocket_car_maven;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class App2Test {
    @Test
    public void testAppHasGreeting() {
        App testApp = new App();
        assertEquals("Hello rocket-car-maven", testApp.getGreeting());
    }

    @Test
    public void testAppHasGreeting2() {
        App testApp = new App();
        assertNotNull("Hello rocket-car-maven", testApp.getGreeting());
    }
}
