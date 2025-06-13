package fractals.core.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fractals.core.Complex;
import fractals.core.Configuration;
import static fractals.core.Configuration.IterationRangeMode.*;

class ConfigurationTest {

    Configuration config = new Configuration.Builder().build();

    @Test
    void getCoordinateTest() {
        assertEquals(new Complex(-2.0, 2.0), config.getCoordinate(0, 0));
        assertEquals(config.max, config.getCoordinate(config.widthHeight - 1, 0));
        assertEquals(config.min, config.getCoordinate(0, config.widthHeight - 1));
        assertTrue(config.getCoordinate(config.widthHeight / 2, config.widthHeight / 2).norm() < 0.01);
    }

    @Test
    void GivenManualIterationMode_WhenGetIterationRange_ThenManualValue() {
        assertEquals(config.iterationRangeManual, config.getIterationRange());
    }

    @Test
    void GivenAutomaticIterationMode_WhenGetIterationRange_ThenAutomaticValue() {
        config = new Configuration.Builder().basedOn(config).iterationRangeMode(AUTOMATIC).build();
        assertEquals(40, config.getIterationRange());

        config = new Configuration.Builder()
                                  .basedOn(config)
                                  .min(new Complex(0.0, 0.0))
                                  .max(new Complex(0.01, 0.01))
                                  .build();
        assertEquals(391, config.getIterationRange());
    }

}