namespace rocket_car_dotnet;

public class PowerCalculatorTest
{
    private readonly PowerCalculator _calculator = new();

    [Test]
    public void TestPower()
    {
        Assert.That(_calculator.Power(2, 3), Is.EqualTo(8.0));
        Assert.That(_calculator.Power(5, 2), Is.EqualTo(25.0));
        Assert.That(_calculator.Power(10, 0), Is.EqualTo(1.0));
    }
}
