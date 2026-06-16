Karma
======

**Steps taken to create this project:**

```bash
npm init -y
npm install --save-dev karma karma-jasmine jasmine-core karma-chrome-launcher karma-json-reporter karma-reports-with-file-paths
```
The instructions are based on:

https://karma-runner.github.io/6.4/intro/installation.html

**Generate `karma.conf.js`:**
```bash
npx karma init

# Answer the prompts:
# - Framework: jasmine
# - Require.js: no
# - Browser: ChromeHeadless
# - Test files: 
# - Files to exclude:
# - Watch files: no
```

**Add following to `karma.conf.js` while keeping the current settings:**
```
module.exports = function (config) {
  config.set({
    files: process.env.KARMA_FILES ? JSON.parse(process.env.KARMA_FILES) : [],
    ...
    preprocessors: {
      '**/*.spec.js': ['reports-with-file-paths']
    },
    ...
    plugins: [
      ...
      require('karma-json-reporter'),
      require('karma-reports-with-file-paths')
    ],
    jsonReporter: {
      outputFile: require('path').join(__dirname, 'test-results.json'),
      stdout: false
    },
    reporters: [..., 'json']
  });
};
```

**Create a test file:**
```bash
mkdir test

cat > test/foo.spec.js << 'EOF'
describe('Foo', function() {
  it('should pass', function() {
    expect(true).toBe(true);
  });

  it('should add numbers', function() {
    expect(1 + 1).toBe(2);
  });
});
EOF
```

**Record session:**
```bash
git init && git add . && git commit -m "Initial commit"
launchable record build --name ${BUILD_NAME}
launchable record session --build ${BUILD_NAME} > session.txt
```

**Run all tests:**
```bash
find test -name "*.spec.ts" -o -name "*.spec.js" > test_list.txt
export KARMA_FILES=$(cat test_list.txt | jq -R -s -c 'split("\n")[:-1]')
npx karma start --single-run
```

**Record tests:**
```bash
launchable record tests karma test-results.json
```

**Request subset:**
```bash
cat test_list.txt | launchable subset --target 25% karma  > subset.txt
```

**Run subset of tests:**
```bash
export KARMA_FILES=$(cat subset.txt | jq -R -s -c 'split("\n")[:-1]')
npx karma start --single-run
```