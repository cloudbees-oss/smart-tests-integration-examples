# JUnit 5 @Nested Test Example

This example demonstrates the customer issue with JUnit 5 `@Nested` annotation and how it affects test path generation.

## The Issue

When using JUnit 5's `@Nested` annotation, Maven Surefire generates XML reports with `$` in classnames:

```xml
<testcase classname="com.example.DeviceTypeTest$EnumConversion" name="testConversionPC" />
<testcase classname="com.example.DeviceTypeTest$GetByValue" name="testGetByValuePC" />
```

**Problem:**
- **CLI** strips `$` suffix → `class=com.example.DeviceTypeTest`
- **Jenkins Plugin (before fix)** kept `$` → `class=com.example.DeviceTypeTest$EnumConversion`
- Result: Test path mismatch → "Too many unrecognized tests" alerts

## Run the Example

### 1. Run Tests
```bash
cd /Users/psakthivel/IdeaProjects/smart-tests-integration-examples/maven/junit5-nested-example
mvn clean test
```

### 2. Check Generated XML
```bash
cat target/surefire-reports/TEST-com.example.DeviceTypeTest.xml
```

You'll see:
- `<testsuite tests="0">` - Parent class has no tests
- `classname="DeviceTypeTest$EnumConversion"` - Nested class with `$`
- `classname="DeviceTypeTest$GetByValue"` - Another nested class
- `classname="DeviceTypeTest$MultiLevelNesting$Level2$Level3"` - Multi-level nesting

### 3. Observe the Pattern
```bash
grep 'classname=' target/surefire-reports/TEST-*.xml
```

**Key Observations:**
- Every `@Nested` class adds a `$ClassName` suffix
- Multi-level nesting: `Outer$Mid$Inner`
- This is JUnit 5's standard behavior

## The Fix

**Mothership Jenkins Plugin** now strips `$` suffixes in [JenkinsJunitResult.java:186-192](../../mothership/src/main/java/com/launchableinc/mercury/intake/model/JenkinsJunitResult.java#L186-L192):

```java
public void setClassName(String className) {
    String normalizedClassName = className.split("\\$")[0];
    this.className = escapeForTestPath(normalizedClassName);
}
```

This matches the CLI behavior from `smart_tests/utils/java.py:39-62`.

## Test Coverage

The example includes:
- ✅ Single-level nesting: `EnumConversion`, `GetByValue`
- ✅ Multi-level nesting: `MultiLevelNesting$Level2$Level3`
- ✅ Multiple tests per nested class

## Expected Behavior After Fix

**Before Fix:**
```
Jenkins Plugin: class=com.example.DeviceTypeTest$EnumConversion#testcase=testConversionPC
CLI:            class=com.example.DeviceTypeTest#testcase=testConversionPC
Match? ❌ NO
```

**After Fix:**
```
Jenkins Plugin: class=com.example.DeviceTypeTest#testcase=testConversionPC
CLI:            class=com.example.DeviceTypeTest#testcase=testConversionPC
Match? ✅ YES
```

## Related Files

- **POC Demo:** [../nested-test-poc/](../nested-test-poc/)
- **Fix Implementation:** [mothership/JenkinsJunitResult.java](../../mothership/src/main/java/com/launchableinc/mercury/intake/model/JenkinsJunitResult.java)
- **Test Coverage:** [mothership/JenkinsJunitResultTest.java](../../mothership/src/test/java/com/launchableinc/mercury/intake/model/JenkinsJunitResultTest.java)
- **Manual Test Guide:** [mothership/manual-test-nested-classes.md](../../mothership/manual-test-nested-classes.md)

## Next Steps

1. Run `mvn clean test` to generate reports
2. Examine the Surefire XML to see `$` in classnames
3. Compare with POC simulators to understand the fix
4. Review mothership implementation for production fix
