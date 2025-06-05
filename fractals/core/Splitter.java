package fractals.core;

import java.util.ArrayList;
import java.util.List;

public class Splitter
{
    public class Split
    {
        public int start, end;

        public Split() {}
        public Split(int start, int end) { this.start = start; this.end = end; }

        @Override
        public boolean equals(Object other) 
        { 
            if (other == null) return false;
            if (!(other instanceof Split)) return false;
            Split other_split = (Split) other;
            return start == other_split.start && end == other_split.end; 
        }

        @Override
        public String toString() { return start + "-" + end; }
    }

    private int range;

    public Splitter(int range) 
    {
        this.range = range;
    }

    public List<Split> splitInto(final int pieces)
    {
        List<Split> splits = new ArrayList<Split>();
        final int caped_pieces = java.lang.Math.min(pieces, range);
        final int length_of_piece = range / caped_pieces; 
        Split split = new Split();
        for (int i = 0; i < caped_pieces; i++)
        {
            split = new Split();
            split.start = i * length_of_piece;
            split.end = (i + 1) * length_of_piece;
            splits.add(split);
        }
        split.end = range;
        return splits;
    }
}
