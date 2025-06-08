package fractals.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Colorizer
{
    public static enum Mode
    {
        BLACK_WHITE, BLACK_WHITE_MODULO, COLOR;
    }

    private Color[] colorCollection = { Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.MAGENTA };
    private Mode mode = Mode.BLACK_WHITE;

    private int iterationRange;
    private int[][] iterationGrid;

    public Colorizer(int[][] iterationGrid, int iterationRange)
    {
        this.iterationGrid = iterationGrid;
        this.iterationRange = iterationRange;
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void setColorCollection(Color[] colorCollection)
    {
        this.colorCollection = colorCollection;
    }

    public BufferedImage applyTo(BufferedImage image)
    {
        for (int i = 0; i < iterationGrid.length; i++)
        {
            for (int j = 0; j < iterationGrid[i].length; j++)
            {
                image.setRGB(i, j, getIterationColor(iterationGrid[i][j]).getRGB());
            }
        }
        return image;
    }

    private Color getIterationColor(int iterationResult)
    {
		switch (mode)
		{
            case BLACK_WHITE:
                if (iterationResult == iterationRange)
                {
                    return Color.BLACK;
                } else
                {
                    return Color.WHITE;
                }
            case BLACK_WHITE_MODULO:
                if (iterationResult < iterationRange)
                {
                    if (iterationResult % 2 != 0)
                    {
                        return Color.BLACK;
                    } else
                        return Color.WHITE;
                } else
                { 
                    return Color.BLACK;
                }
            case COLOR:
                if (iterationResult < iterationRange)
                {
                    for (int i = colorCollection.length - 1; i >= 0; i--)
                    {
                        if (iterationResult > i * (iterationRange / colorCollection.length))
                        {
                            return colorCollection[i];
                        }
                    }
                } else
                {
                    return Color.BLACK;
                }
            default:
                return Color.BLACK;
		}
    }
}