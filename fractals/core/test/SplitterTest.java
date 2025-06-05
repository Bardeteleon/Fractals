package fractals.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.List;

import fractals.core.Splitter;
import fractals.core.Splitter.Split;

class SplitterTest
{
    @Test
    void GivenRange_WhenSplit_ThenSplittedSizeFits()
    {
        Splitter splitter = new Splitter(5);
        assertEquals(3, splitter.splitInto(3).size());
    }

    @Test
    void GivenRange_WhenSplit_ThenSplittedStartAndEndAreCorrect()
    {
        Splitter splitter = new Splitter(5);
        List<Splitter.Split> split = splitter.splitInto(3);
        assertTrue(split.get(0).equals(splitter.new Split(0, 1)), "0: " + split.get(0).toString());
        assertTrue(split.get(1).equals(splitter.new Split(1, 2)), "1: " + split.get(1).toString());
        assertTrue(split.get(2).equals(splitter.new Split(2, 5)), "2: " + split.get(2).toString());
    }

    @Test
    void GivenRange_WhenSplitIntoMorePiecesThanInRange_ThenSplittedSizeEqualsRange()
    {
        Splitter splitter = new Splitter(5);
        assertEquals(5, splitter.splitInto(6).size());
    }
}