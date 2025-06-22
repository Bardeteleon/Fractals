package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class FractalView extends JPanel
{
	private Thread waitThread;
	private BufferedImage buffer;
	private int widthHeight;

	public FractalView() {}

	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		super.setBackground(Color.WHITE);
		if (buffer == null)
		{
			buffer = new BufferedImage(widthHeight, widthHeight, BufferedImage.TYPE_INT_ARGB);
			// paintFractals();
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

	public BufferedImage getImage()
	{
		return buffer;
	}

	public void setImage(BufferedImage image, int widthHeight)
	{
		this.buffer = image;
		this.widthHeight = widthHeight;
		setSize(widthHeight, widthHeight);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(widthHeight, widthHeight);
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
						// if (configuration.min.getReal() == -2.0 && 
						//     configuration.min.getImag() == -2.0 && 
						// 	configuration.max.getReal() == 2.0 && 
						// 	configuration.max.getImag() == 2.0)
						// {
						// 	Graphics g = buffer.getGraphics();
						// 	g.setColor(Color.GRAY);
						// 	g.drawLine(0, widthHeight / 2 - 1, widthHeight, widthHeight / 2 - 1);
						// 	g.drawLine(widthHeight / 2, 0, widthHeight / 2, widthHeight);
						// 	g.drawString("imaginäre Achse", widthHeight / 2 + 5, 10);
						// 	g.drawString("reelle Achse", widthHeight - 80, widthHeight / 2 - 5);
						// 	g.drawLine(widthHeight / 4, widthHeight / 2 - 5, widthHeight / 4, widthHeight / 2 + 5);
						// 	g.drawLine((3 * widthHeight) / 4, widthHeight / 2 - 5, (3 * widthHeight) / 4, widthHeight / 2 + 5);
						// 	g.drawLine(widthHeight / 2 - 5, widthHeight / 4, widthHeight / 2 + 5, widthHeight / 4);
						// 	g.drawLine(widthHeight / 2 - 5, (3 * widthHeight) / 4, widthHeight / 2 + 5, (3 * widthHeight) / 4);
						// 	g.drawString("-1", widthHeight / 4 - 5, widthHeight / 2 - 7);
						// 	g.drawString("1", (3 * widthHeight) / 4 - 3, widthHeight / 2 - 7);
						// 	g.drawString("i", widthHeight / 2 + 7, widthHeight / 4 + 5);
						// 	g.drawString("-i", widthHeight / 2 + 7, (3 * widthHeight) / 4 + 5);
						// }
						repaint();
					}
				});

			};
		};
		wait.setDaemon(true);
		wait.start();
	}
}
