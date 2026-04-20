package com.launchable.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for Calculator class.
 * These tests have NO @Tag annotation, so they WILL RUN with default Maven config.
 * 
 * These represent normal unit tests that customers want to run in their CI pipeline.
 */
@DisplayName("Calculator Unit Tests")
public class CalculatorTest {
    
    private final Calculator calculator = new Calculator();
    
    @Test
    @DisplayName("Addition should work correctly")
    public void testAddition() {
        assertThat(calculator.add(2, 3)).isEqualTo(5);
        assertThat(calculator.add(-1, 1)).isEqualTo(0);
        assertThat(calculator.add(0, 0)).isEqualTo(0);
        System.out.println("✓ CalculatorTest.testAddition() EXECUTED");
    }
    
    @Test
    @DisplayName("Subtraction should work correctly")
    public void testSubtraction() {
        assertThat(calculator.subtract(5, 3)).isEqualTo(2);
        assertThat(calculator.subtract(0, 5)).isEqualTo(-5);
        assertThat(calculator.subtract(10, 10)).isEqualTo(0);
        System.out.println("✓ CalculatorTest.testSubtraction() EXECUTED");
    }
    
    @Test
    @DisplayName("Multiplication should work correctly")
    public void testMultiplication() {
        assertThat(calculator.multiply(2, 3)).isEqualTo(6);
        assertThat(calculator.multiply(-2, 3)).isEqualTo(-6);
        assertThat(calculator.multiply(0, 100)).isEqualTo(0);
        System.out.println("✓ CalculatorTest.testMultiplication() EXECUTED");
    }
    
    @Test
    @DisplayName("Division should work correctly")
    public void testDivision() {
        assertThat(calculator.divide(10, 2)).isEqualTo(5);
        assertThat(calculator.divide(9, 3)).isEqualTo(3);
        assertThat(calculator.divide(-10, 2)).isEqualTo(-5);
        System.out.println("✓ CalculatorTest.testDivision() EXECUTED");
    }
    
    @Test
    @DisplayName("Division by zero should throw exception")
    public void testDivisionByZero() {
        assertThatThrownBy(() -> calculator.divide(10, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cannot divide by zero");
        System.out.println("✓ CalculatorTest.testDivisionByZero() EXECUTED");
    }
    
    @Test
    @DisplayName("Power function should work correctly")
    public void testPower() {
        assertThat(calculator.power(2, 3)).isEqualTo(8.0);
        assertThat(calculator.power(5, 2)).isEqualTo(25.0);
        assertThat(calculator.power(10, 0)).isEqualTo(1.0);
        System.out.println("✓ CalculatorTest.testPower() EXECUTED");
    }
}
