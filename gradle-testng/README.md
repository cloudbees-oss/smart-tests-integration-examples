# Gradle + TestNG + Launchable

This minimal Gradle and TestNG project demonstrates predictive test selection
with Launchable. It has two test classes so you can see that a subset runs only
the selected class.

## Prerequisites

* JDK 17 or later
* Launchable CLI authenticated for the target workspace

Run the whole suite:

```sh
./gradlew test
```

## Launchable pipeline

The normal CI flow is:

```sh
# Verify credentials, then record the source revision for this build.
launchable verify
launchable record build --name "$BUILD_NAME" --source .

# Ask for selected TestNG classes. --bare is required: the adapter expects one
# fully-qualified class name per line, not Gradle --tests arguments.
launchable subset \
  --target 80% \
  --build "$BUILD_NAME" \
  gradle \
  --bare \
  src/test/java \
  > subset.txt

# launchable-testng is on the test runtime classpath and reads this file.
LAUNCHABLE_SUBSET_FILE_PATH="$PWD/subset.txt" ./gradlew test

# Gradle produces JUnit XML reports even though the framework is TestNG.
launchable record tests \
  --build "$BUILD_NAME" \
  gradle \
  build/test-results/test
```

Always run the final `record tests` command, including after a failing test
run. In CI, retain the Gradle exit status and record the reports before exiting
with that status.

```sh
set +e
LAUNCHABLE_SUBSET_FILE_PATH="$PWD/subset.txt" ./gradlew test
test_exit=$?

launchable record tests \
  --build "$BUILD_NAME" \
  gradle \
  build/test-results/test

exit "$test_exit"
```

## Try selection locally

`demo-subset.txt` simulates the output produced by `launchable subset gradle
--bare`:

```sh
LAUNCHABLE_SUBSET_FILE_PATH="$PWD/demo-subset.txt" ./gradlew clean test
```

Only `AdditionTest` runs. Remove `LAUNCHABLE_SUBSET_FILE_PATH` to run all
tests again.

## Important details

* `launchable-testng` 1.3.0 reads `LAUNCHABLE_SUBSET_FILE_PATH`.
* `launchable-testng` is a TestNG listener. The `useTestNG` configuration in
  `build.gradle` registers it explicitly because Gradle does not discover
  TestNG listeners from `META-INF/services`. Do not add
  `--tests "$(cat subset.txt)"` to the Gradle command for this integration.
* `LAUNCHABLE_SUBSET_FILE_PATH` must be an absolute path or a path relative to
  the process that invokes Gradle.
* The file contains fully-qualified test class names, one per line.
