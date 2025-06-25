package fractals.user_interface.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class FractalView extends JPanel
{
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
		}
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
}
