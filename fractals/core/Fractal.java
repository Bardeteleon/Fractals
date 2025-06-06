package fractals.core;

import fractals.core.Complex;
import fractals.core.Configuration;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class Fractal
{
	public final static int MIN_STATUS = 0;
	public final static int MAX_STATUS = 100;

	private final Configuration configuration;
    private final int[][] iterationGrid;

    private AtomicInteger status = new AtomicInteger(0);
	private boolean stop = false;
	private Thread waitThread;
	private Runnable finishCallback;
	private Consumer<Integer> statusUpdateCallback;

    public Fractal(Configuration configuration)
    {
		this.configuration = configuration;
        this.iterationGrid = new int[this.configuration.widthHeight][this.configuration.widthHeight];
    }

	public void setFinishCallback(Runnable finishCallback) {
		this.finishCallback = finishCallback;
	}

	public void setStatusUpdateCallback(Consumer<Integer> statusUpdateCallback) {
		this.statusUpdateCallback = statusUpdateCallback;
	}

    public void evaluate()
	{
		final List<Splitter.Split> widhtHeightSplits = new Splitter(configuration.widthHeight).splitInto(Runtime.getRuntime().availableProcessors());
		final List<Thread> threads = new ArrayList<Thread>(widhtHeightSplits.size());

		setStatus(MIN_STATUS);

		for (Splitter.Split currentSplit : widhtHeightSplits)
		{
			final Thread thread = new Thread()
			{
				@Override
				public void run()
				{
					for (int i = 0; i < configuration.widthHeight; i++)
					{
						for (int j = currentSplit.start; j < currentSplit.end; j++)
						{
							iterationGrid[i][j] = iterateAt(transform(i, j));
							if (stop)
							{
								return;
							}
						}
						updateStatus(widhtHeightSplits.size(), i);
					}
				}
			};
			thread.setDaemon(true);
			thread.start();
			threads.add(thread);
		}
		waitThread = new Thread()
		{
			@Override
			public void run()
			{
				for (Thread t : threads)
				{
					try
					{
						t.join();
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				setStatus(MAX_STATUS);
				callFinishCallback();
			};
		};
		waitThread.setDaemon(true);
		waitThread.start();
	}

	public void stop()
	{
		stop = true;
	}

	public void waitToFinish()
	{
		try
		{
			waitThread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public int[][] getIterationGrid()
	{
		return iterationGrid;
	}

	private int iterateAt(Complex coordinate)
	{
		if ((configuration.variant == Variant.JULIA) && configuration.variant_parameter.isPresent())
			return Iterator.iterateJulia(coordinate, configuration.variant_parameter.get(), configuration.iterationRange);
		else
			return Iterator.iterateMandelbrot(coordinate, configuration.iterationRange);
	}

	private void updateStatus(int splitSize, int relativeStatus)
	{
		final int statusPerSplit = MAX_STATUS / splitSize;
		final int incrementIntervall = configuration.widthHeight / statusPerSplit;
		if ((incrementIntervall != 0) && 
			(relativeStatus != 0) &&
			(relativeStatus % incrementIntervall == 0))
		{
			final int currentStatus = status.incrementAndGet();
			callStatusUpdateCallback(currentStatus);
		}
	}

	private Complex transform(int gridX, int gridY)
	{
		return new Complex(
			configuration.min.getReal() + 
			   gridX * ((configuration.max.getReal() - configuration.min.getReal()) / (configuration.widthHeight - 1)),
		    configuration.max.getImag() + 
		       gridY * ((configuration.min.getImag() - configuration.max.getImag()) / (configuration.widthHeight - 1)));
	}

	private void callFinishCallback()
	{
		if (Objects.nonNull(finishCallback))
		{
			finishCallback.run();
		}
	}

	private void callStatusUpdateCallback(int status)
	{
		if (Objects.nonNull(statusUpdateCallback))
		{
			statusUpdateCallback.accept(status);
		}
	}

	private void setStatus(int status) 
	{
		this.status.set(status);
		callStatusUpdateCallback(status);
	}
}