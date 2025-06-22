package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

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
	private Complex min, max;
	private int widthHeight;
	private int iterationRangeManual;
	private Configuration.IterationRangeMode iterationRangeMode;
	private Colorizer.Mode mode;
	private BufferedImage buffer;
	private Complex paramC;
	private Color[] colorCollection = { Color.blue, Color.cyan, Color.darkGray, Color.magenta };
	public boolean mandelbrotmengePainted = true;
	private AtomicInteger status = new AtomicInteger(0);
	private Thread waitThread;
	private fractals.core.Fractal fractal;
	private fractals.core.Configuration configuration;

	public FractalView()
	{
		setMinCoordinate(new Complex(-2.0, -2.0));
		setMaxCoordinate(new Complex(2.0, 2.0));
		setWidthHeight(1000);
		setIterationRange(40);
		setColorMode(Colorizer.Mode.BLACK_WHITE);
	}

	public FractalView(double minRe, double maxRe, double minIm, double maxIm, int iterationRange, int widthHeight, Colorizer.Mode mode)
	{

		setMinCoordinate(new Complex(minRe, minIm));
		setMaxCoordinate(new Complex(maxRe, maxIm));
		setWidthHeight(widthHeight);
		setIterationRange(iterationRange);
		setColorMode(mode);
	}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		super.setBackground(Color.WHITE);
		if (buffer == null)
		{
			buffer = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
			paintFractals(null, null);
		}
		// Graphics2D g = (Graphics2D) graphics;
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g.setRenderingHint(RenderingHints.KEY_RENDERING,
		// RenderingHints.VALUE_RENDER_QUALITY);
		// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.drawImage(buffer, 0, 0, widthHeight, widthHeight, this);
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
		if (param == null)
			mandelbrotmengePainted = true;
		else
		{
			mandelbrotmengePainted = false;
			this.paramC = param;
		}
		configuration = new Configuration.Builder()
								.widthHeight(widthHeight)
								.iterationRangeManual(iterationRangeManual)
								.iterationRangeMode(iterationRangeMode)
								.variant(mandelbrotmengePainted ? fractals.core.Variant.MANDELBROT : fractals.core.Variant.JULIA)
								.variant_parameter(mandelbrotmengePainted ? Optional.empty() : Optional.of(param))
								.min(min)
								.max(max)
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
		return new Dimension(this.widthHeight, this.widthHeight);
	}

	public double getMinRe()
	{
		return min.getReal();
	}

	public double getMaxRe()
	{
		return max.getReal();
	}

	public double getMinIm()
	{
		return min.getImag();
	}

	public double getMaxIm()
	{
		return max.getImag();
	}

	public int getIterationRange()
	{
		if (Objects.nonNull(configuration))
			return configuration.getIterationRange();
		else
			return iterationRangeManual;
	}

	public int getWidthHeight()
	{
		return widthHeight;
	}

	public Color[] getColorCollection()
	{
		return colorCollection;
	}

	public Complex getParamC()
	{
		return paramC;
	}

	public void setIterationRange(int range)
	{
		if (range <= 0)
		{
			iterationRangeMode = IterationRangeMode.AUTOMATIC;
		}
		else
		{
			iterationRangeManual = range;
			iterationRangeMode = IterationRangeMode.MANUAL;
		}
	}

	public void setColorMode(Colorizer.Mode mode)
	{
		this.mode = mode;
	}

	public void setMinCoordinate(Complex min)
	{
		this.min = min;
	}

	public void setMaxCoordinate(Complex max)
	{
		this.max = max;
	}

	public void setWidthHeight(int wH)
	{
		if (wH > 0)
		{
			if (buffer != null)
				buffer = new BufferedImage(wH, wH, BufferedImage.TYPE_INT_ARGB);
			widthHeight = wH;
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
		g.setClip(0, 0, widthHeight, widthHeight);
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
						if (min.getReal() == -2.0 && min.getImag() == -2.0 && max.getReal() == 2.0 && max.getImag() == 2.0)
						{
							Graphics g = buffer.getGraphics();
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

	public void store(File destination)
	{
		if (destination.getName().toLowerCase().endsWith(".png"))
		{
			BufferedImage img = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = img.createGraphics();
			paintComponent(graphics);
			try
			{
				ImageIO.write(img, "png", destination);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
