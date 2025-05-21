package fractals.core;

public final class Complex
{
	private final double real;
	private final double imag;

	public Complex(double real, double imag)
	{
		this.real = real;
		this.imag = imag;
	}

	public Complex add(Complex c)
	{
		return new Complex(this.real + c.getReal(), this.imag + c.getImag());
	}

	public Complex sub(Complex c)
	{
		return new Complex(this.real - c.getReal(), this.imag - c.getImag());
	}

	public Complex mult(Complex c)
	{
		double r = this.real * c.getReal() - this.imag * c.getImag();
		double i = this.real * c.getImag() + this.imag * c.getReal();
		return new Complex(r, i);
	}

	public double norm()
	{
		return Math.sqrt(this.real * this.real + this.imag * this.imag);
	}

	@Override
	public Complex clone()
	{
		return new Complex(this.real, this.imag);
	}

	@Override
	public String toString()
	{
		if (this.imag < 0)
		{
			return real + " - " + (-imag) + "i";
		}
		else
		{
			return real + " + " + imag + "i";
		}
	}

	public double getReal()
	{
		return real;
	}

	public double getImag()
	{
		return imag;
	}
}
