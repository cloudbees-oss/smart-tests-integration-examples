namespace rocket_car_dotnet;

public class MultiplyCalculatorTest
{
    private readonly MultiplyCalculator _calculator = new();

    [Test]
    public void TestMultiply()
    {
        Assert.That(_calculator.Multiply(2, 3), Is.EqualTo(6));
        Assert.That(_calculator.Multiply(-2, 3), Is.EqualTo(-6));
        Assert.That(_calculator.Multiply(0, 100), Is.EqualTo(0));
    }
}
