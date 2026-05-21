namespace rocket_car_dotnet;

public class AddCalculatorTest
{
    private readonly AddCalculator _calculator = new();

    [Test]
    public void TestAdd()
    {
        Assert.That(_calculator.Add(2, 3), Is.EqualTo(5));
        Assert.That(_calculator.Add(-1, 1), Is.EqualTo(0));
        Assert.That(_calculator.Add(0, 0), Is.EqualTo(0));
    }

    // Intentionally failing test to verify failure reporting in test reports
    [Test]
    public void TestAddFailing()
    {
        Assert.That(_calculator.Add(2, 3), Is.EqualTo(999));
    }
}
