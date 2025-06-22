package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;

import fractals.core.Iterator;
import fractals.core.Complex;
import fractals.core.Configuration;
import fractals.core.Configuration.Builder;
import fractals.core.Configuration.IterationRangeMode;
import fractals.core.Variant;
import fractals.core.Colorizer;
import fractals.core.Colorizer.Mode;

public class FractalView extends JPanel
{
	private Colorizer.Mode mode;
	private BufferedImage buffer;
	private Color[] colorCollection = { Color.blue, Color.cyan, Color.darkGray, Color.magenta };
	private AtomicInteger status = new AtomicInteger(0);
	private Thread waitThread;
	private fractals.core.Fractal fractal;
	private fractals.core.Configuration configuration;

	public FractalView()
	{
		configuration = new Configuration.Builder()
										 .widthHeight(1000)
										 .iterationRangeManual(40)
										 .iterationRangeMode(IterationRangeMode.MANUAL)
										 .variant(fractals.core.Variant.MANDELBROT)
										 .min(new Complex(-2.0, -2.0))
										 .max(new Complex(2.0, 2.0))
										 .build();
		setColorMode(Colorizer.Mode.BLACK_WHITE);
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		super.setBackground(Color.WHITE);
		if (buffer == null)
		{
			buffer = new BufferedImage(configuration.widthHeight, configuration.widthHeight, BufferedImage.TYPE_INT_ARGB);
			paintFractals(null, null);
		}
		// Graphics2D g = (Graphics2D) graphics;
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.drawImage(buffer, 0, 0, configuration.widthHeight, configuration.widthHeight, this);
	}

	/**
	 * Die Methode generiert die Mandelbrotmenge, wenn für paramC null übergeben
	 * wird. Andernfalls generiert die Methode die Juliamenge mit dem
	 * übergebenem Parameter paramC. Der Fortschritt der Berechnungen kann
	 * mithilfe von progress veranschaulicht werden.
	 * 
	 * @param paramC
	 * @param progress
	 */
	public void paintFractals(final Complex param, final JProgressBar progress)
	{
		if (progress != null)
		{
			progress.setString(null);
		}
		boolean mandelbrotmengePainted = Objects.isNull(param);
		configuration = new Configuration.Builder()
								.basedOn(configuration)
								.variant(mandelbrotmengePainted ? fractals.core.Variant.MANDELBROT : fractals.core.Variant.JULIA)
								.variant_parameter(mandelbrotmengePainted ? Optional.empty() : Optional.of(param))
								.build();
		fractal = new fractals.core.Fractal(configuration);
		fractal.setFinishCallback(() -> { SwingUtilities.invokeLater(() -> {
			colorizeImage();
			repaint();
			if (Objects.nonNull(progress))
				progress.setString("Fertig!");
		}); });
		fractal.setStatusUpdateCallback((status) -> { SwingUtilities.invokeLater(() -> { 
			if (Objects.nonNull(progress)) 
				progress.setValue(status); 
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

	public BufferedImage getImage()
	{
		return buffer;
	}

	public boolean isMandelbrotmengeConfigured()
	{
		return configuration.variant == Variant.MANDELBROT;
	}

	public Complex getCoordinate(int x, int y)
	{
		if (Objects.nonNull(configuration))
			return configuration.getCoordinate(x, y);
		else
			return null;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(configuration.widthHeight, configuration.widthHeight);
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
			configuration = new Builder().basedOn(configuration)
									     .iterationRangeMode(IterationRangeMode.AUTOMATIC)
										 .build();
		}
		else
		{
			configuration = new Builder().basedOn(configuration)
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
			if (buffer != null)
				buffer = new BufferedImage(wH, wH, BufferedImage.TYPE_INT_ARGB);
			configuration = new Builder().basedOn(configuration).widthHeight(wH).build();
			setSize(wH, wH);
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

	public void paintZoomRec(int x, int y, int width)
	{
		Graphics g = getGraphics();
		g.setClip(0, 0, configuration.widthHeight, configuration.widthHeight);
		g.setXORMode(Color.RED);
		g.drawRect(x, y, width, width);
	}

	public void paintKoordinateSystem()
	{
		Thread wait = new Thread()
		{
			@Override
			public void run()
			{
				if (waitThread != null && waitThread.isAlive())
				{
					try
					{
						waitThread.join();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
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
						repaint();
					}
				});

			};
		};
		wait.setDaemon(true);
		wait.start();
	}
}
