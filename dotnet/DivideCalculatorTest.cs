namespace rocket_car_dotnet;

public class DivideCalculatorTest
{
    private readonly DivideCalculator _calculator = new();

    [Test]
    public void TestDivide()
    {
        Assert.That(_calculator.Divide(10, 2), Is.EqualTo(5.0));
        Assert.That(_calculator.Divide(9, 3), Is.EqualTo(3.0));
        Assert.That(_calculator.Divide(-10, 2), Is.EqualTo(-5.0));
    }

    [Test]
    public void TestDivideByZero()
    {
        Assert.Throws<ArgumentException>(() => _calculator.Divide(10, 0));
    }

    // Intentionally failing test to verify failure reporting in test reports
    [Test]
    public void TestDivideFailing()
    {
        Assert.That(_calculator.Divide(10, 2), Is.EqualTo(99.0));
    }
}
