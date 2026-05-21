# dotnet x nunit

## Recording test results

```sh
# Record build
$ BUILD_NAME=test
$ launchable record build --name ${BUILD_NAME} --source ..
Launchable recorded 1 commit from repository ~/launchable/samples/examples
Launchable recorded build test to workspace launchableinc/example with commits from 1 repository:

| Name   | Path   | HEAD Commit                              |
|--------|--------|------------------------------------------|
| ../    | ../    | 5fa3dc1aa3dc34589765898ec285e810b0bea422 |

# Run test
$ dotnet test --logger:"nunit;LogFilePath=test-result.xml" --logger:"junit;LogFilePath=junit-result.xml"
...

Failed!  - Failed:     3, Passed:     1, Skipped:     0, Total:     4, Duration: 24 ms - rocket-car-dotnet.dll (net8.0)


$ launchable record tests --build ${BUILD_NAME} dotnet ./test-result.xml
Launchable recorded tests for build test (test session 50) to workspace launchableinc/example from 1 files:

|   Files found |   Tests found |   Tests passed |   Tests failed |   Total duration (min) |
|---------------|---------------|----------------|----------------|------------------------|
|             1 |             4 |              1 |              3 |                   0.00 |

Visit https://app.launchableinc.com/organizations/launchableinc/workspaces/example/test-sessions/50 to view uploaded test results (or run `launchable inspect tests --test-session-id 50`)

```

## Subsetting your test runs

```sh
$ BUILD_NAME=test
$ TARGET="75%"

# Request subset. dotnet profile requires using Zero Input Subsetting
$ launchable subset --build ${BUILD_NAME} --target ${TARGET} --get-tests-from-previous-sessions --output-exclusion-rules dotnet > subset.txt
Your model is currently in training
Launchable created subset 51 for build test (test session 50) in workspace launchableinc/example

|           |   Candidates |   Estimated duration (%) |   Estimated duration (min) |
|-----------|--------------|--------------------------|----------------------------|
| Subset    |            3 |                    75.00 |                       0.00 |
| Remainder |            1 |                    25.00 |                       0.00 |
|           |              |                          |                            |
| Total     |            4 |                   100.00 |                       0.00 |

Run `launchable inspect subset --subset-id 51` to view full subset details

$ cat subset.txt
FullyQualifiedName!=rocket_car_dotnet.ExampleTest.TestDiv

$ dotnet test --filter $(cat subset.txt)
...

Failed!  - Failed:     2, Passed:     1, Skipped:     0, Total:     3, Duration: 25 ms - rocket-car-dotnet.dll (net8.0)
```

## Split subset for parallel test runs

```sh
$ BUILD_NAME=split-subset
$ TARGET="50%"

$ launchable subset --build ${BUILD_NAME} --target ${TARGET} --get-tests-from-previous-sessions --split dotnet > subset.txt
subset/53
Your model is currently in training
Launchable created subset 53 for build test (test session 52) in workspace launchable/example

|           |   Candidates |   Estimated duration (%) |   Estimated duration (min) |
|-----------|--------------|--------------------------|----------------------------|
| Subset    |            2 |                    50.00 |                       0.00 |
| Remainder |            2 |                    50.00 |                       0.00 |
|           |              |                          |                            |
| Total     |            4 |                   100.00 |                       0.00 |

Run `launchable inspect subset --subset-id 53` to view full subset details

# Request for split test bin of 1/2
$ launchable split-subset --subset-id subset/53 --bin 1/2 --output-exclusion-rules dotnet
FullyQualifiedName!=rocket_car_dotnet.ExampleTest.TestMul

# Request for split test bin of 2/2
$ launchable split-subset --subset-id subset/53 --bin 2/2 --output-exclusion-rules dotnet
FullyQualifiedName!=rocket_car_dotnet.ExampleTest.TestDiv
```
