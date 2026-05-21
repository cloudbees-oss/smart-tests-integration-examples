namespace rocket_car_dotnet;

public class SubtractCalculatorTest
{
    private readonly SubtractCalculator _calculator = new();

    [Test]
    public void TestSubtract()
    {
        Assert.That(_calculator.Subtract(5, 3), Is.EqualTo(2));
        Assert.That(_calculator.Subtract(0, 5), Is.EqualTo(-5));
        Assert.That(_calculator.Subtract(10, 10), Is.EqualTo(0));
    }
}
