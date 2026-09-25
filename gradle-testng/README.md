# Gradle + TestNG + CloudBees Smart Tests

This minimal Gradle and TestNG project demonstrates predictive test selection
with CloudBees Smart Tests. It has two test classes so you can see that a
subset runs only the selected class.

## Prerequisites

* JDK 17 or later
* smart-tests CLI authenticated for the target workspace

Run the whole suite:

```sh
./gradlew test
```

## Smart Tests pipeline

The normal CI flow is:

```sh
# Verify credentials, then record the source revision for this build.
smart-tests verify
smart-tests record build --build "$BUILD_NAME"

# Start a new test session for this build. The session ID is required by
# both `subset` and `record tests` below.
smart-tests record session --test-suite gradle-testng --build "$BUILD_NAME" > session.txt

# Ask for selected TestNG classes. --bare is required: the adapter expects one
# fully-qualified class name per line, not Gradle --tests arguments.
smart-tests subset \
  --session @session.txt \
  --target 80% \
  gradle \
  --bare \
  src/test/java \
  > subset.txt

# launchable-testng is on the test runtime classpath and reads this file.
SMART_TESTS_SUBSET_FILE_PATH="$PWD/subset.txt" ./gradlew test

# Gradle produces JUnit XML reports even though the framework is TestNG.
smart-tests record tests \
  --session @session.txt \
  gradle \
  build/test-results/test
```

Always run the final `record tests` command, including after a failing test
run. In CI, retain the Gradle exit status and record the reports before exiting
with that status.

```sh
set +e
SMART_TESTS_SUBSET_FILE_PATH="$PWD/subset.txt" ./gradlew test
test_exit=$?

smart-tests record tests \
  --session @session.txt \
  gradle \
  build/test-results/test

exit "$test_exit"
```

## Try selection locally

`demo-subset.txt` simulates the output produced by `smart-tests subset gradle
--bare`:

```sh
SMART_TESTS_SUBSET_FILE_PATH="$PWD/demo-subset.txt" ./gradlew clean test
```

Only `AdditionTest` runs. Remove `SMART_TESTS_SUBSET_FILE_PATH` to run all
tests again.

## Important details

* `launchable-testng` 1.4.2+ reads `SMART_TESTS_SUBSET_FILE_PATH`. Older
  releases (through 1.3.0) only understood the legacy
  `LAUNCHABLE_SUBSET_FILE_PATH`, which 1.4.2+ still accepts as a deprecated
  fallback — but setting only the new name against an older plugin version
  silently runs the full suite with no warning, so keep the dependency pinned
  to 1.4.2 or later.
* `launchable-testng` is a TestNG listener. The `useTestNG` configuration in
  `build.gradle` registers it explicitly because Gradle does not discover
  TestNG listeners from `META-INF/services`. Do not add
  `--tests "$(cat subset.txt)"` to the Gradle command for this integration.
* `SMART_TESTS_SUBSET_FILE_PATH` must be an absolute path or a path relative to
  the process that invokes Gradle.
* The file contains fully-qualified test class names, one per line.
