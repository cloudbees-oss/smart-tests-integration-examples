*** Settings ***

Library           ../../Calculator.py

*** Test Cases ***
Check method
  Calculator.result_is  -1

Minus method
  Calculator.Plus  1
  Calculator.Minus  1
  Calculator.result_is  0

Minus method 2
  Calculator.Plus  10
  Calculator.Minus  20
  Calculator.result_is  -10
