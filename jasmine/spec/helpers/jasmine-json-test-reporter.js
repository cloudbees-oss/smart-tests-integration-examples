var JSONReporter = require('jasmine-json-test-reporter');
jasmine.getEnv().addReporter(new JSONReporter({
    file: 'jasmine-report.json'
}));
