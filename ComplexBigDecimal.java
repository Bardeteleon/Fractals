package mandelbrot;

import java.math.BigDecimal;

public class ComplexBigDecimal
{
	private BigDecimal real, imag;

	public ComplexBigDecimal(BigDecimal realTeil, BigDecimal imagTeil)
	{
		setReal(realTeil);
		setImag(imagTeil);
	}
	public ComplexBigDecimal(double realTeil, double imagTeil)
	{
		setReal("" + realTeil);
		setImag("" + imagTeil);
	}

	public ComplexBigDecimal add(ComplexBigDecimal c)
	{
		this.setReal(this.getReal().add(c.getReal()).toString());
		this.setImag(this.getImag().add(c.getImag()).toString());
		return this;
	}
	public ComplexBigDecimal sub(ComplexBigDecimal c)
	{
		this.setReal(this.getReal().subtract(c.getReal()).toString());
		this.setImag(this.getImag().subtract(c.getImag()).toString());
		return this;
	}
	public ComplexBigDecimal mult(ComplexBigDecimal c)
	{
		BigDecimal r = this.getReal();
		this.setReal(this.getReal().multiply(c.getReal()).subtract(this.getImag().multiply(c.getImag())).toString());
		this.setImag(r.multiply(c.getImag()).add(this.getImag().multiply(c.getReal())).toString());
		return this;
	}
	public double norm()
	{
		return Math.sqrt(this.getReal().multiply(this.getReal()).add(this.getImag().multiply(this.getImag())).doubleValue());
	}
	@Override
	public ComplexBigDecimal clone()
	{
		return new ComplexBigDecimal(this.getReal(), this.getImag());
	}
	@Override
	public String toString()
	{
		if (this.getImag().compareTo(new BigDecimal(0)) < 0)
		{
			return real + " - " + imag.multiply(new BigDecimal(-1)) + "i";
		} else
			return real + " + " + imag + "i";
	}

	private void setReal(String real)
	{
		this.real = new BigDecimal(real);
	}
	private void setReal(BigDecimal real)
	{
		this.real = real;
	}

	public BigDecimal getReal()
	{
		return real;
	}

	private void setImag(String imag)
	{
		this.imag = new BigDecimal(imag);
	}
	private void setImag(BigDecimal imag)
	{
		this.imag = imag;
	}

	public BigDecimal getImag()
	{
		return imag;
	}
}
