import fractals.core.Complex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ComplexTest
{
    @Test
    void addTest()
    {
        Complex c1 = new Complex(1.0, 1.0);
        Complex c2 = new Complex(2.0, 2.0);
        Complex c3 = c1.add(c2);
        assertEquals(3.0, c3.getReal());
        assertEquals(3.0, c3.getImag());
    }

    @Test
    void subTest()
    {
        Complex c1 = new Complex(3.0, 3.0);
        Complex c2 = new Complex(2.0, 2.0);
        Complex c3 = c1.sub(c2);
        assertEquals(1.0, c3.getReal());
        assertEquals(1.0, c3.getImag());
    }

    @Test
    void multTest()
    {
        Complex c1 = new Complex(4.0, 1.0);
        Complex c2 = new Complex(3.0, 2.0);
        Complex c3 = c1.mult(c2);
        assertEquals(10.0, c3.getReal());
        assertEquals(11.0, c3.getImag());
    }

    @Test
    void normTest()
    {
        Complex c = new Complex(2.0, 2.0);
        assertEquals(Math.sqrt(8), c.norm());
    }

    @Test
    void cloneTest()
    {
        Complex c1 = new Complex(3.0, 3.0);
        Complex c2 = c1.clone();
        assertEquals(c1.getReal(), c2.getReal());
        assertEquals(c1.getImag(), c2.getImag());
        assertFalse(c1 == c2);
    }

    @Test
    void toStringTest()
    {
        Complex c = new Complex(2.0, 2.0);
        assertEquals("2.0 + 2.0i", c.toString());
    }

}