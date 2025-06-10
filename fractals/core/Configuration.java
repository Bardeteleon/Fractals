package fractals.core;

import fractals.core.Complex;
import fractals.core.Variant;

import java.util.Optional;

public class Configuration
{
    public final Complex min;
    public final Complex max;
    public final int widthHeight;
    public final int iterationRange;
    public final Variant variant;
    public final Optional<Complex> variant_parameter;
    public final int maxThreads;

    public Configuration(Complex min, Complex max, int widthHeight, int iterationRange, Variant variant, Optional<Complex> variant_parameter, int maxThreads) {
        this.min = min;
        this.max = max;
        this.widthHeight = widthHeight;
        this.iterationRange = iterationRange;
        this.variant = variant;
        this.variant_parameter = variant_parameter;
        this.maxThreads = maxThreads;
    }

    public static class Builder 
    {
        private Complex min = new Complex(-2.0, -2.0);
        private Complex max = new Complex(+2.0, +2.0);
        private int widthHeight = 1000;
        private int iterationRange = 40;
        private Variant variant = Variant.MANDELBROT;
        private Optional<Complex> variant_parameter = Optional.empty();
        private int maxThreads = Runtime.getRuntime().availableProcessors();

        public Configuration build() {
            return new Configuration(min, max, widthHeight, iterationRange, variant, variant_parameter, maxThreads);
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

        public Builder iterationRange(int iterationRange) {
            this.iterationRange = iterationRange;
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

