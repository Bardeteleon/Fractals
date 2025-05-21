package fractals.core;

import fractals.core.Complex;

public class Iterator
{
    public static int iterateMandelbrot(Complex parameter, int iterationRange)
    {
        return iterate(new Complex(0, 0), parameter, iterationRange);
    }

    public static int iterateJulia(Complex startValue, Complex parameter, int iterationRange)
    {
        return iterate(startValue, parameter, iterationRange);
    }

    private static int iterate(Complex startValue, Complex parameter, int iterationRange)
    {
        int currentIteration = 0;
        Complex currentValue = startValue.clone();
        for (int i = 1; i <= iterationRange; i++)
        {
            if (currentValue.norm() < 2)
            {
                currentValue.mult(currentValue.clone()).add(parameter);
                currentIteration++;
            } 
            else
            {
                return currentIteration;
            }
        }
        return currentIteration;
    }
}