package fractals.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fractals.core.Colorizer;
import fractals.core.Colorizer.Mode;

import java.awt.Color;
import java.awt.image.BufferedImage;

class ColorizerTest {

    private BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    @Test
    void GivenIterationRangeReached_WhenModeBlackWhite_ThenColorBlack() {
        Colorizer colorizer = new Colorizer(new int[][]{{1}}, 1);

        colorizer.setMode(Colorizer.Mode.BLACK_WHITE);
        
        assertEquals(Color.BLACK.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationRangeNotReached_WhenModeBlackWhite_ThenColorWhite() {
        Colorizer colorizer = new Colorizer(new int[][]{{0}}, 1);

        colorizer.setMode(Colorizer.Mode.BLACK_WHITE);
        
        assertEquals(Color.WHITE.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationRangeReached_WhenModeBlackWhiteModulo_ThenColorBlack() {
        Colorizer colorizer = new Colorizer(new int[][]{{1}}, 1);

        colorizer.setMode(Colorizer.Mode.BLACK_WHITE_MODULO);
        
        assertEquals(Color.BLACK.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationRangeNotReached_WhenModeBlackWhiteModulo_AndEvenIteration_ThenColorWhite() {
        Colorizer colorizer = new Colorizer(new int[][]{{2}}, 10);

        colorizer.setMode(Colorizer.Mode.BLACK_WHITE_MODULO);
        
        assertEquals(Color.WHITE.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationRangeNotReached_WhenModeBlackWhiteModulo_AndOddIteration_ThenColorBlack() {
        Colorizer colorizer = new Colorizer(new int[][]{{1}}, 10);

        colorizer.setMode(Colorizer.Mode.BLACK_WHITE_MODULO);
        
        assertEquals(Color.BLACK.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationRangeReached_WhenModeColor_ThenColorBlack() {
        Colorizer colorizer = new Colorizer(new int[][]{{1}}, 1);

        colorizer.setMode(Colorizer.Mode.COLOR);
        
        assertEquals(Color.BLACK.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationWithingFirstThird_WhenModeColor_AndThreeColors_ThenFirstColor() {
        Colorizer colorizer = new Colorizer(new int[][]{{2}}, 9);

        colorizer.setMode(Colorizer.Mode.COLOR);
        colorizer.setColorCollection(new Color[]{Color.RED, Color.GREEN, Color.BLUE});
        
        assertEquals(Color.RED.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationWithingSecondThird_WhenModeColor_AndThreeColors_ThenSecondColor() {
        Colorizer colorizer = new Colorizer(new int[][]{{5}}, 9);

        colorizer.setMode(Colorizer.Mode.COLOR);
        colorizer.setColorCollection(new Color[]{Color.RED, Color.GREEN, Color.BLUE});
        
        assertEquals(Color.GREEN.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationWithingThirdThird_WhenModeColor_AndThreeColors_ThenThirdColor() {
        Colorizer colorizer = new Colorizer(new int[][]{{8}}, 9);

        colorizer.setMode(Colorizer.Mode.COLOR);
        colorizer.setColorCollection(new Color[]{Color.RED, Color.GREEN, Color.BLUE});
        
        assertEquals(Color.BLUE.getRGB(), colorizer.applyTo(image).getRGB(0, 0));
    }
    @Test
    void GivenIterationGrid_WhenAnyMode_ThenApplyColorsToWholeImage() {
        Colorizer colorizer = new Colorizer(new int[][]{{0, 1, 0}, {1, 0, 1}, {0, 0, 1}}, 1);
        image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
        colorizer.setMode(Mode.BLACK_WHITE);
        colorizer.applyTo(image);

        Color[][] expected = new Color[][]{{Color.WHITE, Color.BLACK, Color.WHITE}, {Color.BLACK, Color.WHITE, Color.BLACK}, {Color.WHITE, Color.WHITE, Color.BLACK}};
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j].getRGB(), 
                             image.getRGB(i, j), 
                             "Failure at i = " + i + ", j = " + j + " with expected: " + expected[i][j].toString() + " and actual: " + new Color(image.getRGB(i, j)).toString());
            }
        }
    }
}