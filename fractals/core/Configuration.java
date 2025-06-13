package fractals.core;

import fractals.core.Complex;
import fractals.core.Variant;

import java.util.Optional;

public class Configuration
{
    public static enum IterationRangeMode {
        MANUAL, AUTOMATIC
    }

    public final Complex min;
    public final Complex max;
    public final int widthHeight;
    public final int iterationRangeManual;
    public final IterationRangeMode iterationRangeMode;
    public final Variant variant;
    public final Optional<Complex> variant_parameter;
    public final int maxThreads;

    public Configuration(Complex min, Complex max, int widthHeight, int iterationRangeManual, IterationRangeMode iterationRangeMode, Variant variant, Optional<Complex> variant_parameter, int maxThreads) {
        this.min = min;
        this.max = max;
        this.widthHeight = widthHeight;
        this.iterationRangeManual = iterationRangeManual;
        this.iterationRangeMode = iterationRangeMode;
        this.variant = variant;
        this.variant_parameter = variant_parameter;
        this.maxThreads = maxThreads;
    }

    public int getIterationRange()
    {
        switch (iterationRangeMode)
        {
            case AUTOMATIC: return getAutomaticIterationRange();
            case MANUAL: return iterationRangeManual;
            default: return 0;
        }
    }

	public Complex getCoordinate(int x, int y)
	{
		return new Complex(min.getReal() + x * ((max.getReal() - min.getReal()) / (widthHeight - 1)),
		                   max.getImag() + y * ((min.getImag() - max.getImag()) / (widthHeight - 1)));
	}

    private int getAutomaticIterationRange()
    {
        final double base = 1.6;
        final double minIteration = 40;
        final double maxIteration = 10000;
        final double currentImaginaryDiff = Math.abs(max.getImag() - min.getImag());
        final double inverseImaginaryDiff = 1.0 / currentImaginaryDiff;
        final double iterationAdheringToMin = minIteration * Math.max(1.0, Math.log(inverseImaginaryDiff) / Math.log(base));
        final double iterationAdheringToMinAndMax = Math.min(maxIteration, iterationAdheringToMin);
        return (int) iterationAdheringToMinAndMax;
    }

    public static class Builder 
    {
        private Complex min = new Complex(-2.0, -2.0);
        private Complex max = new Complex(+2.0, +2.0);
        private int widthHeight = 1000;
        private int iterationRangeManual = 40;
        private IterationRangeMode iterationRangeMode = IterationRangeMode.MANUAL;
        private Variant variant = Variant.MANDELBROT;
        private Optional<Complex> variant_parameter = Optional.empty();
        private int maxThreads = Runtime.getRuntime().availableProcessors();

        public Builder basedOn(Configuration configuration) {
            this.min = configuration.min;
            this.max = configuration.max;
            this.widthHeight = configuration.widthHeight;
            this.iterationRangeManual = configuration.iterationRangeManual;
            this.iterationRangeMode = configuration.iterationRangeMode;
            this.variant = configuration.variant;
            this.variant_parameter = configuration.variant_parameter;
            this.maxThreads = configuration.maxThreads;
            return this;
        }

        public Configuration build() {
            return new Configuration(min, max, widthHeight, iterationRangeManual, iterationRangeMode, variant, variant_parameter, maxThreads);
        }

        public Builder min(Complex min) {
            this.min = min;
            return this;
        }

        public Builder max(Complex max) {
            this.max = max;
            return this;
        }

        public Builder widthHeight(int widthHeight) {
            this.widthHeight = widthHeight;
            return this;
        }

        public Builder iterationRangeManual(int iterationRangeManual) {
            this.iterationRangeManual = iterationRangeManual;
            return this;
        }

        public Builder iterationRangeMode(IterationRangeMode iterationRangeMode) {
            this.iterationRangeMode = iterationRangeMode;
            return this;
        }

        public Builder variant(Variant variant) {
            this.variant = variant;
            return this;
        }

        public Builder variant_parameter(Optional<Complex> variant_parameter) {
            this.variant_parameter = variant_parameter;
            return this;
        }

        public Builder maxThreads(int maxThreads) {
            this.maxThreads = maxThreads;
            return this;
        }
    }
}

