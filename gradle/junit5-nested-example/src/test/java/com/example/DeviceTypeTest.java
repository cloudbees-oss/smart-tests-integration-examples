package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Reproduces the @Nested + @DisplayName test path mismatch with Gradle.
 *
 * Unlike Maven Surefire 3.x (which always uses actual method names in XML),
 * Gradle's JUnit Platform test executor uses TestDescriptor.getDisplayName()
 * when writing the XML `name` attribute. This means the XML report contains
 * the @DisplayName string, NOT the actual Java method name.
 *
 * After running `./gradlew test`, check build/test-results/test/ and you will see:
 *
 *   <testcase classname="com.example.DeviceTypeTest$EnumConversion"
 *             name="Mobile is Phone"   ← @DisplayName value, NOT "mobileIsPhone()"
 *             .../>
 *
 * The Launchable CLI reads this `name` verbatim as the `testcase` path segment:
 *   class=com.example.DeviceTypeTest | testcase=Mobile is Phone
 *
 * If a test previously had no @DisplayName (or a different one), the CLI recorded:
 *   class=com.example.DeviceTypeTest | testcase=mobileIsPhone()
 *
 * These are different paths → server treats them as different tests → history lost
 * → subset analysis page shows "unrecognized test" warning.
 */
@DisplayName("Device Type Tests")
class DeviceTypeTest {

    @Nested
    @DisplayName("Enum Conversion")
    class EnumConversion {

        @Test
        @DisplayName("Mobile is Phone")
        void mobileIsPhone() {
            assertEquals(DeviceType.PHONE, DeviceType.PHONE);
        }

        @Test
        @DisplayName("Tablet is Tablet")
        void tabletIsTablet() {
            assertEquals(DeviceType.TABLET, DeviceType.TABLET);
        }

        @Test
        // No @DisplayName here — method name will appear in XML as "laptopIsLaptop()"
        void laptopIsLaptop() {
            assertEquals(DeviceType.LAPTOP, DeviceType.LAPTOP);
        }
    }

    @Nested
    @DisplayName("Get By Value")
    class GetByValue {

        @Test
        @DisplayName("Returns PHONE for value 1")
        void getPhoneByValue() {
            assertEquals(DeviceType.PHONE, DeviceType.getByValue(1));
        }

        @Test
        void returnsNullForUnknownValue() {
            assertNull(DeviceType.getByValue(99));
        }
    }
}
