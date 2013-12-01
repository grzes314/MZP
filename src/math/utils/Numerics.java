package math.utils;

import static java.lang.Math.abs;

/**
 *
 * @author Grzegorz Los
 */
public class Numerics
{
    public static boolean doublesEqual(double a, double b)
    {
        return doublesEqual(a, b, 1e-9);
    }
    
    public static double absoluteError(double a, double b)
    {
        return Math.abs(a-b);
    }
    
    public static double relativeError(double a, double b)
    {
        return Math.abs((a - b) / b);
    }
    
    public static boolean doublesEqual(double a, double b, double eps)
    {
        double err = Math.min( absoluteError(a,b),
                               relativeError(a,b));
        return Math.abs(err) < eps;        
    }
    
    /**
     * Checks if given value may be considered as zero.
     * @param d any real number.
     * @return true if and only if 'd' may be considered as zero..
     */
    public static boolean isZero(double a)
    {
        return abs(a) < 1e-9;
    }
    
    public static double plus(double a)
    {
        return (a > 0 ? a : 0);
    }
}
