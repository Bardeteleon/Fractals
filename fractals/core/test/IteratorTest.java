package fractals.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fractals.core.Iterator;
import fractals.core.Complex;

class IteratorTest
{
    @Test
    void iterateMandelbrotWithoutDivergence()
    {
        assertEquals(10.0, Iterator.iterateMandelbrot(new Complex(0.1, 0.1), 10));
        assertEquals(100.0, Iterator.iterateMandelbrot(new Complex(0.1, 0.1), 100));
        assertEquals(1000.0, Iterator.iterateMandelbrot(new Complex(0.1, 0.1), 1000));
    }

    @Test
    void iterateMandelbrotWithDivergence()
    {
        assertEquals(2.0, Iterator.iterateMandelbrot(new Complex(1.0, 0.1), 10));
        assertEquals(2.0, Iterator.iterateMandelbrot(new Complex(1.0, 0.1), 100));
        assertEquals(8.0, Iterator.iterateMandelbrot(new Complex(0.42, 0.42), 10));
        assertEquals(8.0, Iterator.iterateMandelbrot(new Complex(0.42, 0.42), 100));
    }

    @Test
    void iterateJuliaWithoutDivergence()
    {
        assertEquals(10.0, Iterator.iterateJulia(new Complex(0.1, 1.0), new Complex(0.3, 0.3), 10));
        assertEquals(100.0, Iterator.iterateJulia(new Complex(0.1, 1.0), new Complex(0.3, 0.3), 100));
        assertEquals(1000.0, Iterator.iterateJulia(new Complex(0.1, 1.0), new Complex(0.3, 0.3), 1000));
    }

    @Test
    void iterateJuliaWithDivergence()
    {
        assertEquals(5.0, Iterator.iterateJulia(new Complex(0.4, 1.0), new Complex(0.3, 0.3), 10));
        assertEquals(5.0, Iterator.iterateJulia(new Complex(0.4, 1.0), new Complex(0.3, 0.3), 100));
        assertEquals(9.0, Iterator.iterateJulia(new Complex(0.36, 1.0), new Complex(0.3, 0.3), 10));
        assertEquals(9.0, Iterator.iterateJulia(new Complex(0.36, 1.0), new Complex(0.3, 0.3), 100));
    }
}