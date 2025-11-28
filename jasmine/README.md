Jasmine Example Project
=====================

#### Project Structure

```
jasmine
└── spec
     ├── helpers
     │ └── jasmine-json-test-reporter.js
     │ 
     └── jasmine_examples
          ├── PlayerSpec.js
          └── SongSpec.js
```

NOTE: The tests in SongSpec.js are set to fail intentionally.

#### To Build the Project

```
npm install
```

#### To Record Tests

Create test session:

```
BUILD_NAME=jasmine_build
launchable record build --name ${BUILD_NAME}
launchable record session --build ${BUILD_NAME} > session.txt
```

Find and write all tests to a file:

```
find spec/jasmine_examples -type f > test_list.txt
```

To run all tests:

```
npx jasmine $(cat test_list.txt)
```

This creates `jasmine-report.json` in the project root.

Then to record the tests:

```
launchable record tests --base $(pwd) jasmine jasmine-report.json
```

To request subset:

```
cat test_list.txt | launchable subset --target 25% jasmine > subset.txt
```

To run subset of tests:

```
npx jasmine $(cat subset.txt)
```

