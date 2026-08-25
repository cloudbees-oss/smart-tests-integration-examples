package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates @Nested tests WITHOUT @DisplayName.
 *
 * When Surefire runs these tests, the XML report will have:
 *   classname="com.example.DeviceTypeTest$EnumConversion"  (binary $ notation)
 *   name="mobileIsPhone()"                                 (actual method name)
 *
 * The Launchable CLI's junit5_nested_class_path_builder strips "$EnumConversion",
 * so the recorded path becomes:
 *   class=com.example.DeviceTypeTest | testcase=mobileIsPhone()
 *
 * The subset path (from file scanning) is:
 *   class=com.example.DeviceTypeTest
 *
 * These match at the class level — no mismatch in this scenario.
 * Compare with DeviceTypeDisplayNameTest to see the broken @DisplayName scenario.
 */
class DeviceTypeTest {

    @Nested
    @DisplayName("Enum Conversion Tests")
    class EnumConversion {

        @Test
        @DisplayName("Mobile is Phone")
        void mobileIsPhone() {
            assertEquals(DeviceType.PHONE, DeviceType.PHONE);
        }

        @Test
        void tabletIsTablet() {
            assertEquals(DeviceType.TABLET, DeviceType.TABLET);
        }

        @Test
        void laptopIsLaptop() {
            assertEquals(DeviceType.LAPTOP, DeviceType.LAPTOP);
        }
    }

    @Nested
    class GetByValue {

        @Test
        void getPhoneByValue() {
            assertEquals(DeviceType.PHONE, DeviceType.getByValue(1));
        }

        @Test
        void getTabletByValue() {
            assertEquals(DeviceType.TABLET, DeviceType.getByValue(2));
        }

        @Test
        void getLaptopByValue() {
            assertEquals(DeviceType.LAPTOP, DeviceType.getByValue(3));
        }

        @Test
        void returnsNullForUnknownValue() {
            assertNull(DeviceType.getByValue(99));
        }
    }

    @Nested
    class MultiLevelNesting {

        @Nested
        class Level2 {

            @Nested
            class Level3 {

                @Test
                void deeplyNestedTestStillWorks() {
                    assertTrue(true);
                }
            }

            @Test
            void level2TestWorks() {
                assertTrue(true);
            }
        }

        @Test
        void level1TestWorks() {
            assertTrue(true);
        }
    }
}
