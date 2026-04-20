package com.launchable.demo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

/**
 * Test class demonstrating mixed tagging scenarios.
 * Some tests are excluded, some are not.
 * 
 * This shows that exclusion works at the METHOD level, not just CLASS level.
 */
@DisplayName("Mixed Tags Test Suite")
public class MixedTagsTest {
    
    @Test
    @DisplayName("Regular unit test - will run")
    public void testRegularUnitTest() {
        assertThat(1 + 1).isEqualTo(2);
        System.out.println("✓ MixedTagsTest.testRegularUnitTest() EXECUTED");
    }
    
    @Test
    @Tag("IntegrationTest")
    @DisplayName("Integration test in mixed class - will be excluded")
    public void testIntegrationInMixedClass() {
        assertThat(true).isTrue();
        System.out.println("✗ MixedTagsTest.testIntegrationInMixedClass() - SHOULD BE EXCLUDED");
    }
    
    @Test
    @Tag("SmokeTest")
    @DisplayName("Smoke test in mixed class - will be excluded")
    public void testSmokeInMixedClass() {
        assertThat(true).isTrue();
        System.out.println("✗ MixedTagsTest.testSmokeInMixedClass() - SHOULD BE EXCLUDED");
    }
    
    @Test
    @DisplayName("Another regular test - will run")
    public void testAnotherRegularTest() {
        assertThat("hello").isNotEmpty();
        System.out.println("✓ MixedTagsTest.testAnotherRegularTest() EXECUTED");
    }
    
    @Test
    @Tag("Performance")
    @DisplayName("Performance test - NOT excluded (not in excludedGroups)")
    public void testPerformance() {
        // This tag is not in excludedGroups, so it will run
        assertThat(System.currentTimeMillis()).isPositive();
        System.out.println("✓ MixedTagsTest.testPerformance() EXECUTED (Performance tag not excluded)");
    }
}
