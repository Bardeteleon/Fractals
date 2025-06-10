package fractals.core.test; 

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.Collections;

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
    
    @Test
    void GivenSmallFractal_WhenEvaluate_ThenStatusUpdateCallbackIsCalled()
    {
        final List<Integer> statusUpdates = new ArrayList<Integer>();
        final List<Integer> expectedStatusUpdates = Arrays.asList(0, 100);
        fractal.setStatusUpdateCallback((status) -> { statusUpdates.add(status); });
        fractal.evaluate();
        fractal.waitToFinish();
        assertTrue(statusUpdates.equals(expectedStatusUpdates), "statusUpdates: " + statusUpdates + ", expectedStatusUpdates: " + expectedStatusUpdates);
    }

    @Test
    void GivenBigFractal_WhenEvaluateWithOneThread_ThenStatusUpdateCallbackIsCalled()
    {
        final List<Integer> statusUpdates = Collections.synchronizedList(new ArrayList<Integer>());
        final List<Integer> expectedStatusUpdates = Stream.concat(IntStream.range(0, 100).boxed(), Stream.of(100)).collect(Collectors.toList());
        fractal = new Fractal(new Configuration.Builder().widthHeight(400).maxThreads(1).build());
        fractal.setStatusUpdateCallback((status) -> { statusUpdates.add(status); });
        fractal.evaluate();
        fractal.waitToFinish();
        assertTrue(statusUpdates.equals(expectedStatusUpdates), "statusUpdates: " + statusUpdates + ", expectedStatusUpdates: " + expectedStatusUpdates);
    }

    @Test
    void GivenBigFractal_WhenEvaluateWithMultipleThreads_ThenStatusUpdateCallbackIsCalled()
    {
        assumeTrue(Runtime.getRuntime().availableProcessors() >= 2);
        final List<Integer> statusUpdates = Collections.synchronizedList(new ArrayList<Integer>());
        final List<Integer> expectedStatusUpdates = Stream.concat(IntStream.range(0, 99).boxed(), Stream.of(100)).collect(Collectors.toList());
        fractal = new Fractal(new Configuration.Builder().widthHeight(400).maxThreads(2).build());
        fractal.setStatusUpdateCallback((status) -> { statusUpdates.add(status); });
        fractal.evaluate();
        fractal.waitToFinish();
        
        Collections.sort(statusUpdates); // it can happen that the status updates are not in order due to thread switching between status increment and callback call

        assertTrue(statusUpdates.equals(expectedStatusUpdates), "statusUpdates: " + statusUpdates + ", expectedStatusUpdates: " + expectedStatusUpdates);
    }
}
