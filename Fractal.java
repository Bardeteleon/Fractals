import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FractalD extends JPanel {
  private double maxIm,minIm,maxRe,minRe,maxOld,minOld;
  private int widthHeight,iterationRange,mode;
  private Image buffer;
  private Complex paramC;
  private Color[] colorCollection={Color.blue,Color.cyan,Color.darkGray,Color.magenta};
  public static final int MODE_BLACK_WHITE = 1;
  public static final int MODE_BLACK_WHITE_MODULO = 2;
  public static final int MODE_COLOR = 3;
  public boolean mandelbrotmengePainted;

  public Fractal()
  {
    setKoordinates(-2.0, 2.0, -2.0, 2.0);
    setWidthHeight(600);
    setIterationRange(40);
    setColorMode(MODE_BLACK_WHITE);
  }
  public Fractal(double minRe, double maxRe, double minIm, double maxIm, int iterationRange, int widthHeight, int mode) {
    
    setKoordinates(minRe, maxRe, minIm, maxIm);
    setWidthHeight(widthHeight);
    setIterationRange(iterationRange);
    setColorMode(mode);
  }

  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    super.setBackground(Color.WHITE);
    if(buffer==null)
    {
      buffer = createImage(widthHeight, widthHeight);
      paintMandelbrotmenge();     
    }
    graphics.drawImage(buffer, 0, 0, widthHeight, widthHeight, this);
    
  }
  /*
   Zeichnet die Mandelbrotmenge auf ein internes Image.
   Zum Anzeigen auf dem JPanel repaint() aufrufen. 
   */
  public void paintMandelbrotmenge()
  {
    Graphics g = buffer.getGraphics();
    for (int i = 1; i <= this.widthHeight; i++) {
      for (int j = 1; j <=widthHeight; j++) {
        g.setColor(getIterationColor(iteration(new Complex(0,0), new Complex(getKoordinatesRe(i), getKoordinatesIm(j)))));
        g.drawLine(i, j, i, j); 
      }
    }
    mandelbrotmengePainted = true;
  }
  /*
  Zeichnet die Julia Menge mit dem Parameter c auf ein internes Image.
  Zum Anzeigen auf dem JPanel repaint() aufrufen.
   */
  public void paintJuliaMenge(Complex c)
  {
    paramC = c;
    Graphics g = buffer.getGraphics();
    for(int i = 1; i<=this.widthHeight; i++)  {
      for(int j = 1; j <= this.widthHeight; j++){
          g.setColor(getIterationColor(iteration(new Complex(getKoordinatesRe(i), getKoordinatesIm(j)) , c)));
          g.drawLine(i, j, i, j); 
      }
    }
    mandelbrotmengePainted = false;
  }
  private int iteration(Complex startvalue, Complex parameterC) {
    int counter=0;
    for (int i = 1; i <= iterationRange; i++) {
      if(startvalue.norm()<2)
      {
        startvalue.mult(startvalue.clone()).add(parameterC);
        counter++;
      }else return counter;
    }   
    return counter;
  }
  /*
  *Liefert je nach Color Modus und Iterationsfolge die Farbe für die Pixel
   */
  public Color getIterationColor(int iterationCounter)
  {
    switch(mode)
    {
       case 1:  if(iterationCounter==iterationRange)
            {
              return Color.BLACK;
            }
            else return Color.WHITE;
       case 2:  if(iterationCounter<iterationRange)
            {
              if(iterationCounter % 2 != 0)
              {
                return Color.BLACK;
              }
              else return Color.WHITE;
            }else return Color.BLACK;
       case 3:  if(iterationCounter<iterationRange)
            {
              for(int i = colorCollection.length-1; i>=0;i--)
              {
                if(iterationCounter>i*(iterationRange/colorCollection.length))
                {
                  return colorCollection[i];
                }
              }
            }else return Color.BLACK;
       default : return null;
    } 
  }
  public Dimension getPreferredSize()
  {
    return new Dimension(this.widthHeight,this.widthHeight);
  }
  /*
  Liefert den Abzissen Wert des KOS am pixel.
   */
  public double getKoordinatesRe(int pixel)
  {
    return this.minRe+pixel*((maxRe-minRe)/(widthHeight-1));
  }
  /*
  Liefert den Ordinaten Wert des KOS am pixel.
   */
  public double getKoordinatesIm(int pixel)
  {
    return this.maxIm+pixel*((minIm-maxIm)/(widthHeight-1));
  }
  /*
  Liefert Informationen über die aktuelle Fractal Instanz
   */
  public Object[] getValues()
  {
    Object[] values = {minRe,maxRe,minIm,maxIm,iterationRange,widthHeight, colorCollection};
    return values;
  }
  /*
  Setzt die maximale Iterationszahl auf range.
  Bei Werten kleiner als 0 wird die max. Iterationszahl automatisch gesetzt.
   */
  public void setIterationRange(int range)
  {
    //automatische Iterationtiefen-Anpassung beim Zoom
    if(range<=0)
    { 
      double quotient = (Math.abs(maxOld-minOld)/(Math.abs(maxIm-minIm)));
      if(quotient>1)
      {
        iterationRange += (int) quotient * (35/4);
      }
    }else iterationRange = range;
  }
  /*
  Setzt den Color Modus, s.h. Fractal Konstanten.
   */
  public void setColorMode(int mode)
  {
    if(mode==1||mode==2||mode==3)
    this.mode = mode;
  }
  
  /*
  Setzt die min. und max. Koordinaten auf den Achsen.
   */
  public void setKoordinates(double minRe, double maxRe, double minIm, double maxIm)
  {
    this.maxOld = this.maxIm;
    this.minOld = this.minIm;
    this.maxIm = maxIm;
    this.minIm = minIm;
    this.maxRe = maxRe;
    this.minRe = minRe; 
  }
  /*
  Setzt die Größe des internen Images auf wH.
   */
  public void setWidthHeight(int wH)
  {
    if(wH>0)
    {
      buffer = createImage(wH, wH);
      widthHeight = wH;
      setSize(wH, wH);
    }
  }
  /*
  Setzt die Fraben für die divergierenden Iterationsfolgen.
   */
  public void setColorCollection(Color[] c)
  {
    colorCollection = c.clone();
  }
  /*
  Zeichnet das Fraktal neu.
   */
  public void repaintFractal()
  {
    if(mandelbrotmengePainted==true)
    {
      paintMandelbrotmenge();
    }else paintJuliaMenge(paramC);
    repaint();
  }
  public void paintZoomRec(int x, int y, int width)
  {
    Graphics g = getGraphics();
    g.setClip(0, 0, widthHeight, widthHeight);
    g.setXORMode(Color.RED);
    g.drawRect(x, y, width, width);
  }
  /*
  Zeichnet bei dem Standard KOS das KOS ein.
   */
  public void paintKoordinateSystem()
  {
    if(minRe == -2.0 && minIm == -2.0 && maxRe == 2.0 && maxIm == 2.0)
    {
      Graphics g = buffer.getGraphics();
      g.setColor(Color.GRAY);
      g.drawLine(0, widthHeight/2, widthHeight, widthHeight/2);
      g.drawLine(widthHeight/2, 0 , widthHeight/2, widthHeight);
      g.drawString("imaginäre Achse", widthHeight/2+5, 10);
      g.drawString("reelle Achse", widthHeight-80, widthHeight/2-5);
      g.drawLine(widthHeight/4,widthHeight/2-5, widthHeight/4, widthHeight/2+5);
      g.drawLine((3*widthHeight)/4,widthHeight/2-5, (3*widthHeight)/4, widthHeight/2+5);
      g.drawLine(widthHeight/2-5, widthHeight/4, widthHeight/2+5, widthHeight/4);
      g.drawLine(widthHeight/2-5, (3*widthHeight)/4, widthHeight/2+5, (3*widthHeight)/4);
      g.drawString("-1",widthHeight/4-5 , widthHeight/2-7);
      g.drawString("1", (3*widthHeight)/4-3, widthHeight/2-7);
      g.drawString("i", widthHeight/2+7, widthHeight/4+5);
      g.drawString("-i", widthHeight/2+7, (3*widthHeight)/4+5);
    }
  }
  /*
  Speichert das aktuell gezeichnete Fraktal in destination.
   */
  public void store(File destination)
  {
    BufferedImage img = new BufferedImage(widthHeight,widthHeight,BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = img.createGraphics();
    paintComponent(graphics);
    try 
    {
      ImageIO.write(img, "png", destination);
    } 
    catch (IOException e){}
  }
}
