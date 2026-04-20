# Maven Test Exclusion Example with Launchable

This example demonstrates how to use the `--scan-dryrun-results` feature with Maven to discover **all** tests, including those excluded by JUnit 5 tags, for intelligent subset selection with Launchable.

## Problem Statement

When using Maven's `<excludedGroups>` configuration to filter tests, traditional test discovery only finds tests that will actually run. This creates a problem:

- **Normal discovery**: Discovers only 10 tests (tests that will run)
- **Excluded tests**: 11 tests are filtered out and invisible to Launchable
- **Result**: Launchable cannot optimize subset selection for the full test suite

The `--scan-dryrun-results` feature solves this by discovering **all 21 tests** (including excluded ones), enabling Launchable to:
- Understand your complete test inventory
- Make better subset optimization decisions
- Track historical data for all tests, even when exclusions change

## Test Organization

This project contains **21 tests across 5 test classes**:

### Tests That Run (10 tests)
- `CalculatorTest` - 6 untagged tests (basic calculator operations)
- `MixedTagsTest` - 3 untagged tests (will run)
- `ExcludeTestApplicationTests` - 1 Spring Boot context test

### Tests That Are Excluded (11 tests)
- `UserServiceIntegrationTest` - 4 tests with `@Tag("IntegrationTest")` (excluded)
- `ApplicationSmokeTest` - 5 tests with `@Tag("SmokeTest")` (excluded)
- `MixedTagsTest` - 2 tests with exclusion tags (excluded)

### Maven Configuration

The `pom.xml` configures test exclusions:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <excludedGroups>IntegrationTest,SmokeTest</excludedGroups>
    </configuration>
</plugin>
```

## Running Tests

### Run all non-excluded tests (10 tests)
```sh
mvn test
```

### Run with dry-run mode to discover all tests (21 tests)
```sh
mvn test -DdryRun=true
```

This generates Surefire reports in `target/surefire-reports/` for all tests, including excluded ones.

### Run all tests (ignoring exclusions)
```sh
mvn test -Dtest.excludedGroups=
```

## Launchable Integration

### Normal Discovery Mode (Without `--scan-dryrun-results`)

When using standard test discovery, Launchable only sees tests that actually run:

```sh
# Record build
BUILD_NAME=my-build-$(date +%s)
launchable record build --name ${BUILD_NAME} --source .

# Run tests normally
mvn test

# Record test results - only discovers 9 tests
launchable record tests --build ${BUILD_NAME} maven surefire ./target/surefire-reports
```

**Result**: Launchable discovers **10 tests** (excluded tests are invisible)

### With `--scan-dryrun-results` (Recommended)

Using dry-run mode, Launchable discovers all tests including excluded ones:

```sh
# Record build
BUILD_NAME=my-build-$(date +%s)
launchable record build --name ${BUILD_NAME} --source .

# Generate dry-run reports (discovers all 20 tests)
mvn test -DdryRun=true

# Scan dry-run results to discover all tests
launchable subset maven \
  --scan-dryrun-results \
  --build ${BUILD_NAME} \
  --target 10% \
  ./target/surefire-reports

# Run the subset
mvn test -Dtest=$(cat subset.txt | tr '\n' ',')

# Record actual test results
launchable record tests --build ${BUILD_NAME} maven surefire ./target/surefire-reports
```

**Result**: Launchable discovers **all 21 tests** and makes better optimization decisions

## Demo Scripts

This example includes three demo scripts to explore test discovery:

### 1. `discover-tests-maven.sh`
Demonstrates Maven-based test discovery showing the difference between normal and dry-run modes.

```sh
./discover-tests-maven.sh
```

**Output**:
- Extracts Maven exclusion configuration
- Discovers all 21 tests (without exclusions)
- Discovers 10 tests that will run (with exclusions)
- Shows which 11 tests are excluded and why

### 2. `compare-tests-demo.sh`
Quick comparison showing how test discovery differs with and without exclusions.

```sh
./compare-tests-demo.sh
```

**Output**:
- Creates `all-tests.txt` (21 tests)
- Creates `included-tests.txt` (10 tests)
- Creates `excluded-tests.txt` (11 tests)
- Side-by-side comparison

### 3. `ci-pipeline-example.sh`
Complete CI/CD pipeline example integrating with Launchable CLI.

```sh
# Set environment variables
export LAUNCHABLE_TOKEN='v1:your-org/your-workspace:your-token'
export BUILD_ID='my-build-123'
export SUBSET_TARGET='60%'

./ci-pipeline-example.sh
```

**Pipeline steps**:
1. Compile tests
2. Extract Maven exclusion configuration
3. Discover tests and identify exclusions
4. Record build with Launchable
5. Request test subset
6. Run subset
7. Record results

## Manual Testing with Your Own Project

To manually test the `--scan-dryrun-results` feature with your own Maven project:

```sh
# 1. Navigate to your Maven project
cd /path/to/your/maven-project

# 2. Run Maven dry-run to generate reports
mvn test -DdryRun=true

# 3. Verify reports were created
ls -la target/surefire-reports/TEST-*.xml

# 4. Set up environment (if not already set)
export LAUNCHABLE_TOKEN='v1:your-org/your-workspace:your-actual-token'

# 5. Record build
launchable record build --name 'test-build-name' --source .

# 6. Create a session and scan dry-run results
launchable subset maven \
  --scan-dryrun-results \
  --build 'test-build-name' \
  --target 10% \
  ./target/surefire-reports

# 7. Compare with normal discovery (optional)
mvn test
launchable record tests --build 'test-build-name' maven surefire ./target/surefire-reports
# Check: How many tests were discovered? Only non-excluded tests!
```

## Key Differences

| Discovery Mode | Tests Discovered | Use Case |
|---|---|---|
| **Normal** (`mvn test`) | 10 tests (only non-excluded) | Standard test execution |
| **Dry-run** (`mvn test -DdryRun=true`) | 21 tests (all tests) | Discovery for Launchable subset optimization |
| **No exclusions** (`mvn test -Dtest.excludedGroups=`) | 21 tests (all tests run) | Running complete test suite |

## Test Files

### Main Source Files
- `Calculator.java` - Simple calculator with basic operations
- `UserService.java` - User service for integration tests
- `TestDiscoveryUtil.java` - Utility for test discovery
- `ExcludeTestApplication.java` - Spring Boot application entry point

### Test Files
- `CalculatorTest.java` - 6 tests (untagged, always run)
- `MixedTagsTest.java` - 5 tests (3 untagged + 2 with exclusion tags)
- `UserServiceIntegrationTest.java` - 4 tests (`@Tag("IntegrationTest")`)
- `ApplicationSmokeTest.java` - 5 tests (`@Tag("SmokeTest")`)
- `ExcludeTestApplicationTests.java` - Spring Boot context test

## Understanding Surefire Reports

When running `mvn test -DdryRun=true`, Maven generates two types of reports:

### XML Reports (`TEST-*.xml`)
```xml
<testsuite name="com.launchable.demo.CalculatorTest" tests="6" ...>
  <testcase name="testAddition" classname="com.launchable.demo.CalculatorTest" time="0.001"/>
  ...
</testsuite>
```

### Text Reports (`*.txt`)
```
Test set: com.launchable.demo.CalculatorTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

Both formats include metadata for **all discovered tests**, making them ideal for the `--scan-dryrun-results` feature.

## Troubleshooting

### "No POM in this directory" Error
```sh
$ mvn test -DdryRun=true
[ERROR] The goal you specified requires a project to execute but there is no POM in this directory
```

**Solution**: Make sure you're running the command from the project root directory containing `pom.xml`.

### Launchable Not Discovering All Tests
If Launchable still shows only 10 tests instead of 21:

1. Verify dry-run reports were generated:
   ```sh
   ls -la target/surefire-reports/TEST-*.xml | wc -l
   # Should show reports for all test classes
   ```

2. Make sure you're using `--scan-dryrun-results` flag:
   ```sh
   launchable subset maven --scan-dryrun-results ...
   ```

3. Check the Surefire plugin version in `pom.xml` (should be 3.0.0-M5 or later for best results).

## Learn More

- [Launchable Documentation](https://www.launchableinc.com/docs/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [JUnit 5 Tagging and Filtering](https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering)

## Summary

This example demonstrates:
- ✅ How Maven's `<excludedGroups>` filters tests at runtime
- ✅ Why standard discovery misses excluded tests (9 discovered vs 20 total)
- ✅ How `--scan-dryrun-results` discovers all tests for better optimization
- ✅ Complete Launchable integration workflow with test exclusions
- ✅ Scripts to explore and compare test discovery methods
