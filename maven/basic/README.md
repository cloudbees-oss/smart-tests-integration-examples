# Maven Basic Example

This is a simple Maven project using JUnit 4 to demonstrate basic Launchable integration.

## Project Structure

- Simple Maven project with JUnit 4
- Two test classes: `AppTest.java` and `App2Test.java`
- Basic "Hello World" application

## Running Tests

```sh
# Run all tests
mvn test
```

## Recording Test Results

```sh
# Record build
BUILD_NAME=my-build-$(date +%s)
launchable record build --name ${BUILD_NAME} --source .

# Run tests
mvn test

# Record test results
launchable record tests --build ${BUILD_NAME} maven surefire ./target/surefire-reports
```

## Subsetting Test Runs

```sh
# Set up variables
BUILD_NAME=my-build-$(date +%s)
TARGET="80%"

# Record build
launchable record build --name ${BUILD_NAME} --source .

# Request subset
launchable subset --target ${TARGET} --build ${BUILD_NAME} maven surefire ./target/surefire-reports > subset.txt

# Run only the subset
mvn test -Dtest=$(cat subset.txt | tr '\n' ',')

# Record the subset results
launchable record tests --build ${BUILD_NAME} maven surefire ./target/surefire-reports
```

## Learn More

- [Launchable Documentation](https://www.launchableinc.com/docs/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
