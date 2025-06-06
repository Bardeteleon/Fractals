package mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import fractals.core.Variant;

public class Fractal extends JPanel implements Serializable
{
	private double maxIm, minIm, maxRe, minRe, maxOld, minOld;
	private int widthHeight;
	private int iterationRange;
	private int mode;
	private BufferedImage buffer;
	private List<BufferedImage> history; // TODO
	private Complex paramC;
	private Color[] colorCollection =
	{ Color.blue, Color.cyan, Color.darkGray, Color.magenta };
	public static final int MODE_BLACK_WHITE = 1;
	public static final int MODE_BLACK_WHITE_MODULO = 2;
	public static final int MODE_COLOR = 3;
	public boolean mandelbrotmengePainted = true;
	private AtomicInteger status = new AtomicInteger(0);
	private Thread waitThread;
	private fractals.core.Fractal fractal;

	public Fractal()
	{
		setKoordinates(-2.0, 2.0, -2.0, 2.0);
		setWidthHeight(700);
		setIterationRange(40);
		setColorMode(MODE_BLACK_WHITE);
	}

	public Fractal(double minRe, double maxRe, double minIm, double maxIm, int iterationRange, int widthHeight, int mode)
	{

		setKoordinates(minRe, maxRe, minIm, maxIm);
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
		fractal = new fractals.core.Fractal(
			new Configuration.Builder()
								.widthHeight(widthHeight)
								.iterationRange(iterationRange)
								.variant(mandelbrotmengePainted ? fractals.core.Variant.MANDELBROT : fractals.core.Variant.JULIA)
								.variant_parameter(mandelbrotmengePainted ? Optional.empty() : Optional.of(param))
								.min(new Complex(minRe, minIm))
								.max(new Complex(maxRe, maxIm))
								.build());
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

		final int[][] iterationGrid = fractal.getIterationGrid();
		for (int i = 0; i < iterationGrid.length; i++)
		{
			for (int j = 0; j < iterationGrid[i].length; j++)
			{
				buffer.setRGB(i, j, getIterationColor(iterationGrid[i][j]).getRGB());
			}
		}
	}

	private Color getIterationColor(int iterationCounter)
	{
		switch (mode)
		{
		case 1:
			if (iterationCounter == iterationRange)
			{
				return Color.BLACK;
			} else
				return Color.WHITE;
		case 2:
			if (iterationCounter < iterationRange)
			{
				if (iterationCounter % 2 != 0)
				{
					return Color.BLACK;
				} else
					return Color.WHITE;
			} else
				return Color.BLACK;
		case 3:
			if (iterationCounter < iterationRange)
			{
				for (int i = colorCollection.length - 1; i >= 0; i--)
				{
					if (iterationCounter > i * (iterationRange / colorCollection.length))
					{
						return colorCollection[i];
					}
				}
			} else
				return Color.WHITE;
		default:
			return Color.black;
		}
	}

	public double getKoordinatesRe(int pixel)
	{
		return minRe + pixel * ((maxRe - minRe) / (widthHeight - 1));
	}

	public double getKoordinatesIm(int pixel)
	{
		return maxIm + pixel * ((minIm - maxIm) / (widthHeight - 1));
	}

	private void updateStatus(int prozzAnzahl, int relativeStatus, final JProgressBar progress)
	{
		if (progress != null)
		{
			int intervall = widthHeight / (100 / prozzAnzahl);
			if (intervall != 0 && relativeStatus % intervall == 0)
			{
				final int current_status = status.incrementAndGet();
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						progress.setValue(current_status);
					}
				});
			}
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(this.widthHeight, this.widthHeight);
	}

	public double getMinRe()
	{
		return minRe;
	}

	public double getMaxRe()
	{
		return maxRe;
	}

	public double getMinIm()
	{
		return minIm;
	}

	public double getMaxIm()
	{
		return maxIm;
	}

	public int getIterationRange()
	{
		return iterationRange;
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
		// automatische Iterationtiefen-Anpassung beim Zoom
		if (range <= 0)
		{
			double quotient = (Math.abs(maxOld - minOld) / (Math.abs(maxIm - minIm)));
			if (quotient > 1)
			{
				iterationRange += (int) quotient * (35 / 4);
			}
		} else
			iterationRange = range;
	}

	public void setColorMode(int mode)
	{
		if (mode == 1 || mode == 2 || mode == 3)
			this.mode = mode;
	}

	public void setKoordinates(double minRe, double maxRe, double minIm, double maxIm)
	{
		this.maxOld = this.maxIm;
		this.minOld = this.minIm;
		this.maxIm = maxIm;
		this.minIm = minIm;
		this.maxRe = maxRe;
		this.minRe = minRe;
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
						if (minRe == -2.0 && minIm == -2.0 && maxRe == 2.0 && maxIm == 2.0)
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
		if (destination.getName().toLowerCase().endsWith(".ser"))
		{
			FileOutputStream out = null;
			ObjectOutputStream objout = null;
			try
			{
				out = new FileOutputStream(destination);
				objout = new ObjectOutputStream(out);
				objout.writeObject(this);
			} catch (IOException e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					objout.close();
					out.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
