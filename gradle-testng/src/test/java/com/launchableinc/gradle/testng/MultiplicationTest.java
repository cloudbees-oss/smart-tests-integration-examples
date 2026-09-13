package com.launchableinc.gradle.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MultiplicationTest {
    @Test
    public void multipliesTwoNumbers() {
        Assert.assertEquals(Calculator.multiply(6, 7), 42);
    }
}
