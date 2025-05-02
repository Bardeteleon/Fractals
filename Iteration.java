package mandelbrot;

public class Iteration
{
	public static void main(String[] args)
	{
		int counter;
		for (double d = -2.0; d <= 2.0; d += 0.2)
			for (double i = -2.0; i <= 2.0; i += 0.2)
			{
				counter = iteration(new Complex(0, 0), new Complex(d, i), 30);
				if (counter == 30)
					System.out.println("Complex: " + d + " + " + i + "i\t" + counter);
			}
	}
	private static int iteration(Complex startvalue, Complex parameterC, int iterationRange)
	{
		int counter = 0;
		for (int i = 1; i <= iterationRange; i++)
		{
			if (startvalue.norm() < 2)
			{
				startvalue.mult(startvalue.clone()).add(parameterC);
				counter++;
			} else
				return counter;
		}
		return counter;
	}
}
