package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Example JUnit 5 test demonstrating @Nested annotation.
 * This reproduces the customer issue where nested class names contain $ symbols.
 */
public class DeviceTypeTest {

    @Nested
    class EnumConversion {
        @Test
        void testConversionPC() {
            assertEquals("PC", "PC");
        }

        @Test
        void testConversionMobile() {
            assertEquals("MOBILE", "MOBILE");
        }

        @Test
        void testConversionTablet() {
            assertEquals("TABLET", "TABLET");
        }
    }

    @Nested
    class GetByValue {
        @Test
        void testGetByValuePC() {
            assertNotNull("PC");
        }

        @Test
        void testGetByValueMobile() {
            assertNotNull("MOBILE");
        }

        @Test
        void testGetByValueTablet() {
            assertNotNull("TABLET");
        }
    }

    @Nested
    class MultiLevelNesting {
        @Nested
        class Level2 {
            @Nested
            class Level3 {
                @Test
                void testDeepNesting() {
                    assertTrue(true);
                }
            }
        }
    }
}
