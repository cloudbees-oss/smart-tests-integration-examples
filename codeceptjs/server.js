const express = require("express");
const app = express();
const PORT = 3000;

app.get("/", (req, res) => {
  res.send(`<h1>Test Page</h1><p>Welcome</p>`);
});

app.get("/form", (req, res) => {
  res.send(`<form><input name="name"><button>Submit</button></form>`);
});

let server;

module.exports = {
  startServer: () =>
    new Promise((resolve) => {
      server = app.listen(PORT, resolve);
    }),
  stopServer: () =>
    new Promise((resolve) => {
      server ? server.close(resolve) : resolve();
    }),
};
