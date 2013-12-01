
package numerics;

/**
 *
 * @author Grzegorz Los
 */
public class Interval
{

    public Interval(double beg, double end)
    {
        if (beg > end)
            throw new RuntimeException("Beginning of the interval must not lay above its end.");
        this.beg = beg;
        this.end = end;
    }
    public final double beg;
    public final double end;
}
