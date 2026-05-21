namespace rocket_car_dotnet;

public class DivideCalculator
{
    public double Divide(double a, double b)
    {
        if (b == 0)
            throw new ArgumentException("Cannot divide by zero");
        return a / b;
    }
}
