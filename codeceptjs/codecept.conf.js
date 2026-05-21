/** @type {CodeceptJS.MainConfig} */
exports.config = {
  tests: "./tests/*_test.js",
  output: "./output",
  helpers: {
    Playwright: {
      url: "http://localhost:3000",
      show: false,
      browser: "chromium",
    },
  },
  bootstrap: async () => {
    const { startServer } = require('./server');
    await startServer();
  },
  teardown: async () => {
    const { stopServer } = require('./server');
    await stopServer();
  },
};
