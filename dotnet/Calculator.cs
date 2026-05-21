namespace rocket_car_dotnet;

public class Calculator
{
    private readonly AddCalculator _add = new();
    private readonly SubtractCalculator _subtract = new();
    private readonly MultiplyCalculator _multiply = new();
    private readonly DivideCalculator _divide = new();
    private readonly PowerCalculator _power = new();

    public int Add(int a, int b) => _add.Add(a, b);
    public int Subtract(int a, int b) => _subtract.Subtract(a, b);
    public int Multiply(int a, int b) => _multiply.Multiply(a, b);
    public double Divide(double a, double b) => _divide.Divide(a, b);
    public double Power(double base_, double exponent) => _power.Power(base_, exponent);
}
