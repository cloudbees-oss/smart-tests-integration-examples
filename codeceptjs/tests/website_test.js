Feature("Basic Tests");

Scenario("Visit homepage", ({ I }) => {
  I.amOnPage("/");
  I.see("Test Page");
});

Scenario("Visit form page", ({ I }) => {
  I.amOnPage("/form");
  I.seeElement("form");
});
