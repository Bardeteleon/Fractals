package fractals.core.test; 

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import fractals.core.Fractal;
import fractals.core.Configuration;
import fractals.core.Configuration.Builder;

class FractalTest 
{
    private static final int GRID_SIZE = 3;
    private Configuration configuration;
    private Fractal fractal;
    private int[][] expectedGrid;

    @BeforeEach
    void setUp()
    {
        configuration = new Configuration.Builder().widthHeight(GRID_SIZE).build();
        fractal = new Fractal(configuration);
        expectedGrid = new int[][] { { 1, 1, 1 }, { 1, 40, 1 }, { 1, 1, 1 } };
    }

    @Test
    void GivenSmallFractal_WhenEvaluate_ThenCorrectResult()
    {
        fractal.evaluate();
        fractal.waitToFinish();
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                assertEquals(expectedGrid[i][j], 
                             fractal.getIterationGrid()[i][j], 
                             "Failure at i = " + i + ", j = " + j + ", whole grid: " + Arrays.deepToString(fractal.getIterationGrid()));
            }
        }
    }

    @Test
    void GivenSmallFractal_WhenEvaluate_ThenFinishCallbackIsCalled()
    {
        final AtomicBoolean called = new AtomicBoolean(false);
        fractal.setFinishCallback(() -> { called.set(true); });
        fractal.evaluate();
        fractal.waitToFinish();
        assertEquals(true, called.get());
    }
}
