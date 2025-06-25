package fractals.user_interface.desktop;

import fractals.core.Complex;
import fractals.core.Configuration;
import fractals.core.Configuration.Builder;
import fractals.core.Fractal;
import fractals.core.Configuration.IterationRangeMode;
import fractals.core.Colorizer;
import fractals.core.Variant;
import fractals.core.Colorizer.Mode;
import fractals.user_interface.desktop.FractalView;
import fractals.user_interface.desktop.WindowView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.awt.Color;
import java.util.Optional;
import java.awt.Graphics;

public class FractalPresenter implements MouseListener, MouseMotionListener
{
    private int xPress, yPress, xOld, yOld, checker;
    private FractalView fractalView;
    private WindowView windowView;
	private Colorizer.Mode mode;
	private BufferedImage buffer;
	private Color[] colorCollection = { Color.blue, Color.cyan, Color.darkGray, Color.magenta };
	private Fractal fractal;
	private Configuration configuration;

    public FractalPresenter(WindowView windowView)
    {
        this.fractalView = windowView.fractalView;
        this.windowView = windowView;
		configuration = new Configuration.Builder()
										 .widthHeight(1000)
										 .iterationRangeManual(40)
										 .iterationRangeMode(IterationRangeMode.MANUAL)
										 .variant(fractals.core.Variant.MANDELBROT)
										 .min(new Complex(-2.0, -2.0))
										 .max(new Complex(2.0, 2.0))
										 .build();
        setWidthHeight(configuration.widthHeight);
		setColorMode(Colorizer.Mode.BLACK_WHITE);
        setInteractive(true);
    }

	public void paintFractals()
	{
		windowView.statusBarView.progressBar.setString(null);
		fractal = new fractals.core.Fractal(configuration);
		fractal.setFinishCallback(() -> { SwingUtilities.invokeLater(() -> {
			colorizeImage();
            paintCoordinateSystem();
            fractalView.setImage(buffer, configuration.widthHeight);
			fractalView.repaint();
			windowView.statusBarView.progressBar.setString("Fertig!");
		}); });
		fractal.setStatusUpdateCallback((status) -> { SwingUtilities.invokeLater(() -> { 
			windowView.statusBarView.progressBar.setValue(status); 
		}); });
		fractal.evaluate();
	}

	private void colorizeImage()
	{
		if (Objects.isNull(fractal))
			return;

		if (Objects.isNull(configuration))
			return;

		Colorizer colorizer = new Colorizer(fractal.getIterationGrid(), configuration.getIterationRange());
		colorizer.setMode(mode);
		colorizer.setColorCollection(colorCollection);
		colorizer.applyTo(buffer);
	}

    private void paintCoordinateSystem()
    {
        if (!windowView.configurationView.complexPlaneCheckBox.isSelected())
        {
            return;
        }

        if (configuration.min.getReal() == -2.0 && 
            configuration.min.getImag() == -2.0 && 
            configuration.max.getReal() == 2.0 && 
            configuration.max.getImag() == 2.0)
        {
            Graphics g = buffer.getGraphics();
            int widthHeight = configuration.widthHeight;
            g.setColor(Color.GRAY);
            g.drawLine(0, widthHeight / 2 - 1, widthHeight, widthHeight / 2 - 1);
            g.drawLine(widthHeight / 2, 0, widthHeight / 2, widthHeight);
            g.drawString("imaginäre Achse", widthHeight / 2 + 5, 10);
            g.drawString("reelle Achse", widthHeight - 80, widthHeight / 2 - 5);
            g.drawLine(widthHeight / 4, widthHeight / 2 - 5, widthHeight / 4, widthHeight / 2 + 5);
            g.drawLine((3 * widthHeight) / 4, widthHeight / 2 - 5, (3 * widthHeight) / 4, widthHeight / 2 + 5);
            g.drawLine(widthHeight / 2 - 5, widthHeight / 4, widthHeight / 2 + 5, widthHeight / 4);
            g.drawLine(widthHeight / 2 - 5, (3 * widthHeight) / 4, widthHeight / 2 + 5, (3 * widthHeight) / 4);
            g.drawString("-1", widthHeight / 4 - 5, widthHeight / 2 - 7);
            g.drawString("1", (3 * widthHeight) / 4 - 3, widthHeight / 2 - 7);
            g.drawString("i", widthHeight / 2 + 7, widthHeight / 4 + 5);
            g.drawString("-i", widthHeight / 2 + 7, (3 * widthHeight) / 4 + 5);
        }
        else
        {
            windowView.configurationView.exceptionTextArea.setText("Die Gaußsche Zahlenebene kann nur im Standardintervall (-2, -2) bis (2, 2) dargestellt werden. Rechtsklick im Fraktal zeichnet das Standardintervall.");
            windowView.configurationView.complexPlaneCheckBox.setSelected(false);
        }
    }

	public void setMandelbrotmengeConfigured()
	{
		configuration = new Configuration.Builder()
										 .basedOn(configuration)
										 .variant(fractals.core.Variant.MANDELBROT)
										 .build();
	}

	public void setJuliamengeConfigured(Complex parameter)
	{
		configuration = new Configuration.Builder()
										 .basedOn(configuration)
										 .variant(fractals.core.Variant.JULIA)
										 .variant_parameter(Optional.of(parameter))
										 .build();
	}
    
	public double getMinRe()
	{
		return configuration.min.getReal();
	}

	public double getMaxRe()
	{
		return configuration.max.getReal();
	}

	public double getMinIm()
	{
		return configuration.min.getImag();
	}

	public double getMaxIm()
	{
		return configuration.max.getImag();
	}

	public int getIterationRange()
	{
		if (Objects.nonNull(configuration))
			return configuration.getIterationRange();
		else
			return configuration.iterationRangeManual;
	}

	public int getWidthHeight()
	{
		return configuration.widthHeight;
	}

	public Color[] getColorCollection()
	{
		return colorCollection;
	}

	public Complex getParamC()
	{
		return configuration.variant_parameter.orElse(null);
	}

	public void setIterationRange(int range)
	{
		if (range <= 0)
		{
			configuration = new Configuration.Builder()
                                             .basedOn(configuration)
                                             .iterationRangeMode(IterationRangeMode.AUTOMATIC)
                                             .build();
		}
		else
		{
			configuration = new Configuration.Builder()
                                             .basedOn(configuration)
			                                 .iterationRangeMode(IterationRangeMode.MANUAL)
										     .iterationRangeManual(range)
										     .build();
		}
	}

	public void setColorMode(Colorizer.Mode mode)
	{
		this.mode = mode;
	}

	public void setMinCoordinate(Complex min)
	{
		configuration = new Builder().basedOn(configuration).min(min).build();
	}

	public void setMaxCoordinate(Complex max)
	{
		configuration = new Builder().basedOn(configuration).max(max).build();
	}

	public void setWidthHeight(int wH)
	{
		if (wH > 0)
		{
            buffer = new BufferedImage(wH, wH, BufferedImage.TYPE_INT_ARGB);
			configuration = new Builder().basedOn(configuration).widthHeight(wH).build();
		}
	}

	public void setColorCollection(Color[] c)
	{
		colorCollection = c.clone();
	}

	public void stop()
	{
		if (Objects.nonNull(fractal))
			fractal.stop();
	}

    @Override
    public void mousePressed(MouseEvent me)
    {
        // temporäre Daten für Zoom-Funktion, Zoom-Quadrat
        if (SwingUtilities.isLeftMouseButton(me))
        {
            xPress = me.getX();
            yPress = me.getY();
            xOld = me.getX();
            yOld = me.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
        // Zoom-Funktion
        if (SwingUtilities.isLeftMouseButton(me))
        {
            if ((me.getX() > xPress) && (yPress < me.getY()))
            {
                int yMin, xMax;
                if (me.getX() - xPress > me.getY() - yPress)
                {
                    yMin = yPress + (me.getX() - xPress);
                    xMax = me.getX();
                } 
                else
                {
                    yMin = me.getY();
                    xMax = xPress + (me.getY() - yPress);
                }
                setMinCoordinate(configuration.getCoordinate(xPress, yMin));
                setMaxCoordinate(configuration.getCoordinate(xMax, yPress));
                if (windowView.configurationView.iterationCheckBox.isSelected())
                {
                    setIterationRange(-1);
                }
                setInteractive(false);
                windowView.configurationView.repaintButton.setText("Abbrechen!");
                paintFractals();
            }
            windowView.setAktTextFieldText(this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        if (configuration.variant == Variant.MANDELBROT)
        {
            if (SwingUtilities.isLeftMouseButton(me))
            {
                windowView.configurationView.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                setMinCoordinate(new Complex(-2.0, -2.0));
                setMaxCoordinate(new Complex(2.0, 2.0));
                setIterationRange(40);
                setJuliamengeConfigured(configuration.getCoordinate(me.getX(), me.getY()));
                paintFractals();
                windowView.configurationView.juliaCheckBox.setSelected(true);
            }
            if (SwingUtilities.isRightMouseButton(me))
            {
                windowView.configurationView.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                setMinCoordinate(new Complex(-2.0, -2.0));
                setMaxCoordinate(new Complex(2.0, 2.0));
                setIterationRange(40);
                paintFractals();
                windowView.configurationView.juliaCheckBox.setSelected(false);
            }
        } else
        {
            if (SwingUtilities.isLeftMouseButton(me))
            {
                windowView.configurationView.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                setMinCoordinate(new Complex(-2.0, -2.0));
                setMaxCoordinate(new Complex(2.0, 2.0));
                setIterationRange(40);
                setMandelbrotmengeConfigured();
                paintFractals();
                windowView.configurationView.juliaCheckBox.setSelected(false);
            }
            if (SwingUtilities.isRightMouseButton(me))
            {
                windowView.configurationView.repaintButton.setText("Abbrechen!");
                setInteractive(false);
                setMinCoordinate(new Complex(-2.0, -2.0));
                setMaxCoordinate(new Complex(2.0, 2.0));
                setIterationRange(40);
                paintFractals();
                windowView.configurationView.juliaCheckBox.setSelected(true);
            }
        }
        windowView.setAktTextFieldText(this);
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        // aktuelle Koordinaten des Cursers in den jeweiligen TextFeldern
        // anzeigen lassen
        if (!windowView.configurationView.juliaCheckBox.isSelected())
        {
            final Complex coordinate = configuration.getCoordinate(me.getX(), me.getY());
            windowView.statusBarView.reelField1.setText("" + coordinate.getReal());
            windowView.statusBarView.imagField1.setText("" + coordinate.getImag());
        }
    }

    @Override
    public void mouseDragged(MouseEvent me)
    {
        if (SwingUtilities.isLeftMouseButton(me))
        {
            // Zoom-Quadrat bei gedrückter Maus
            // Anzeige nur im 4.Quadranten (wenn die Koordinate, die
            // gedrückt wird, den Ursprung darstellt)
            if ((me.getX() > xPress) && (yPress < me.getY()))
            {
                // Einteilung der Zeichenoperation in 2Fälle(um immer ein
                // Quadrat zu zeichnen und kein Rechteck)
                // 1Fall: x-Länge ist größer als die y-Länge des gezogenen
                // Rechtecks(mit der Maus wird mit hoher Wahrscheinlichkeit
                // ein Rechteck gezogen)->x-Länge=QuadratSeitenLänge
                if ((me.getX() - xPress) > (me.getY() - yPress))
                {
                    // Falls ein Wechsel zwischen den Fällen stattfindet
                    // muss die Zeichenoperation des 1Falls nochmal
                    // ausgeführt werden um das Quadrat verschwinden zu
                    // lassen, ansonsten gibt es Überlappungen
                    if (checker == 2)
                    {
                        fractalView.paintZoomRec(xPress, yPress, yOld - yPress);
                        fractalView.paintZoomRec(xPress, yPress, xOld - xPress);
                    }
                    fractalView.paintZoomRec(xPress, yPress, xOld - xPress);
                    xOld = me.getX();
                    fractalView.paintZoomRec(xPress, yPress, xOld - xPress);
                    checker = 1;
                }
                // 2Fall: y-Länge > x-Länge des gezogenen
                // Rechtecks->y-Länge=QuadratSeitenLänge
                else
                {
                    if ((me.getX() - xPress) < (me.getY() - yPress))
                    {
                        // Falls ein Wechsel zwischen den Fällen stattfindet
                        // muss die Zeichenoperation des 1Falls nochmal
                        // ausgeführt werden um das Quadrat verschwinden zu
                        // lassen, ansonsten gibt es Überlappungen
                        if (checker == 1)
                        {
                            fractalView.paintZoomRec(xPress, yPress, xOld - xPress);
                            fractalView.paintZoomRec(xPress, yPress, yOld - yPress);
                        }
                        fractalView.paintZoomRec(xPress, yPress, yOld - yPress);
                        yOld = me.getY();
                        fractalView.paintZoomRec(xPress, yPress, yOld - yPress);
                        checker = 2;
                    }
                }

            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
    }

    public void setInteractive(boolean interactive)
    {
        if (interactive)
        {
            if (fractalView.getMouseListeners().length < 1)
            {
                fractalView.addMouseListener(this);
            }
            if (fractalView.getMouseMotionListeners().length < 1)
            {
                fractalView.addMouseMotionListener(this);
            }
        } else
        {
            fractalView.removeMouseListener(this);
            fractalView.removeMouseMotionListener(this);
        }
    }
}