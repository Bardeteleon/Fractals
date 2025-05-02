public class Complex {
  private double real, imag;
  
  public Complex(double realTeil, double imagTeil) {
    setReal(realTeil);
    setImag(imagTeil);
  }
  public Complex add(Complex c){
    this.setReal(this.getReal() + c.getReal());
    this.setImag(this.getImag() + c.getImag());
    return this;
  }
  public Complex sub(Complex c)
  {
    this.setReal(this.getReal() - c.getReal());
    this.setImag(this.getImag() - c.getImag());
    return this;
  }
  public Complex mult(Complex c)
  {
    double r = this.getReal();
    this.setReal(this.getReal()*c.getReal()-this.getImag()*c.getImag());
    this.setImag(r*c.getImag()+this.getImag()*c.getReal());
    return this;
  }
  public double norm()
  {
    return Math.sqrt(this.getReal()*this.getReal()+this.getImag()*this.getImag());
  }
  public Complex clone()
  {
    return new Complex(this.getReal(), this.getImag());
  }
  public String toString()
  {
    if(this.getImag()<0)
    {
      return real +" - "+imag*(-1)+"i";
    }
    else return real +" + "+imag+"i";
  }

  private void setReal(double real) {
    this.real = real;
  }

  public double getReal() {
    return real;
  }

  private void setImag(double imag) {
    this.imag = imag;
  }

  public double getImag() {
    return imag;
  }
}
