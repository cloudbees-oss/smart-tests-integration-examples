*** Settings ***

Library           Calculator.py

*** Test Cases ***
Check method
  Calculator.result_is  -1

Plus method
  Calculator.Plus  1
  Calculator.Plus  1
  Calculator.result_is  2

Plus method 2
  Calculator.Plus  10
  Calculator.Plus  20
  Calculator.result_is  30